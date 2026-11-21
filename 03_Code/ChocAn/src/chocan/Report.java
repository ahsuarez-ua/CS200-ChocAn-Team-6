package chocan;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclass of the member, provider, and summary reports. A report is a
 * header, a list of ReportLines, and a footer. Each subclass decides what
 * its header, footer, and file name look like; this class assembles the
 * text and writes it to a file.
 *
 * @author Peyton Hardy
 */
public abstract class Report {
    protected final String reportDate;   // week-ending date, MM-DD-YYYY
    protected String fileName;
    private final Path folder;
    private final List<ReportLine> lines = new ArrayList<>();

    protected Report(LocalDate weekEnd, Path folder) {
        this.reportDate = weekEnd.format(ServiceRecord.DATE);
        this.folder = folder;
    }

    public void addLine(ReportLine line) {
        lines.add(line);
    }

    protected abstract String formatHeader();

    /** Text after the body. Subclasses with totals override this. */
    protected String formatFooter() {
        return "";
    }

    /** The file name only, for example "AvaThompson_11-13-2026.txt". */
    protected abstract String buildFileName();

    /** The full text of the report: header, each line, then footer. */
    public String getText() {
        StringBuilder sb = new StringBuilder(formatHeader());
        for (ReportLine line : lines) {
            sb.append(line).append('\n');
        }
        sb.append(formatFooter());
        return sb.toString();
    }

    /** Writes the report into its folder and returns the file's path. */
    public Path writeToFile() throws IOException {
        if (fileName == null) {
            fileName = buildFileName();
        }
        Files.createDirectories(folder);
        Path file = folder.resolve(fileName);
        Files.writeString(file, getText(), StandardCharsets.UTF_8);
        return file;
    }

    protected boolean isEmpty() {
        return lines.isEmpty();
    }

    /** Turns a name into a safe file-name piece: "Dr. Sarah Mitchell" -> "DrSarahMitchell". */
    protected static String fileSafe(String name) {
        String cleaned = name.replaceAll("[^A-Za-z0-9]", "");
        return cleaned.isEmpty() ? "Unnamed" : cleaned;
    }
}

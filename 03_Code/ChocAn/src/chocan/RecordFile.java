package chocan;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the plain text data files used by the registries.
 * Each line is one record, with fields separated by '|'. Because every
 * registry saves after each change, records persist between program runs.
 *
 * @author Gabriel Mckinney
 */
public final class RecordFile {
    private static final String SEP = "|";

    private RecordFile() { }

    /** True if the data file exists yet. */
    public static boolean exists(Path file) {
        return Files.exists(file);
    }

    /**
     * Reads every record in the file. Lines with the wrong number of fields
     * are skipped with a warning instead of crashing the program.
     */
    public static List<String[]> read(Path file, int fieldCount) {
        List<String[]> rows = new ArrayList<>();
        if (!Files.exists(file)) {
            return rows;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split("\\|", -1); // -1 keeps empty comments
                if (fields.length == fieldCount) {
                    rows.add(fields);
                } else {
                    System.out.println("Warning: skipped a damaged line in " + file.getFileName());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + file, e);
        }
        return rows;
    }

    /** Replaces the file's contents with the given lines. */
    public static void write(Path file, List<String> lines) {
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save " + file, e);
        }
    }

    /** Joins fields into one line of a data file. */
    public static String join(Object... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(SEP);
            }
            sb.append(fields[i]);
        }
        return sb.toString();
    }
}

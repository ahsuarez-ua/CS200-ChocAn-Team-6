package chocan;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * The provider directory: an alphabetical list of service names with their
 * 6-digit codes and fees. It is written to reports/ProviderDirectory.txt,
 * standing in for the email attachment described in the requirements.
 *
 * @author Gabriel Mckinney
 */
public class ProviderDirectory {
    private final String dateCreated;
    private final Path fileName;
    private final List<Service> services;

    public ProviderDirectory(List<Service> servicesByName, Path folder) {
        this.services = servicesByName;
        this.dateCreated = LocalDate.now().format(ServiceRecord.DATE);
        this.fileName = folder.resolve("ProviderDirectory.txt");
    }

    /** The whole directory as text. */
    public String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append("CHOCAN PROVIDER DIRECTORY (created ").append(dateCreated).append(")\n\n");
        sb.append(String.format("%-20s  %-6s  %10s%n", "Service", "Code", "Fee"));
        sb.append("-".repeat(40)).append('\n');
        for (Service s : services) {
            sb.append(formatEntry(s)).append('\n');
        }
        return sb.toString();
    }

    public void writeToFile() throws IOException {
        Files.createDirectories(fileName.getParent());
        Files.writeString(fileName, getText(), StandardCharsets.UTF_8);
    }

    public Path getFileName() {
        return fileName;
    }

    private String formatEntry(Service service) {
        return String.format("%-20s  %06d  %10s",
                service.getName(), service.getCode(), Service.formatMoney(service.getFee()));
    }
}

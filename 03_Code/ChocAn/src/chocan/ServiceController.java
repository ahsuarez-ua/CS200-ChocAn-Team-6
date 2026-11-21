package chocan;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Logic behind the provider terminal: looking up services, recording a
 * service (Bill For Service), and creating the provider directory
 * (Request Provider Directory).
 *
 * @author Gabriel Mckinney
 */
public class ServiceController {
    private final ServiceCatalog serviceCatalog;
    private final ServiceLog serviceLog;
    private final Path reportsDir;

    public ServiceController(ServiceCatalog serviceCatalog, ServiceLog serviceLog, Path reportsDir) {
        this.serviceCatalog = serviceCatalog;
        this.serviceLog = serviceLog;
        this.reportsDir = reportsDir;
    }

    /** The service with this code, or null for a nonexistent code. */
    public Service lookUpService(int serviceCode) {
        return serviceCatalog.findService(serviceCode);
    }

    /** Writes a service record stamped with the current date and time, and returns it. */
    public ServiceRecord recordService(int providerNumber, int memberNumber, int serviceCode,
                                       LocalDate serviceDate, String comments) {
        ServiceRecord record = new ServiceRecord(LocalDateTime.now().withNano(0), serviceDate,
                providerNumber, memberNumber, serviceCode, comments);
        serviceLog.addRecord(record);
        return record;
    }

    /** Builds the alphabetical provider directory and writes it to a file. */
    public ProviderDirectory createProviderDirectory() throws IOException {
        ProviderDirectory directory = new ProviderDirectory(serviceCatalog.getServicesByName(), reportsDir);
        directory.writeToFile();
        return directory;
    }
}

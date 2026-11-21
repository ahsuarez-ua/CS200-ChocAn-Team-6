package chocan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Every service ChocAn pays for, loaded from data/services.txt.
 * On the first run it starts with a sample list of services.
 *
 * @author Gabriel Mckinney
 */
public class ServiceCatalog {
    private final Path fileName;
    private final Map<Integer, Service> services = new LinkedHashMap<>();

    public ServiceCatalog(Path dataDir) {
        this.fileName = dataDir.resolve("services.txt");
        load();
    }

    /** The service with this 6-digit code, or null if there is none. */
    public Service findService(int serviceCode) {
        return services.get(serviceCode);
    }

    /** All services in alphabetical order by name. */
    public List<Service> getServicesByName() {
        List<Service> list = new ArrayList<>(services.values());
        list.sort(Comparator.comparing(Service::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    private void load() {
        if (!RecordFile.exists(fileName)) {
            add(new Service(598470, "Dietitian", 75.00));
            add(new Service(883948, "Aerobics Session", 40.00));
            add(new Service(112233, "Internist Consult", 150.00));
            add(new Service(445566, "Exercise Physiology", 60.00));
            add(new Service(778899, "Group Therapy", 35.00));
            add(new Service(102938, "Nutrition Workshop", 25.00));
            List<String> lines = new ArrayList<>();
            for (Service s : services.values()) {
                lines.add(RecordFile.join(s.getCode(), s.getName(), String.format("%.2f", s.getFee())));
            }
            RecordFile.write(fileName, lines);
            return;
        }
        for (String[] f : RecordFile.read(fileName, 3)) {
            add(new Service(Integer.parseInt(f[0]), f[1], Double.parseDouble(f[2])));
        }
    }

    private void add(Service s) {
        services.put(s.getCode(), s);
    }
}

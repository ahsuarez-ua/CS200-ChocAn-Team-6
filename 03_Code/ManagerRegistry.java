package chocan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the managers who may log in to the manager terminal, loaded
 * from data/managers.txt. On the first run it creates one sample manager
 * (number 999999999).
 *
 * @author Jacob Wilson
 */
public class ManagerRegistry {
    private final Path fileName;
    private final Map<Integer, Manager> managers = new LinkedHashMap<>();

    public ManagerRegistry(Path dataDir) {
        this.fileName = dataDir.resolve("managers.txt");
        load();
    }

    public Manager findManager(int managerNumber) {
        return managers.get(managerNumber);
    }

    private void load() {
        if (!RecordFile.exists(fileName)) {
            managers.put(999999999, new Manager(999999999, "Marcus Hale"));
            List<String> lines = new ArrayList<>();
            for (Manager m : managers.values()) {
                lines.add(RecordFile.join(m.getNumber(), m.getName()));
            }
            RecordFile.write(fileName, lines);
            return;
        }
        for (String[] f : RecordFile.read(fileName, 2)) {
            Manager m = new Manager(Integer.parseInt(f[0]), f[1]);
            managers.put(m.getNumber(), m);
        }
    }
}

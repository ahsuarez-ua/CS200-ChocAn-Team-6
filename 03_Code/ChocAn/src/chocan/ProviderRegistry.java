package chocan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores every provider and saves them to data/providers.txt.
 * Assigns new 9-digit provider numbers. On the first run it starts
 * with sample providers.
 *
 * @author Aaron Suarez
 */
public class ProviderRegistry {
    private static final int FIRST_NUMBER = 200000001;

    private final Path fileName;
    private int lastNumber = FIRST_NUMBER - 1;
    private final Map<Integer, Provider> providers = new LinkedHashMap<>();

    public ProviderRegistry(Path dataDir) {
        this.fileName = dataDir.resolve("providers.txt");
        load();
    }

    public Provider findProvider(int providerNumber) {
        return providers.get(providerNumber);
    }

    public void addProvider(Provider provider) {
        providers.put(provider.getNumber(), provider);
        lastNumber = Math.max(lastNumber, provider.getNumber());
        save();
    }

    public boolean removeProvider(int providerNumber) {
        boolean removed = providers.remove(providerNumber) != null;
        if (removed) {
            save();
        }
        return removed;
    }

    /** The number to give the next new provider. */
    public int nextProviderNumber() {
        return lastNumber + 1;
    }

    /** All providers, sorted by provider number. */
    public List<Provider> getAllProviders() {
        List<Provider> list = new ArrayList<>(providers.values());
        list.sort(Comparator.comparingInt(Provider::getNumber));
        return list;
    }

    /** Writes every provider to the data file. Called after every change. */
    public void save() {
        List<String> lines = new ArrayList<>();
        for (Provider p : providers.values()) {
            lines.add(RecordFile.join(p.getNumber(), p.getName(), p.getAddress(), p.getCity(),
                    p.getState(), p.getZipCode()));
        }
        RecordFile.write(fileName, lines);
    }

    private void load() {
        if (!RecordFile.exists(fileName)) {
            addSampleProviders();
            return;
        }
        for (String[] f : RecordFile.read(fileName, 6)) {
            Provider p = new Provider(Integer.parseInt(f[0]), f[1], f[2], f[3], f[4], f[5]);
            providers.put(p.getNumber(), p);
            lastNumber = Math.max(lastNumber, p.getNumber());
        }
    }

    private void put(Provider provider) {
        providers.put(provider.getNumber(), provider);
        lastNumber = Math.max(lastNumber, provider.getNumber());
    }

    private void addSampleProviders() {
        put(new Provider(200000001, "Dr. Sarah Mitchell", "100 Health Plaza", "Tuscaloosa", "AL", "35401"));
        put(new Provider(200000002, "Fit Life Center", "2300 McFarland Blvd", "Northport", "AL", "35476"));
        put(new Provider(200000003, "Dr. James Patel", "750 University Blvd", "Tuscaloosa", "AL", "35401"));
        save();
    }
}

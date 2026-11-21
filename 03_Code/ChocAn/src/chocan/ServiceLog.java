package chocan;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Every service record ever written, saved to data/records.txt.
 * The accounting procedure and reports read the week's records from here.
 * On the first run it starts with sample records from the past week so
 * the reports have data right away.
 *
 * @author Gabriel Mckinney
 */
public class ServiceLog {
    private final Path fileName;
    private final List<ServiceRecord> records = new ArrayList<>();

    public ServiceLog(Path dataDir) {
        this.fileName = dataDir.resolve("records.txt");
        load();
    }

    /** Adds a record and saves it to disk right away. */
    public void addRecord(ServiceRecord record) {
        records.add(record);
        save();
    }

    /**
     * Records received by the computer between start and end, inclusive,
     * in the order they were received.
     */
    public List<ServiceRecord> getRecordsBetween(LocalDate start, LocalDate end) {
        List<ServiceRecord> result = new ArrayList<>();
        for (ServiceRecord r : records) {
            LocalDate received = r.getDateReceived().toLocalDate();
            if (!received.isBefore(start) && !received.isAfter(end)) {
                result.add(r);
            }
        }
        return result;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (ServiceRecord r : records) {
            lines.add(RecordFile.join(
                    r.getDateReceived().format(ServiceRecord.DATE_TIME),
                    r.getServiceDate().format(ServiceRecord.DATE),
                    r.getProviderNumber(), r.getMemberNumber(), r.getServiceCode(),
                    r.getComments()));
        }
        RecordFile.write(fileName, lines);
    }

    private void load() {
        if (!RecordFile.exists(fileName)) {
            addSampleRecords();
            return;
        }
        for (String[] f : RecordFile.read(fileName, 6)) {
            records.add(new ServiceRecord(
                    LocalDateTime.parse(f[0], ServiceRecord.DATE_TIME),
                    LocalDate.parse(f[1], ServiceRecord.DATE),
                    Integer.parseInt(f[2]), Integer.parseInt(f[3]), Integer.parseInt(f[4]),
                    f[5]));
        }
    }

    private void addSampleRecords() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        sample(now, 5, 200000001, 100000001, 598470, "Initial diet plan");
        sample(now, 4, 200000002, 100000001, 883948, "");
        sample(now, 3, 200000002, 100000002, 883948, "Good progress");
        sample(now, 2, 200000001, 100000004, 112233, "Follow up in 2 weeks");
        sample(now, 1, 200000002, 100000002, 445566, "");
        save();
    }

    private void sample(LocalDateTime now, int daysAgo, int provider, int member, int code, String comments) {
        LocalDateTime when = now.minusDays(daysAgo);
        records.add(new ServiceRecord(when, when.toLocalDate(), provider, member, code, comments));
    }
}

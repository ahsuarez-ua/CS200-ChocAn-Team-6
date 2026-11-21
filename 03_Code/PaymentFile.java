package chocan;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The weekly EFT data file: one PaymentRecord per provider to be paid.
 * In the real system the bank would read this file; here it is only written.
 *
 * @author Peyton Hardy
 */
public class PaymentFile {
    private final String weekEnding;
    private final Path fileName;
    private final List<PaymentRecord> records = new ArrayList<>();

    public PaymentFile(LocalDate weekEnd, Path folder) {
        this.weekEnding = weekEnd.format(ServiceRecord.DATE);
        this.fileName = folder.resolve("EFT_" + weekEnding + ".txt");
    }

    public void addRecord(PaymentRecord record) {
        records.add(record);
    }

    /** Total amount to transfer to all providers. */
    public double getTotal() {
        double total = 0;
        for (PaymentRecord r : records) {
            total += r.getAmount();
        }
        return total;
    }

    public Path writeToFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("CHOCAN EFT DATA - week ending ").append(weekEnding).append("\n\n");
        sb.append(String.format("%-25s  %-9s  %12s%n", "Provider", "Number", "Amount"));
        sb.append("-".repeat(50)).append('\n');
        for (PaymentRecord r : records) {
            sb.append(r.format()).append('\n');
        }
        sb.append("\nTotal to transfer: ").append(Service.formatMoney(getTotal())).append('\n');
        Files.createDirectories(fileName.getParent());
        Files.writeString(fileName, sb.toString(), StandardCharsets.UTF_8);
        return fileName;
    }
}

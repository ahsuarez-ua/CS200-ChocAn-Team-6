package chocan;

import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Weekly report for one provider: name, number, and address, then each
 * service provided that week (date of service, date and time received,
 * member name and number, service code, fee), then the total number of
 * consultations and the total fee for the week.
 *
 * @author Peyton Hardy
 */
public class ProviderReport extends Report {
    private final Provider provider;
    private int totalConsultations;
    private double totalFee;

    public ProviderReport(Provider provider, LocalDate weekEnd, Path folder) {
        super(weekEnd, folder);
        this.provider = provider;
    }

    /** Adds one consultation's fee to the weekly totals. */
    public void addConsultation(double fee) {
        totalConsultations++;
        totalFee += fee;
    }

    public int getTotalConsultations() { return totalConsultations; }
    public double getTotalFee() { return totalFee; }

    @Override
    protected String formatHeader() {
        return "CHOCAN PROVIDER REPORT - week ending " + reportDate + "\n\n"
                + "Provider name:   " + provider.getName() + "\n"
                + "Provider number: " + provider.getNumber() + "\n"
                + "Street address:  " + provider.getAddress() + "\n"
                + "City:            " + provider.getCity() + "\n"
                + "State:           " + provider.getState() + "\n"
                + "Zip code:        " + provider.getZipCode() + "\n\n"
                + String.format("%-12s  %-19s  %-25s  %-9s  %-6s  %10s%n",
                        "Service Date", "Received", "Member", "Member #", "Code", "Fee")
                + "-".repeat(92) + "\n";
    }

    @Override
    protected String formatFooter() {
        return (isEmpty() ? "No services provided this week.\n" : "")
                + "\nTotal number of consultations: " + totalConsultations + "\n"
                + "Total fee for the week:        " + Service.formatMoney(totalFee) + "\n";
    }

    @Override
    protected String buildFileName() {
        return fileSafe(provider.getName()) + "_" + reportDate + ".txt";
    }
}

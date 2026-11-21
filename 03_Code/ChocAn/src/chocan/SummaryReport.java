package chocan;

import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Weekly summary report for the manager (accounts payable): each provider
 * to be paid that week with their number of consultations and total fee,
 * then the total number of providers, total consultations, and overall fee.
 *
 * @author Peyton Hardy
 */
public class SummaryReport extends Report {
    private int totalProviders;
    private int totalConsultations;
    private double overallFee;

    public SummaryReport(LocalDate weekEnd, Path folder) {
        super(weekEnd, folder);
    }

    /** Adds one provider's line and includes it in the totals. */
    public void addProvider(String providerName, int providerNumber, int consultations, double fee) {
        addLine(new ReportLine(String.format("%-25s  %-9d  %13d  %12s",
                providerName, providerNumber, consultations, Service.formatMoney(fee))));
        totalProviders++;
        totalConsultations += consultations;
        overallFee += fee;
    }

    public int getTotalProviders() { return totalProviders; }
    public int getTotalConsultations() { return totalConsultations; }
    public double getOverallFee() { return overallFee; }

    @Override
    protected String formatHeader() {
        return "CHOCAN SUMMARY REPORT (ACCOUNTS PAYABLE) - week ending " + reportDate + "\n\n"
                + String.format("%-25s  %-9s  %13s  %12s%n", "Provider", "Number", "Consultations", "Total Fee")
                + "-".repeat(65) + "\n";
    }

    @Override
    protected String formatFooter() {
        return (isEmpty() ? "No providers are to be paid this week.\n" : "")
                + "\nTotal providers who provided services: " + totalProviders + "\n"
                + "Total number of consultations:         " + totalConsultations + "\n"
                + "Overall fee total:                     " + Service.formatMoney(overallFee) + "\n";
    }

    @Override
    protected String buildFileName() {
        return "SummaryReport_" + reportDate + ".txt";
    }
}

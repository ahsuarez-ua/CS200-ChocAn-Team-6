package chocan;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Runs the main accounting procedure and produces reports on request.
 * The week covers the seven days ending on the week-ending date. Every
 * report for a week is written to reports/week-ending-MM-DD-YYYY.
 *
 * Used by the Scheduler (Run Weekly Accounting Procedure) and by the
 * ManagerTerminal (Generate Reports).
 *
 * @author Peyton Hardy
 */
public class AccountingController {
    private LocalDate weekStart;
    private LocalDate weekEnd;

    private final ServiceLog serviceLog;
    private final MemberRegistry memberRegistry;
    private final ProviderRegistry providerRegistry;
    private final ReportController reportController;
    private final Path reportsDir;

    public AccountingController(ServiceLog serviceLog, MemberRegistry memberRegistry,
                                ProviderRegistry providerRegistry, ReportController reportController,
                                Path reportsDir) {
        this.serviceLog = serviceLog;
        this.memberRegistry = memberRegistry;
        this.providerRegistry = providerRegistry;
        this.reportController = reportController;
        this.reportsDir = reportsDir;
        this.weekEnd = LocalDate.now();
        this.weekStart = weekEnd.minusDays(6);
    }

    /** Chooses which week the next reports cover. */
    public void setWeekEnding(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
        this.weekStart = weekEnd.minusDays(6);
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    /** Folder for this week's reports. */
    public Path getWeekFolder() {
        return reportsDir.resolve("week-ending-" + weekEnd.format(ServiceRecord.DATE));
    }

    /**
     * The main accounting procedure: member reports, provider reports, the
     * summary report, and the EFT data for the week.
     *
     * @return paths of every file written
     */
    public List<Path> runMainAccountingProcedure() throws IOException {
        List<Path> written = new ArrayList<>();
        for (MemberReport r : produceMemberReports()) {
            written.add(r.writeToFile());
        }
        for (ProviderReport r : produceProviderReports()) {
            written.add(r.writeToFile());
        }
        written.add(produceSummaryReport().writeToFile());
        written.add(producePaymentData().writeToFile());
        return written;
    }

    /** Member reports for every member who received a service this week. */
    public List<MemberReport> produceMemberReports() {
        List<ServiceRecord> records = weekRecords();
        List<MemberReport> reports = new ArrayList<>();
        for (Member m : memberRegistry.getAllMembers()) {
            if (hasMember(records, m.getID())) {
                reports.add(reportController.createMemberReport(m, records, weekEnd, memberFolder()));
            }
        }
        return reports;
    }

    /** Member report for one member, even if they had no services. Null if no such member. */
    public MemberReport produceMemberReport(int memberNumber) {
        Member m = memberRegistry.findMember(memberNumber);
        if (m == null) {
            return null;
        }
        return reportController.createMemberReport(m, weekRecords(), weekEnd, memberFolder());
    }

    /** Provider reports for every provider who provided a service this week. */
    public List<ProviderReport> produceProviderReports() {
        List<ServiceRecord> records = weekRecords();
        List<ProviderReport> reports = new ArrayList<>();
        for (Provider p : providerRegistry.getAllProviders()) {
            if (hasProvider(records, p.getNumber())) {
                reports.add(reportController.createProviderReport(p, records, weekEnd, providerFolder()));
            }
        }
        return reports;
    }

    /** Provider report for one provider, even if they had no services. Null if no such provider. */
    public ProviderReport produceProviderReport(int providerNumber) {
        Provider p = providerRegistry.findProvider(providerNumber);
        if (p == null) {
            return null;
        }
        return reportController.createProviderReport(p, weekRecords(), weekEnd, providerFolder());
    }

    public SummaryReport produceSummaryReport() {
        return reportController.createSummaryReport(weekRecords(), weekEnd, getWeekFolder());
    }

    /** EFT data: the amount owed to each provider who provided services this week. */
    public PaymentFile producePaymentData() {
        PaymentFile file = new PaymentFile(weekEnd, getWeekFolder());
        Map<Integer, Double> totals = new TreeMap<>();
        for (ServiceRecord r : weekRecords()) {
            totals.merge(r.getProviderNumber(), reportController.fee(r.getServiceCode()), Double::sum);
        }
        for (Map.Entry<Integer, Double> e : totals.entrySet()) {
            Provider p = providerRegistry.findProvider(e.getKey());
            String name = p == null ? "(deleted provider)" : p.getName();
            file.addRecord(new PaymentRecord(name, e.getKey(), e.getValue()));
        }
        return file;
    }

    private List<ServiceRecord> weekRecords() {
        return serviceLog.getRecordsBetween(weekStart, weekEnd);
    }

    private Path memberFolder() {
        return getWeekFolder().resolve("members");
    }

    private Path providerFolder() {
        return getWeekFolder().resolve("providers");
    }

    private static boolean hasMember(List<ServiceRecord> records, int memberNumber) {
        for (ServiceRecord r : records) {
            if (r.getMemberNumber() == memberNumber) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasProvider(List<ServiceRecord> records, int providerNumber) {
        for (ServiceRecord r : records) {
            if (r.getProviderNumber() == providerNumber) {
                return true;
            }
        }
        return false;
    }
}

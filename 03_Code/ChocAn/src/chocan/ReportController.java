package chocan;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds member, provider, and summary reports from a week's service
 * records. It looks up names in the registries and catalog so the reports
 * show names instead of only numbers.
 *
 * @author Peyton Hardy
 */
public class ReportController {
    private final MemberRegistry memberRegistry;
    private final ProviderRegistry providerRegistry;
    private final ServiceCatalog serviceCatalog;

    public ReportController(MemberRegistry memberRegistry, ProviderRegistry providerRegistry,
                            ServiceCatalog serviceCatalog) {
        this.memberRegistry = memberRegistry;
        this.providerRegistry = providerRegistry;
        this.serviceCatalog = serviceCatalog;
    }

    /** One member's services from the records, sorted by date of service. */
    public MemberReport createMemberReport(Member member, List<ServiceRecord> records,
                                           LocalDate weekEnd, Path folder) {
        MemberReport report = new MemberReport(member, weekEnd, folder);
        List<ServiceRecord> mine = new ArrayList<>();
        for (ServiceRecord r : records) {
            if (r.getMemberNumber() == member.getID()) {
                mine.add(r);
            }
        }
        mine.sort(Comparator.comparing(ServiceRecord::getServiceDate));
        for (ServiceRecord r : mine) {
            report.addLine(new ReportLine(String.format("%-12s  %-25s  %-20s",
                    r.getServiceDate().format(ServiceRecord.DATE),
                    providerName(r.getProviderNumber()),
                    serviceName(r.getServiceCode()))));
        }
        return report;
    }

    /** One provider's services from the records, in the order received, with totals. */
    public ProviderReport createProviderReport(Provider provider, List<ServiceRecord> records,
                                               LocalDate weekEnd, Path folder) {
        ProviderReport report = new ProviderReport(provider, weekEnd, folder);
        for (ServiceRecord r : records) {
            if (r.getProviderNumber() != provider.getNumber()) {
                continue;
            }
            double fee = fee(r.getServiceCode());
            report.addLine(new ReportLine(String.format("%-12s  %-19s  %-25s  %-9d  %06d  %10s",
                    r.getServiceDate().format(ServiceRecord.DATE),
                    r.getDateReceived().format(ServiceRecord.DATE_TIME),
                    memberName(r.getMemberNumber()),
                    r.getMemberNumber(),
                    r.getServiceCode(),
                    Service.formatMoney(fee))));
            report.addConsultation(fee);
        }
        return report;
    }

    /** Per-provider consultation counts and fees for every provider paid this week. */
    public SummaryReport createSummaryReport(List<ServiceRecord> records, LocalDate weekEnd, Path folder) {
        SummaryReport report = new SummaryReport(weekEnd, folder);
        Map<Integer, int[]> counts = new TreeMap<>();
        Map<Integer, Double> fees = new TreeMap<>();
        for (ServiceRecord r : records) {
            counts.computeIfAbsent(r.getProviderNumber(), k -> new int[1])[0]++;
            fees.merge(r.getProviderNumber(), fee(r.getServiceCode()), Double::sum);
        }
        for (Integer number : counts.keySet()) {
            report.addProvider(providerName(number), number, counts.get(number)[0], fees.get(number));
        }
        return report;
    }

    /** Fee for a service code; 0 if the service was removed from the catalog. */
    public double fee(int serviceCode) {
        Service s = serviceCatalog.findService(serviceCode);
        return s == null ? 0.0 : s.getFee();
    }

    private String providerName(int number) {
        Provider p = providerRegistry.findProvider(number);
        return p == null ? "(deleted provider)" : p.getName();
    }

    private String memberName(int number) {
        Member m = memberRegistry.findMember(number);
        return m == null ? "(deleted member)" : m.getName();
    }

    private String serviceName(int code) {
        Service s = serviceCatalog.findService(code);
        return s == null ? String.format("%06d", code) : s.getName();
    }
}

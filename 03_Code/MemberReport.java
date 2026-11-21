package chocan;

import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Weekly report for one member: name, number, and address, then each
 * service received that week (date of service, provider name, service name)
 * sorted by date of service.
 *
 * @author Peyton Hardy
 */
public class MemberReport extends Report {
    private final Member member;

    public MemberReport(Member member, LocalDate weekEnd, Path folder) {
        super(weekEnd, folder);
        this.member = member;
    }

    @Override
    protected String formatHeader() {
        return "CHOCAN MEMBER REPORT - week ending " + reportDate + "\n\n"
                + "Member name:    " + member.getName() + "\n"
                + "Member number:  " + member.getID() + "\n"
                + "Street address: " + member.getAddress() + "\n"
                + "City:           " + member.getCity() + "\n"
                + "State:          " + member.getState() + "\n"
                + "Zip code:       " + member.getZipCode() + "\n\n"
                + String.format("%-12s  %-25s  %-20s%n", "Date", "Provider", "Service")
                + "-".repeat(61) + "\n";
    }

    @Override
    protected String formatFooter() {
        return isEmpty() ? "No services received this week.\n" : "";
    }

    @Override
    protected String buildFileName() {
        return fileSafe(member.getName()) + "_" + reportDate + ".txt";
    }
}

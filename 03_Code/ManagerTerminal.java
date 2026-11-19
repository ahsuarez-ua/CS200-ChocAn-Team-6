package chocan;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The manager terminal. After Verify Manager succeeds, the manager can
 * request member, provider, and summary reports for any week (Generate
 * Reports). Each report is shown on screen and written to a file.
 *
 * @author Jacob Wilson
 */
public class ManagerTerminal {
    private int currentManagerNumber;
    private boolean loggedIn;

    private final ConsoleInput in;
    private final VerificationController verificationController;
    private final AccountingController accountingController;

    public ManagerTerminal(ConsoleInput in, VerificationController verificationController,
                           AccountingController accountingController) {
        this.in = in;
        this.verificationController = verificationController;
        this.accountingController = accountingController;
    }

    /** Logs the manager in, then shows the manager menu until they log out. */
    public void start() {
        int number = in.readNumber("Enter your 9-digit manager number: ", 9);
        if (!login(number)) {
            System.out.println("Invalid manager number. Access denied.");
            return;
        }
        System.out.println("Access granted.");
        while (loggedIn) {
            System.out.println();
            System.out.println("------------ Manager Terminal ------------");
            System.out.println("1. Member report(s)");
            System.out.println("2. Provider report(s)");
            System.out.println("3. Summary report");
            System.out.println("0. Log out");
            try {
                switch (in.readChoice("Choose an option: ", 0, 3)) {
                    case 1: requestMemberReports(); break;
                    case 2: requestProviderReport(); break;
                    case 3: requestSummaryReport(); break;
                    default: loggedIn = false; break;
                }
            } catch (IOException e) {
                System.out.println("Error: could not write the report (" + e.getMessage() + ").");
            }
        }
        System.out.println("Manager " + currentManagerNumber + " logged out.");
    }

    /** Verify Manager: asks the VerificationController whether the number is valid. */
    public boolean login(int managerNumber) {
        loggedIn = verificationController.verifyManager(managerNumber) == ValidationResult.VALIDATED;
        if (loggedIn) {
            currentManagerNumber = managerNumber;
        }
        return loggedIn;
    }

    /** One member's report, or reports for every member with services this week. */
    public void requestMemberReports() throws IOException {
        accountingController.setWeekEnding(in.readWeekEnding());
        if (in.confirm("Reports for all members with services this week?")) {
            List<MemberReport> reports = accountingController.produceMemberReports();
            if (reports.isEmpty()) {
                System.out.println("No members received services that week.");
            }
            for (MemberReport r : reports) {
                show(r);
            }
            return;
        }
        MemberReport report = accountingController.produceMemberReport(in.readNumber("Member number: ", 9));
        if (report == null) {
            System.out.println("No member has that number.");
        } else {
            show(report);
        }
    }

    /** One provider's report, or reports for every provider with services this week. */
    public void requestProviderReport() throws IOException {
        accountingController.setWeekEnding(in.readWeekEnding());
        if (in.confirm("Reports for all providers with services this week?")) {
            List<ProviderReport> reports = accountingController.produceProviderReports();
            if (reports.isEmpty()) {
                System.out.println("No providers provided services that week.");
            }
            for (ProviderReport r : reports) {
                show(r);
            }
            return;
        }
        ProviderReport report = accountingController.produceProviderReport(in.readNumber("Provider number: ", 9));
        if (report == null) {
            System.out.println("No provider has that number.");
        } else {
            show(report);
        }
    }

    public void requestSummaryReport() throws IOException {
        accountingController.setWeekEnding(in.readWeekEnding());
        show(accountingController.produceSummaryReport());
    }

    private void show(Report report) throws IOException {
        System.out.println();
        System.out.print(report.getText());
        Path file = report.writeToFile();
        System.out.println("Saved to " + file);
    }
}

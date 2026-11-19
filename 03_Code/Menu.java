package chocan;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point of the ChocAn data processing system. Creates the registries,
 * controllers, and terminals, then shows the main menu, where the user
 * opens the operator, provider, or manager terminal.
 *
 * The main menu also simulates the two outside actors: the Scheduler (to
 * run the weekly accounting procedure on demand) and ACME Accounting
 * Services (to suspend or reinstate a member).
 *
 * Records are saved in the "data" folder and reports in the "reports"
 * folder, both inside the folder the program is run from.
 *
 * @author Jacob Wilson
 */
public class Menu {
    private boolean running;

    private final ConsoleInput in;
    private final OperatorTerminal operatorTerminal;
    private final ProviderTerminal providerTerminal;
    private final ManagerTerminal managerTerminal;
    private final Scheduler scheduler;
    private final ACMEInterface acmeInterface;
    private final AccountingController accountingController;

    public static void main(String[] args) {
        Path base = args.length > 0 ? Paths.get(args[0]) : Paths.get("");
        try {
            new Menu(base, new ConsoleInput(new Scanner(System.in))).run();
        } catch (ConsoleInput.InputClosedException e) {
            System.out.println("Input closed. Exiting.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** Builds every object in the system and connects them. */
    public Menu(Path base, ConsoleInput in) {
        this.in = in;
        Path dataDir = base.resolve("data");
        Path reportsDir = base.resolve("reports");
        boolean firstRun = !RecordFile.exists(dataDir.resolve("members.txt"));

        MemberRegistry memberRegistry = new MemberRegistry(dataDir);
        ProviderRegistry providerRegistry = new ProviderRegistry(dataDir);
        OperatorRegistry operatorRegistry = new OperatorRegistry(dataDir);
        ManagerRegistry managerRegistry = new ManagerRegistry(dataDir);
        ServiceCatalog serviceCatalog = new ServiceCatalog(dataDir);
        ServiceLog serviceLog = new ServiceLog(dataDir);

        VerificationController verificationController = new VerificationController(
                memberRegistry, providerRegistry, operatorRegistry, managerRegistry);
        MemberController memberController = new MemberController(memberRegistry);
        ProviderController providerController = new ProviderController(providerRegistry);
        ServiceController serviceController = new ServiceController(serviceCatalog, serviceLog, reportsDir);
        ReportController reportController = new ReportController(memberRegistry, providerRegistry, serviceCatalog);
        this.accountingController = new AccountingController(serviceLog, memberRegistry,
                providerRegistry, reportController, reportsDir);

        this.operatorTerminal = new OperatorTerminal(in, verificationController, memberController, providerController);
        this.providerTerminal = new ProviderTerminal(in, verificationController, serviceController);
        this.managerTerminal = new ManagerTerminal(in, verificationController, accountingController);
        this.scheduler = new Scheduler(accountingController);
        this.acmeInterface = new ACMEInterface(memberController);

        System.out.println(firstRun
                ? "First run: sample data created in " + dataDir.toAbsolutePath().normalize()
                : "Loaded saved records from " + dataDir.toAbsolutePath().normalize());
    }

    /** Shows the main menu until the user exits. */
    public void run() {
        running = true;
        while (running) {
            showMenu();
            openTerminal(in.readChoice("Choose an option: ", 0, 5));
        }
        System.out.println("Goodbye.");
    }

    private void showMenu() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("   Chocoholics Anonymous (ChocAn) Data Center");
        System.out.println("==============================================");
        System.out.println("1. Operator terminal");
        System.out.println("2. Provider terminal");
        System.out.println("3. Manager terminal");
        System.out.println("4. Scheduler: run main accounting procedure now");
        System.out.println("5. ACME Accounting Services: update member status");
        System.out.println("0. Exit");
    }

    private void openTerminal(int choice) {
        switch (choice) {
            case 1: operatorTerminal.start(); break;
            case 2: providerTerminal.start(); break;
            case 3: managerTerminal.start(); break;
            case 4: runAccountingProcedure(); break;
            case 5: updateMemberStatus(); break;
            default: running = false; break;
        }
    }

    private void runAccountingProcedure() {
        System.out.println("\nRunning the main accounting procedure (normally " + scheduler.getScheduledTime() + ")...");
        try {
            List<Path> files = scheduler.triggerMainAccountingProcedure();
            System.out.println("Procedure complete. Files written to "
                    + accountingController.getWeekFolder() + ":");
            for (Path f : files) {
                System.out.println("  " + accountingController.getWeekFolder().relativize(f));
            }
            System.out.println();
            System.out.print(accountingController.produceSummaryReport().getText());
        } catch (IOException e) {
            System.out.println("Procedure failed: " + e.getMessage());
        }
    }

    private void updateMemberStatus() {
        System.out.println("\n--- ACME Accounting Services ---");
        int number = in.readNumber("Member number: ", 9);
        MemberStatus status = in.confirm("Suspend this member? (n = reinstate)")
                ? MemberStatus.SUSPENDED : MemberStatus.VALID;
        if (acmeInterface.updateMemberStatus(number, status)) {
            System.out.println("Member " + number + " is now " + status + ".");
        } else {
            System.out.println("No member has that number.");
        }
    }
}

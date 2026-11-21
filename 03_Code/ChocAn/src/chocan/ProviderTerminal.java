package chocan;

import java.io.IOException;
import java.time.LocalDate;

/**
 * The provider terminal. After Verify Provider succeeds, the provider can
 * validate a member (Validate Member Number), bill ChocAn for a service
 * (Bill For Service, with optional Enter Service Comments), and request
 * the provider directory. Swiping a member card is simulated by typing
 * the 9-digit member number.
 *
 * @author Gabriel Mckinney
 */
public class ProviderTerminal {
    private int currentProviderID;
    private boolean loggedIn;

    private final ConsoleInput in;
    private final VerificationController verificationController;
    private final ServiceController serviceController;

    public ProviderTerminal(ConsoleInput in, VerificationController verificationController,
                            ServiceController serviceController) {
        this.in = in;
        this.verificationController = verificationController;
        this.serviceController = serviceController;
    }

    /** Logs the provider in, then shows the provider menu until they log out. */
    public void start() {
        if (!login(promptProviderID())) {
            displayMessage("Invalid provider number. Access denied.");
            return;
        }
        displayMessage("Access granted.");
        while (loggedIn) {
            System.out.println();
            System.out.println("------------ Provider Terminal ------------");
            System.out.println("1. Validate member");
            System.out.println("2. Bill ChocAn for a service");
            System.out.println("3. Request provider directory");
            System.out.println("0. Log out");
            switch (in.readChoice("Choose an option: ", 0, 3)) {
                case 1: validateMember(); break;
                case 2: billService(); break;
                case 3: requestProviderDirectory(); break;
                default: loggedIn = false; break;
            }
        }
        displayMessage("Provider logged out.");
    }

    /** Verify Provider: asks the VerificationController whether the number is valid. */
    public boolean login(int providerNumber) {
        loggedIn = verificationController.verifyProvider(providerNumber) == ValidationResult.VALIDATED;
        if (loggedIn) {
            currentProviderID = providerNumber;
        }
        return loggedIn;
    }

    public int promptProviderID() {
        return in.readNumber("Enter your 9-digit provider number: ", 9);
    }

    /** Simulates swiping the member card. */
    public int readMemberID() {
        return in.readNumber("Member number (swipe card): ", 9);
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    /** Validate Member Number: displays Validated, Invalid number, or Member suspended. */
    public ValidationResult validateMember() {
        ValidationResult result = verificationController.verifyMember(readMemberID());
        displayMessage(result.getMessage());
        return result;
    }

    /** Bill For Service. */
    public void billService() {
        System.out.println("\n--- Bill ChocAn ---");
        int memberNumber = readMemberID();
        ValidationResult result = verificationController.verifyMember(memberNumber);
        displayMessage(result.getMessage());
        if (result != ValidationResult.VALIDATED) {
            return;
        }

        LocalDate serviceDate = promptServiceDate();

        Service service = null;
        while (service == null) {
            int code = promptServiceCode();
            Service found = serviceController.lookUpService(code);
            if (found == null) {
                displayMessage("Nonexistent code. Please try again.");
            } else if (confirmService(found.getName())) {
                service = found;
            }
        }

        String comments = promptComments();
        ServiceRecord record = serviceController.recordService(currentProviderID, memberNumber,
                service.getCode(), serviceDate, comments);
        displayMessage("Service record saved at " + record.getDateReceived().format(ServiceRecord.DATE_TIME) + ".");
        displayFee(service.getFee());
    }

    public LocalDate promptServiceDate() {
        return in.readPastDate("Date service was provided (MM-DD-YYYY): ");
    }

    public int promptServiceCode() {
        return in.readNumber("6-digit service code: ", 6);
    }

    public boolean confirmService(String name) {
        return in.confirm("Service: " + name + ". Is this correct?");
    }

    /** Enter Service Comments: optional; returns "" when the provider skips or cancels. */
    public String promptComments() {
        if (!in.confirm("Add comments?")) {
            return "";
        }
        String comments = in.readOptional("Comments (max " + ServiceRecord.COMMENTS_MAX
                + ", blank to cancel): ", ServiceRecord.COMMENTS_MAX);
        if (comments.isEmpty()) {
            displayMessage("Comments cancelled. Billing continues without comments.");
        }
        return comments;
    }

    public void displayFee(double fee) {
        displayMessage("Fee to be paid for this service: " + Service.formatMoney(fee));
    }

    /** Request Provider Directory: shows it and writes it to a file. */
    public void requestProviderDirectory() {
        try {
            ProviderDirectory directory = serviceController.createProviderDirectory();
            System.out.println();
            System.out.print(directory.getText());
            displayMessage("\nDirectory saved to " + directory.getFileName());
        } catch (IOException e) {
            displayMessage("Error: could not create the provider directory (" + e.getMessage() + ").");
        }
    }
}

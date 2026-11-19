package ChocAn.src.chocan;

import java.util.List;

/**
 * The operator terminal. After Verify Operator succeeds, the operator can
 * add, update, delete, and list members and providers (Manage Records).
 *
 * @author Aaron Suarez
 */
public class OperatorTerminal {
    private boolean loggedIn;

    private final ConsoleInput in;
    private final VerificationController verificationController;
    private final MemberController memberController;
    private final ProviderController providerController;

    public OperatorTerminal(ConsoleInput in, VerificationController verificationController,
                            MemberController memberController, ProviderController providerController) {
        this.in = in;
        this.verificationController = verificationController;
        this.memberController = memberController;
        this.providerController = providerController;
    }

    /** Logs the operator in, then shows the operator menu until they log out. */
    public void start() {
        int number = in.readNumber("Enter your 9-digit operator number: ", 9);
        if (!login(number)) {
            System.out.println("Invalid operator number. Access denied.");
            return;
        }
        System.out.println("Access granted.");
        while (loggedIn) {
            System.out.println();
            System.out.println("------------ Operator Terminal ------------");
            System.out.println("1. Add member");
            System.out.println("2. Update member");
            System.out.println("3. Delete member");
            System.out.println("4. List members");
            System.out.println("5. Add provider");
            System.out.println("6. Update provider");
            System.out.println("7. Delete provider");
            System.out.println("8. List providers");
            System.out.println("0. Log out");
            switch (in.readChoice("Choose an option: ", 0, 8)) {
                case 1: addMember(); break;
                case 2: updateMember(); break;
                case 3: deleteMember(); break;
                case 4: listMembers(); break;
                case 5: addProvider(); break;
                case 6: updateProvider(); break;
                case 7: deleteProvider(); break;
                case 8: listProviders(); break;
                default: loggedIn = false; break;
            }
        }
        System.out.println("Operator logged out.");
    }

    /** Verify Operator: asks the VerificationController whether the number is valid. */
    public boolean login(int operatorNumber) {
        loggedIn = verificationController.verifyOperator(operatorNumber) == ValidationResult.VALIDATED;
        return loggedIn;
    }

    // ------------------------------- Members -------------------------------

    public void addMember() {
        System.out.println("\n--- Add Member ---");
        String name = in.readName("Name (max 25): ", null);
        String street = in.readStreet("Street address (max 25): ", null);
        String city = in.readCity("City (max 14): ", null);
        String state = in.readState("State (2 letters): ", null);
        String zip = in.readZip("Zip code (5 digits): ", null);
        if (!in.confirm("Save this member?")) {
            System.out.println("Cancelled. Nothing was saved.");
            return;
        }
        Member m = memberController.addMember(name, street, city, state, zip);
        System.out.println("Member added. Member number: " + m.getID());
    }

    public void updateMember() {
        System.out.println("\n--- Update Member ---");
        Member m = memberController.findMember(in.readNumber("Member number: ", 9));
        if (m == null) {
            System.out.println("No member has that number.");
            return;
        }
        System.out.println("Current record: " + m);
        System.out.println("Press Enter to keep a current value.");
        String name = in.readName("Name", m.getName());
        String street = in.readStreet("Street address", m.getAddress());
        String city = in.readCity("City", m.getCity());
        String state = in.readState("State", m.getState());
        String zip = in.readZip("Zip code", m.getZipCode());
        if (!in.confirm("Save these changes?")) {
            System.out.println("Cancelled. Nothing was changed.");
            return;
        }
        memberController.updateMember(m.getID(), name, street, city, state, zip);
        System.out.println("Member " + m.getID() + " updated.");
    }

    public void deleteMember() {
        System.out.println("\n--- Delete Member ---");
        Member m = memberController.findMember(in.readNumber("Member number: ", 9));
        if (m == null) {
            System.out.println("No member has that number.");
        } else if (in.confirm("Delete " + m.getName() + " (" + m.getID() + ")?")) {
            memberController.deleteMember(m.getID());
            System.out.println("Member deleted.");
        } else {
            System.out.println("Cancelled. Nothing was deleted.");
        }
    }

    public void listMembers() {
        System.out.println("\n--- Members ---");
        List<Member> members = memberController.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members.");
        }
        for (Member m : members) {
            System.out.println(m);
        }
    }

    // ------------------------------ Providers ------------------------------

    public void addProvider() {
        System.out.println("\n--- Add Provider ---");
        String name = in.readName("Name (max 25): ", null);
        String street = in.readStreet("Street address (max 25): ", null);
        String city = in.readCity("City (max 14): ", null);
        String state = in.readState("State (2 letters): ", null);
        String zip = in.readZip("Zip code (5 digits): ", null);
        if (!in.confirm("Save this provider?")) {
            System.out.println("Cancelled. Nothing was saved.");
            return;
        }
        Provider p = providerController.addProvider(name, street, city, state, zip);
        System.out.println("Provider added. Provider number: " + p.getNumber());
    }

    public void updateProvider() {
        System.out.println("\n--- Update Provider ---");
        Provider p = providerController.findProvider(in.readNumber("Provider number: ", 9));
        if (p == null) {
            System.out.println("No provider has that number.");
            return;
        }
        System.out.println("Current record: " + p);
        System.out.println("Press Enter to keep a current value.");
        String name = in.readName("Name", p.getName());
        String street = in.readStreet("Street address", p.getAddress());
        String city = in.readCity("City", p.getCity());
        String state = in.readState("State", p.getState());
        String zip = in.readZip("Zip code", p.getZipCode());
        if (!in.confirm("Save these changes?")) {
            System.out.println("Cancelled. Nothing was changed.");
            return;
        }
        providerController.updateProvider(p.getNumber(), name, street, city, state, zip);
        System.out.println("Provider " + p.getNumber() + " updated.");
    }

    public void deleteProvider() {
        System.out.println("\n--- Delete Provider ---");
        Provider p = providerController.findProvider(in.readNumber("Provider number: ", 9));
        if (p == null) {
            System.out.println("No provider has that number.");
        } else if (in.confirm("Delete " + p.getName() + " (" + p.getNumber() + ")?")) {
            providerController.deleteProvider(p.getNumber());
            System.out.println("Provider deleted.");
        } else {
            System.out.println("Cancelled. Nothing was deleted.");
        }
    }

    public void listProviders() {
        System.out.println("\n--- Providers ---");
        List<Provider> providers = providerController.getAllProviders();
        if (providers.isEmpty()) {
            System.out.println("No providers.");
        }
        for (Provider p : providers) {
            System.out.println(p);
        }
    }
}

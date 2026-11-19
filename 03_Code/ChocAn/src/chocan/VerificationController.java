package chocan;

/**
 * Checks login numbers for operators, providers, and managers, and checks
 * a member's number and status before a provider bills for a service.
 * Used by the Verify Operator, Verify Provider, Verify Manager, and
 * Validate Member Number use cases.
 *
 * @author Jacob Wilson
 */
public class VerificationController {
    private final MemberRegistry memberRegistry;
    private final ProviderRegistry providerRegistry;
    private final OperatorRegistry operatorRegistry;
    private final ManagerRegistry managerRegistry;

    public VerificationController(MemberRegistry memberRegistry, ProviderRegistry providerRegistry,
                                  OperatorRegistry operatorRegistry, ManagerRegistry managerRegistry) {
        this.memberRegistry = memberRegistry;
        this.providerRegistry = providerRegistry;
        this.operatorRegistry = operatorRegistry;
        this.managerRegistry = managerRegistry;
    }

    public ValidationResult verifyManager(int managerNumber) {
        if (isValidNumber(managerNumber) && managerRegistry.findManager(managerNumber) != null) {
            return ValidationResult.VALIDATED;
        }
        return ValidationResult.INVALID_NUMBER;
    }

    public ValidationResult verifyOperator(int operatorNumber) {
        if (isValidNumber(operatorNumber) && operatorRegistry.findOperator(operatorNumber) != null) {
            return ValidationResult.VALIDATED;
        }
        return ValidationResult.INVALID_NUMBER;
    }

    public ValidationResult verifyProvider(int providerNumber) {
        if (isValidNumber(providerNumber) && providerRegistry.findProvider(providerNumber) != null) {
            return ValidationResult.VALIDATED;
        }
        return ValidationResult.INVALID_NUMBER;
    }

    /** VALIDATED, INVALID_NUMBER if no such member, or MEMBER_SUSPENDED. */
    public ValidationResult verifyMember(int memberNumber) {
        if (!isValidNumber(memberNumber)) {
            return ValidationResult.INVALID_NUMBER;
        }
        Member member = memberRegistry.findMember(memberNumber);
        if (member == null) {
            return ValidationResult.INVALID_NUMBER;
        }
        return member.isSuspended() ? ValidationResult.MEMBER_SUSPENDED : ValidationResult.VALIDATED;
    }

    /** True if the number has exactly 9 digits. */
    private boolean isValidNumber(int number) {
        return number >= 100000000 && number <= 999999999;
    }
}

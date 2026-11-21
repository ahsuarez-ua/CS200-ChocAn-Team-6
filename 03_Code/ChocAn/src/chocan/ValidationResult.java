package chocan;

/**
 * Result returned by the VerificationController when a number is checked.
 *
 * @author Gabriel Mckinney
 */
public enum ValidationResult {
    VALIDATED("Validated"),
    INVALID_NUMBER("Invalid number"),
    MEMBER_SUSPENDED("Member suspended");

    private final String message;

    ValidationResult(String message) {
        this.message = message;
    }

    /** The text shown on the terminal for this result. */
    public String getMessage() {
        return message;
    }
}

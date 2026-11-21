package chocan;

/**
 * One electronic funds transfer (EFT) entry: the provider name, provider
 * number, and the amount to transfer for the week.
 *
 * @author Gabriel Mckinney
 */
public class PaymentRecord {
    private final String providerName;
    private final int providerNumber;
    private final double amount;

    public PaymentRecord(String providerName, int providerNumber, double amount) {
        this.providerName = providerName;
        this.providerNumber = providerNumber;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String format() {
        return String.format("%-25s  %-9d  %12s", providerName, providerNumber, Service.formatMoney(amount));
    }
}

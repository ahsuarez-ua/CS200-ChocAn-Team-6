package chocan;

/**
 * One service in the provider directory: a 6-digit service code,
 * a name of at most 20 characters, and a fee of at most $999.99.
 *
 * @author Gabriel Mckinney
 */
public class Service {
    public static final int NAME_MAX = 20;

    private final int serviceCode;
    private final String serviceName;
    private final double fee;

    public Service(int serviceCode, String serviceName, double fee) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.fee = fee;
    }

    public int getCode() { return serviceCode; }
    public String getName() { return serviceName; }
    public double getFee() { return fee; }

    /** Formats an amount as dollars, for example 75.0 -> "$75.00". */
    public static String formatMoney(double amount) {
        return String.format("$%,.2f", amount);
    }
}

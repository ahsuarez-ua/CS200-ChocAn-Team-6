package chocan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The record written to disk each time a provider bills ChocAn: the date
 * and time it was received by the computer, the date the service was
 * provided, the provider, member, service code, and optional comments.
 *
 * @author Gabriel Mckinney
 */
public class ServiceRecord {
    public static final int COMMENTS_MAX = 100;
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    private final LocalDateTime currentDateTime;
    private final LocalDate serviceDate;
    private final int providerNumber;
    private final int memberNumber;
    private final int serviceCode;
    private final String comments;

    public ServiceRecord(LocalDateTime currentDateTime, LocalDate serviceDate,
                         int providerNumber, int memberNumber, int serviceCode,
                         String comments) {
        this.currentDateTime = currentDateTime;
        this.serviceDate = serviceDate;
        this.providerNumber = providerNumber;
        this.memberNumber = memberNumber;
        this.serviceCode = serviceCode;
        this.comments = comments == null ? "" : comments;
    }

    public LocalDate getServiceDate() { return serviceDate; }
    public LocalDateTime getDateReceived() { return currentDateTime; }
    public int getProviderNumber() { return providerNumber; }
    public int getMemberNumber() { return memberNumber; }
    public int getServiceCode() { return serviceCode; }
    public String getComments() { return comments; }
}

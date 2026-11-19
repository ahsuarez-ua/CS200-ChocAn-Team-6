package ChocAn.src.chocan;

/**
 * A ChocAn member who can receive services from providers.
 * Member numbers are 9 digits.
 * 
 * @author Aaron Suarez
 */
public class Member {
    private int memberID;
    private String memberName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private MemberStatus status;

    public Member(int ID, String name, String address, String city, String state, String zipCode, MemberStatus status) {
        this.memberID = ID;
        this.memberName = name;
        this.streetAddress = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.status = status;
    }

    public String getName() {
        return memberName;
    }

    public int getID() {
        return memberID;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public MemberStatus getStatus() {
        return status;
    }

   
    public void suspend() {
        this.status = MemberStatus.SUSPENDED;
    }

    public void reinstate() {
        this.status = MemberStatus.VALID;
    }

    /** Replaces the member's name and full address at once. */
    public void updateInfo(String name, String address, String city, String state, String zip) {
        this.memberName = name;
        this.streetAddress = address;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
    }

    public void setName(String name) {
        this.memberName = name;
    }

    public void setNumber(int number) {
        this.memberID = number;
    }

    public void setAddress(String street) {
        this.streetAddress = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zip) {
        this.zipCode = zip;
    }

    @Override 
    public String toString() {
        return String.format("%09d  %-25s  %-25s %s, %s %s  [%s]", memberID, memberName, streetAddress, city, state, zipCode, status);
    }
}

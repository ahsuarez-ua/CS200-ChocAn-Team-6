/**
 * The member class is the base entity class for each member in that uses ChocAn's services
 * 
 * @author Aaron Suarez
 */
public class Member {
    private int memberID;
    private String memberName;
    private String streetAddress;
    private String city;
    private String state;
    private int zipCode;
    private MemberStatus status;

    public Member(int ID, String name, String address, String city, String state, int zipCode, MemberStatus status) {
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

    public int getNumber() {
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

    public int getZipCode() {
        return zipCode;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public String getAddress() {
        return streetAddress + ", " + city + ", " + state + ", " + zipCode;
    }

    public void suspend() {
        this.status = MemberStatus.SUSPENDED;
    }

    public void reinstate() {
        this.status = MemberStatus.VALID;
    }

    // Replaces all of the member's personal information at once
    public void updateInfo(String name, String address, String city, String state, int zip) {
        setName(name);
        setAddress(address);
        setCity(city);
        setState(state);
        setZipCode(zip);
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

    public void setZipCode(int zip) {
        this.zipCode = zip;
    }

    @Override 
    public String toString() {
        return String.format("%09d  %-25s  %s  [%s]", memberID, memberID, getAddress(), status);
    }
}

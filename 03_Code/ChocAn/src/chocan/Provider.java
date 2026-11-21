package chocan;

/**
 * A health care provider (dietitian, internist, exercise expert, ...)
 * who provides services to members and is paid by ChocAn.
 * Provider numbers are 9 digits.
 * 
 * @author Aaron Suarez
 */

public class Provider {
    private int providerID;
    private String providerName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;

    public Provider(int number, String name, String address, String city, String state, String zipCode) {
        this.providerID = number;
        this.providerName = name;
        this.streetAddress = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public int getNumber() {
        return providerID;
    }

    public String getName() {
        return providerName;
    }

    public String getAddress() {
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
    
    public void setNumber(int number) {
        this.providerID = number;
    }

    public void setName(String name) {
        this.providerName = name;
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
        return String.format("%09d  %-25s  %-25s  %s, %s %s", providerID, providerName, streetAddress, city, state, zipCode);
    }
}

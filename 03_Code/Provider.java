/**
 * The Provider Class is the base entity class for all healthcare providers that ChocAn uses. 
 * Providers bill members through the provider terminal
 * 
 * @author Aaron Suarez
 */

public class Provider {
    private int providerID;
    private String providerName;
    private String streetAddress;
    private String city;
    private String state;
    private int zipCode;

    public Provider(int number, String name, String address, String city, String state, int zipCode) {
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

    public String getSteetAddress() {
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

    /** @return the full one-line mailing address */
    public String getAddress() {
        return streetAddress + ", " + city + ", " + state + ", " + zipCode;
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

    public void setZipCode(int zip) {
        this.zipCode = zip;
    }

    @Override 
    public String toString() {
        return String.format("%09d  %-25s  %s  [%s]", providerID, providerName, getAddress(), zipCode);
    }
 
}

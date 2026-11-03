public class Member {
    private int number;
    private String name;
    private String address;
    private String city;
    private String state;
    private int zipCode;

    public Member(int number, String name, String address, String city, String state, int zipCode) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAddress(String street) {
        this.address = street;
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

    public String getAddress() {
        return this.address + ", " + this.city + ", " + this.state + ", " + this.zipCode;
    }
    
}

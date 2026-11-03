public class Provider {
    private int number;
    private String name;
    private String address;
    private String city;
    private String state;
    private int zipCode;

    public Provider(int number, String name, String address, String city, String state, int zipCode) {
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


}

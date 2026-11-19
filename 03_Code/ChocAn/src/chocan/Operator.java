package chocan;

/**
 * A ChocAn operator, who maintains member and provider records.
 *
 * @author Jacob Wilson
 */
public class Operator {
    private String name;
    private int number;

    public Operator(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() { return name; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
}

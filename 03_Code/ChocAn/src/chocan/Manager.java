package chocan;

/**
 * A ChocAn manager, who can request reports at any time.
 *
 * @author Jacob Wilson
 */
public class Manager {
    private String name;
    private int number;

    public Manager(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() { return name; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public void setName(String name) { this.name = name; }
}

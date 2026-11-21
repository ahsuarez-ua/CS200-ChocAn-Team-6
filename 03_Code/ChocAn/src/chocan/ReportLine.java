package chocan;

/**
 * One line in the body of a report. Report lines exist only inside their
 * report (composition in the class diagram).
 *
 * @author Peyton Hardy
 */
public class ReportLine {
    private final String text;

    public ReportLine(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

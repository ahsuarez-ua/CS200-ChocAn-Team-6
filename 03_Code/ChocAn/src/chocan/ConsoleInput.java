package chocan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Keyboard input shared by every terminal. Each method keeps asking until
 * the user types something valid, so the terminals never receive bad data.
 * Also holds the field rules from the requirements (name 25 characters,
 * street 25, city 14, state 2 letters, zip 5 digits).
 *
 * @author Jacob Wilson
 */
public class ConsoleInput {
    public static final int NAME_MAX = 25;
    public static final int STREET_MAX = 25;
    public static final int CITY_MAX = 14;

    /** Thrown when keyboard input ends, so the program can exit cleanly. */
    public static class InputClosedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    // "uuuu" with STRICT rejects impossible dates such as 02-30-2026
    private static final DateTimeFormatter STRICT_DATE =
            DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Prints a prompt and returns the trimmed line typed. */
    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println();
            throw new InputClosedException();
        }
        return scanner.nextLine().trim();
    }

    /** Reads a menu choice between min and max. */
    public int readChoice(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

    /** Reads a number with exactly this many digits, such as a 9-digit member number. */
    public int readNumber(String prompt, int digits) {
        while (true) {
            String line = readLine(prompt);
            if (line.matches("\\d{" + digits + "}")) {
                return Integer.parseInt(line);
            }
            System.out.println("Please enter exactly " + digits + " digits.");
        }
    }

    /**
     * Reads a value checked by a rule. The rule returns null if the value is
     * fine, or an error message to show. If keep is not null, pressing Enter
     * keeps that current value (used when editing a record).
     */
    public String readValid(String prompt, String keep, Function<String, String> rule) {
        while (true) {
            String value = readLine(keep == null ? prompt : prompt + " [" + keep + "]: ");
            if (keep != null && value.isEmpty()) {
                return keep;
            }
            String error = rule.apply(value);
            if (error == null) {
                return value;
            }
            System.out.println(error);
        }
    }

    public String readName(String prompt, String keep) {
        return readValid(prompt, keep, v -> textRule(v, "Name", NAME_MAX));
    }

    public String readStreet(String prompt, String keep) {
        return readValid(prompt, keep, v -> textRule(v, "Street address", STREET_MAX));
    }

    public String readCity(String prompt, String keep) {
        return readValid(prompt, keep, v -> textRule(v, "City", CITY_MAX));
    }

    public String readState(String prompt, String keep) {
        return readValid(prompt, keep, v -> v.matches("[A-Za-z]{2}")
                ? null : "State must be a 2-letter abbreviation, like AL.").toUpperCase();
    }

    public String readZip(String prompt, String keep) {
        return readValid(prompt, keep, v -> v.matches("\\d{5}")
                ? null : "Zip code must be exactly 5 digits.");
    }

    /** Optional text of at most max characters (empty is allowed). */
    public String readOptional(String prompt, int max) {
        return readValid(prompt, null, v -> v.length() > max
                ? "Must be at most " + max + " characters."
                : v.contains("|") ? "Cannot contain the '|' character." : null);
    }

    /** Reads a date as MM-DD-YYYY that is not in the future. */
    public LocalDate readPastDate(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                LocalDate date = LocalDate.parse(line, STRICT_DATE);
                if (!date.isAfter(LocalDate.now())) {
                    return date;
                }
                System.out.println("The date cannot be in the future.");
            } catch (DateTimeParseException e) {
                System.out.println("Please enter a valid date as MM-DD-YYYY.");
            }
        }
    }

    /** Reads a week-ending date; pressing Enter means today. */
    public LocalDate readWeekEnding() {
        while (true) {
            String line = readLine("Week-ending date (MM-DD-YYYY, Enter for today): ");
            if (line.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(line, STRICT_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Please enter a valid date as MM-DD-YYYY.");
            }
        }
    }

    /** Asks a yes/no question. */
    public boolean confirm(String prompt) {
        while (true) {
            String line = readLine(prompt + " (y/n): ").toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }

    /** Waits for Enter so output can be read before the menu returns. */
    public void pause() {
        readLine("\nPress Enter to continue...");
    }

    private static String textRule(String value, String field, int max) {
        if (value.isEmpty()) {
            return field + " cannot be empty.";
        }
        if (value.length() > max) {
            return field + " must be at most " + max + " characters.";
        }
        if (value.contains("|")) {
            return field + " cannot contain the '|' character.";
        }
        return null;
    }
}

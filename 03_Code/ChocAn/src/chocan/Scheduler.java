package chocan;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Starts the main accounting procedure. In the real system a timer calls
 * triggerMainAccountingProcedure() at midnight every Friday; here it can
 * also be started from the main menu so the procedure can be demonstrated.
 *
 * @author Peyton Hardy
 */
public class Scheduler {
    private String scheduledTime = "00:00 FRIDAY";
    private int runHour = 0;
    private DayOfWeek runDay = DayOfWeek.FRIDAY;

    private final AccountingController accountingController;

    public Scheduler(AccountingController accountingController) {
        this.accountingController = accountingController;
    }

    /** Runs the main accounting procedure for the week ending today. */
    public List<Path> triggerMainAccountingProcedure() throws IOException {
        accountingController.setWeekEnding(LocalDateTime.now().toLocalDate());
        return accountingController.runMainAccountingProcedure();
    }

    /** Sets the hour (0-23) the procedure runs. */
    public boolean setRunTime(int runTime) {
        if (runTime < 0 || runTime > 23) {
            return false;
        }
        runHour = runTime;
        scheduledTime = String.format("%02d:00 %s", runHour, runDay);
        return true;
    }

    /** Sets the day the procedure runs, for example "FRIDAY". */
    public boolean setRunDay(String runDate) {
        try {
            runDay = DayOfWeek.valueOf(runDate.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }
        scheduledTime = String.format("%02d:00 %s", runHour, runDay);
        return true;
    }

    /** True when the given time is the scheduled day and hour. */
    public boolean isTimeToRun(LocalDateTime now) {
        return now.getDayOfWeek() == runDay && now.getHour() == runHour;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }
}

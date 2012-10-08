import java.util.Calendar;
import java.util.Date;

/**
 * This class stores an event that occurs every month on the basis of the day of the week
 * (e.g. 10 - 11 am on the 2nd Thursday of every month)
 * 
 * @author Nicholas Dyszel
 *
 */
public class MonthlyDayRecurringEvent extends RecurringEvent {
    private int dayOfWeek;      // represents day of the week (Calendar.MONDAY, Calendar.TUESDAY, etc.)
    private int weekOfMonth;    // represents week of the month (Calendar.DAY_OF_WEEK_IN_MONTH)
    // e.g. dayOfWeek = Calendar.THURSDAY, weekOfMonth = 2 represents the 2nd Thursday of the month
    
    public MonthlyDayRecurringEvent(String name, String location, User[] attendees, User creator, 
            int startHour, int startMinute, int endHour, int endMinute, int dayOfWeek, int weekOfMonth) {
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.dayOfWeek = dayOfWeek;
        this.weekOfMonth = weekOfMonth;
    }
    
    @Override
    public Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        startOfEvent.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.MONTH, 1);
        }
        return startOfEvent;
    }
    
    @Override
    public void nextEvent(Calendar time) {
        time.add(Calendar.MONTH, 1);
        time.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        time.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
    }

}

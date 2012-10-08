import java.util.Calendar;
import java.util.Date;

/**
 * Stores an event that occurs every year
 * (e.g. every January 3rd from 3-5 pm)
 * 
 * @author Nicholas Dyszel
 */
public class YearlyRecurringEvent extends RecurringEvent {
    private int month;  // represents month (Calendar.JANUARY, Calendar.FEBRUARY, etc.)
    private int date;   // represents date
    
    public YearlyRecurringEvent(String name, String location, User[] attendees, User creator, 
            int startHour, int startMinute, int endHour, int endMinute, int month, int date) {
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.month = month;
        this.date = date;
    }
    
    @Override
    public Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.MONTH, month);
        startOfEvent.set(Calendar.DATE, date);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.YEAR, 1);
        }
        return startOfEvent;
    }
    
    @Override
    public void nextEvent(Calendar time) {
        time.add(Calendar.YEAR, 1);
    }
}

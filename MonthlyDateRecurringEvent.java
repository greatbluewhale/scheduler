import java.util.Calendar;
import java.util.Date;

/**
 * This class stores an event that occurs every month
 * (e.g. the 15th of every month from 10 - 11 am)
 * 
 * @author Nicholas Dyszel
 *
 */
public class MonthlyDateRecurringEvent extends RecurringEvent {
    private int date;   // represents the date
    
    public MonthlyDateRecurringEvent(String name, String location, User[] attendees, User creator, 
            int startHour, int startMinute, int endHour, int endMinute, int date) {
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.date = date;
    }
    
    @Override
    public Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.DATE, date);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.MONTH, 1);
        }
        return startOfEvent;
    }
    
    @Override
    public void nextEvent(Calendar time) {
        time.add(Calendar.MONTH, 1);
    }
}

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents an event that occurs every week 
 * (e.g. Tuesdays from 10:00am to 11:15am).
 * 
 * @author Amuthan
 *
 */
public class WeeklyRecurringEvent extends RecurringEvent {
    private int dayOfWeek;  // represents day of the week 
                            // (Calendar.SUNDAY, Calendar.MONDAY, etc.)
    
    public WeeklyRecurringEvent(String name, String location, User[] attendees, User creator, 
                               int startHour, int startMinute, int endHour, int endMinute, int dayOfWeek){
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.dayOfWeek = dayOfWeek;
    }
    
    @Override
    public Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return startOfEvent;
    }
    
    @Override
    public void nextEvent(Calendar time){
        time.add(Calendar.WEEK_OF_YEAR, 1);
    }
}

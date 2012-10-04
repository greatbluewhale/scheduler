import java.util.Calendar;

/**
 * This class represents an event that occurs every day 
 * (e.g. daily from 10:00am to 11:15am).
 * 
 * @author Amuthan
 *
 */
public class DailyRecurringEvent extends RecurringEvent {
    
    public DailyRecurringEvent(String name, String location, User[] attendees, User creator, 
                               int startHour, int startMinute, int endHour, int endMinute){
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
    }
    
    @Override
    public void nextEvent(Calendar time){
        time.add(Calendar.DATE, 1);
    }
}
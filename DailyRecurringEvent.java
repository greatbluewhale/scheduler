/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */
import java.util.Calendar;

/**
 * This class represents an event that occurs every day 
 * (e.g. daily from 10:00am to 11:15am).
 * 
 * @author Amuthan
 *
 */
public class DailyRecurringEvent extends RecurringEvent {
    
    /**
     * Init constructor
     * @param name          name of the event
     * @param location      location of the event
     * @param attendees     users attending event
     * @param creator       creator of the event
     * @param startHour     hour the event starts
     * @param startMinute   minute the event starts
     * @param endHour       hour the event ends
     * @param endMinute     minute the event ends
     * @throws Exception    if the arguments do not yield a valid time block
     */
    public DailyRecurringEvent(String name, String location, User[] attendees, User creator, 
                               int startHour, int startMinute, int endHour, int endMinute, Calendar startInterval, Calendar endInterval) throws Exception{
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute, startInterval, endInterval);
    }
    
    /**
     * This method increments the given Calendar object by 1 day.
     */
    @Override
    protected void nextEvent(Calendar time){
        time.add(Calendar.DATE, 1);
    }
}
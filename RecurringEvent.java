import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This abstract class represents an event that occurs 
 * repeatedly (daily, weekly, monthly, etc.).
 * 
 * @author Amuthan
 *
 */
public abstract class RecurringEvent extends Event{
    protected int startHour;      // hour of the start time (0-23)
    protected int startMinute;    // minute of the start time (0-59)
    protected int endHour;        // hour of the end time (0-23)
    protected int endMinute;      // minute of the end time (0-59)
    
    public RecurringEvent(String name, String location, User[] attendees, User creator, 
                          int startHour, int startMinute, int endHour, int endMinute){
        super(name, location, attendees, creator);
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }
    
    /**
     * This method returns the start time of the first event after 
     * the given Date. 
     * 
     * Example:
     * Suppose the given date is Monday, Oct. 1 at 12:01am. 
     * A daily event from 10am-11am would return 'Oct. 1 at 10am', 
     * while a weekly event on Thursdays from 10am-11am would return 
     * 'Oct. 4 at 10am'.
     * 
     * @param start     the start of the time interval
     * @return          the time of the first event after 'start'
     */
    public Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = new GregorianCalendar();
        startOfEvent.setTime(start);
        startOfEvent.set(Calendar.HOUR_OF_DAY, startHour);
        startOfEvent.set(Calendar.MINUTE, startMinute);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.DATE, 1);
        }
        return startOfEvent;
    }
    
    /**
     * This method increments the given Calendar object by a certain 
     * amount. Daily events will add 1 day, weekly events 
     * will add 1 week, etc.
     * 
     * @param time  the Calendar object to be incremented (it will be modified)
     */
    public abstract void nextEvent(Calendar time);
    
    @Override
    public ArrayList<OneTimeEvent> getEvents(Date start, Date end){
        ArrayList<OneTimeEvent> list = new ArrayList<OneTimeEvent>();    // list of one-time events
        Calendar startOfEvent = getStartOfFirstEvent(start);             // the start of each one-time event
        Calendar endOfEvent = new GregorianCalendar();                   // the end of each one-time event
        
        // Find the end of the first event
        endOfEvent.setTime(startOfEvent.getTime());
        endOfEvent.set(Calendar.HOUR_OF_DAY, endHour);
        endOfEvent.set(Calendar.MINUTE, endMinute);
        
        // For each event time in the interval, add a one-time event
        while (end.after(startOfEvent.getTime())){
            list.add(new OneTimeEvent(name, location, attendees, creator, startOfEvent.getTime(), endOfEvent.getTime()));
            nextEvent(startOfEvent);
            nextEvent(endOfEvent);
        }
        return list;
    }
}
/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

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
public abstract class RecurringEvent extends Event {
    protected TimeBlock time;
    protected Calendar startInterval;
    protected Calendar endInterval;
        
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
    public RecurringEvent(String name, String location, User[] attendees, User creator, 
                          int startHour, int startMinute, int endHour, int endMinute, Calendar startInterval, Calendar endInterval) throws Exception {
        super(name, location, attendees, creator);
        time = new TimeBlock(startHour, startMinute, endHour, endMinute);
        this.startInterval = new GregorianCalendar();
        this.endInterval = new GregorianCalendar();
        this.startInterval.setTime(startInterval.getTime());
        this.endInterval.setTime(endInterval.getTime());
    }
    
    public Calendar getIntervalStart() {
        return startInterval;
    }
    
    public Calendar getIntervalEnd() {
        return endInterval;
    }
    
    public TimeBlock getTimes() {
        return time;
    }
    
    /**
     * This method returns the start time of the first event after 
     * the given Date. This method is meant to be overridden, but 
     * the implementation in RecurringEvent will assume that the 
     * event is a daily event (since this implementation is useful 
     * for all of the subclasses of RecurringEvent).
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
    protected Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = new GregorianCalendar();
        startOfEvent.setTime(start);
        startOfEvent.set(Calendar.HOUR_OF_DAY, time.getStartHour());
        startOfEvent.set(Calendar.MINUTE, time.getStartMinute());
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
    protected abstract void nextEvent(Calendar time);
    
    /**
     * This gets all events within a certain time frame.
     * Example: If this is an event that occurs every Monday at 9:00, 
     *          and if the time frame is from Mon. Oct. 1 to Wed. Oct. 31, 
     *          then this function would return events at 9:00 on 
     *          Oct. 1, Oct. 8. Oct. 15, Oct. 22, and Oct. 29.
     * 
     * @return  an ArrayList of OneTimeEvent objects representing
     *          the events in the given time interval
     */
    @Override
    public ArrayList<OneTimeEvent> getEvents(Date start, Date end){
        ArrayList<OneTimeEvent> list = new ArrayList<OneTimeEvent>();    // list of one-time events
        
        Calendar startCal = new GregorianCalendar();
        startCal.setTime(start);
        Date actualStartOfInterval = (startInterval.after(startCal) ? startInterval : startCal).getTime();
        
        Calendar startOfEvent = getStartOfFirstEvent(actualStartOfInterval);    // the start of each one-time event
        Calendar endOfEvent = new GregorianCalendar();                          // the end of each one-time event
        
        // Find the end of the first event
        endOfEvent.setTime(startOfEvent.getTime());
        endOfEvent.set(Calendar.HOUR_OF_DAY, time.getEndHour());
        endOfEvent.set(Calendar.MINUTE, time.getEndMinute());
        
        // For each event time in the interval, add a one-time event
        while (endInterval.after(startOfEvent) && end.after(startOfEvent.getTime())){
            list.add(new OneTimeEvent(name, location, attendees, creator, startOfEvent.getTime(), endOfEvent.getTime(), this));
            nextEvent(startOfEvent);
            nextEvent(endOfEvent);
        }
        
        return list;
    }
    
    public Calendar getStartEventCalendar(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(this.startInterval.getTime());
        return cal;
    }
    
    public Calendar getEndEventCalendar(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(this.endInterval.getTime());
        return cal;
    }
}
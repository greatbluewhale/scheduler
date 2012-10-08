import java.util.Calendar;
import java.util.Date;

/**
 * Stores an event that occurs every year
 * (e.g. every January 3rd from 3-5 pm)
 * 
 * @author Nicholas Dyszel
 * @version 1.0, 7 Oct 2012
 */
public class YearlyRecurringEvent extends RecurringEvent {
    private int month;  // represents month (Calendar.JANUARY, Calendar.FEBRUARY, etc.)
    private int date;   // represents date
    
    /**
     * Init constructor
     * @param name          name of event
     * @param location      location of event
     * @param attendees     users attending event
     * @param creator       user who created event
     * @param startHour     hour of start time
     * @param startMinute   minute of start time
     * @param endHour       hour of end time
     * @param endMinute     minute of end time
     * @param month         month the event recurs in
     * @param date          date event recurs on
     */
    public YearlyRecurringEvent(String name, String location, User[] attendees, User creator, 
            int startHour, int startMinute, int endHour, int endMinute, int month, int date) {
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.month = month;
        this.date = date;
    }
    
    /**
     * Gets first instance of event after start date
     * @return first occurrence of event
     */
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
    
    /**
     * Sets time to the next event
     */
    @Override
    public void nextEvent(Calendar time) {
        time.add(Calendar.YEAR, 1);
    }
}

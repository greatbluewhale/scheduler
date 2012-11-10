/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

import java.util.Calendar;
import java.util.Date;

/**
 * This class stores an event that occurs every month on the basis of the day of the week
 * (e.g. 10 - 11 am on the 2nd Thursday of every month)
 * 
 * @author Nicholas Dyszel
 * @version 1.0, 7 Oct 2012
 */
public class MonthlyDayRecurringEvent extends RecurringEvent {
    private int dayOfWeek;      // represents day of the week (Calendar.MONDAY, Calendar.TUESDAY, etc.)
    private int weekOfMonth;    // represents week of the month (Calendar.DAY_OF_WEEK_IN_MONTH)
    // e.g. dayOfWeek = Calendar.THURSDAY, weekOfMonth = 2 represents the 2nd Thursday of the month
    
    /**
     * Init constructor
     * @param name          name of the event
     * @param location      location of the event
     * @param attendees     users attending event
     * @param creator       user who created event
     * @param startHour     starting hour of event
     * @param startMinute   starting minute of event
     * @param endHour       ending hour of event
     * @param endMinute     ending minute of event
     * @param dayOfWeek     day of week event recurs on
     * @param weekOfMonth   week of the month the event recurs on
     * @throws Exception    if the arguments do not yield a valid time block
     */
    public MonthlyDayRecurringEvent(String name, String location, User[] attendees, User creator, 
            int startHour, int startMinute, int endHour, int endMinute, int dayOfWeek, int weekOfMonth) throws Exception {
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.dayOfWeek = dayOfWeek;
        this.weekOfMonth = weekOfMonth;
    }
    
    /**
     * Gets the first instance of the event after the start date
     * @return first event
     */
    @Override
    protected Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        startOfEvent.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
        if (start.after(startOfEvent.getTime())){
            nextEvent(startOfEvent);
        }
        return startOfEvent;
    }
    
    /**
     * Sets time to the next event
     */
    @Override
    protected void nextEvent(Calendar time) {
        time.add(Calendar.MONTH, 1);
        time.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        time.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
    }

}

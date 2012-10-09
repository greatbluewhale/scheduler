/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

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
     * @param dayOfWeek     day of the week the event recurs on
     * @throws Exception    if the arguments do not yield a valid time block
     */
    public WeeklyRecurringEvent(String name, String location, User[] attendees, User creator, 
                               int startHour, int startMinute, int endHour, int endMinute, int dayOfWeek) throws Exception{
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute);
        this.dayOfWeek = dayOfWeek;
    }
    
    /**
     * This method gets the start of the first occurrence of 
     * this event after the given date.
     */
    @Override
    protected Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return startOfEvent;
    }
    
    /**
     * This method increments the given Calendar object by one week.
     */
    @Override
    protected void nextEvent(Calendar time){
        time.add(Calendar.WEEK_OF_YEAR, 1);
    }
}

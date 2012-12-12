/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

import java.util.Calendar;
import java.util.Date;

/**
 * This class stores an event that occurs every month
 * (e.g. the 15th of every month from 10 - 11 am)
 * 
 * @author Nicholas Dyszel
 * @version 1.0, 7 Oct 2012
 */
public class MonthlyDateRecurringEvent extends RecurringEvent {
    private int date;   // represents the date
    
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
     * @param date          date of the month the event recurs on
     * @throws Exception    if the arguments do not yield a valid time block
     */
    public MonthlyDateRecurringEvent(String name, String location, User[] attendees, User creator, 
            int startHour, int startMinute, int endHour, int endMinute, int date, Calendar startInterval, Calendar endInterval) throws Exception {
        super(name, location, attendees, creator, startHour, startMinute, endHour, endMinute, startInterval, endInterval);
        this.date = date;
    }
    
    /**
     * Gets the first occurrence of the event after the start date
     * @return the first event after start date
     */
    @Override
    protected Calendar getStartOfFirstEvent(Date start) {
        Calendar startOfEvent = super.getStartOfFirstEvent(start);
        startOfEvent.set(Calendar.DATE, date);
        if (start.after(startOfEvent.getTime())){
            startOfEvent.add(Calendar.MONTH, 1);
        }
        return startOfEvent;
    }
    
    /**
     * Sets time to that of the next event
     */
    @Override
    protected void nextEvent(Calendar time) {
        time.add(Calendar.MONTH, 1);
    }

    @Override
    public int getRecurrence() {
        return 3;
    }
}

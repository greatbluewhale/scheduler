/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * User class contains data for user account and functions to get user's events
 * 
 * @author Nicholas Dyszel
 * @version 1.0, 6 Oct 2012
 */
public class User {
    private static final int DEFAULT_START_AVAILABILITY = 8;    // 8:00 is the default start availability
    private static final int DEFAULT_END_AVAILABILITY = 18;     // 18:00 is the default end availability
    
    private String name;                            // account username
    private String password;                        // account password
    private ArrayList<Event> events;                // user's events
    private TimeBlock availability;   // user's weekly availability
        // This is used for users to control what times they are available during the day.
        // For example, when sleeping at night, the user should be unavailable although the
        // user does not have a scheduled event at the time.
    
    /**
     * Init constructor with default availability
     * @param name      account name
     * @param password  account password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        events = new ArrayList<Event>();
        try {
            availability = new TimeBlock(DEFAULT_START_AVAILABILITY, 0, DEFAULT_END_AVAILABILITY, 0);
        } catch (Exception e) {
            // Don't do anything, because the TimeBlock being created is definitely valid
        }
        
    }
    
    /**
     * Init constructor with custom availability
     * @param name          account name
     * @param password      account password
     * @param availability  initial availability
     */
    public User(String name, String password, TimeBlock availability) {
        this.name = name;
        this.password = password;
        events = new ArrayList<Event>();
        this.availability = availability;
    }
    
    /**
     * Adds an event to the user's event list
     * @param newEvent  event to add
     */
    public void addEvent(Event newEvent) {
        events.add(newEvent);
    }
    
    /**
     * Deletes an event from the user's event list
     * @param deleteEvent   event to remove
     */
    public void deleteEvent(Event deleteEvent) {
        events.remove(deleteEvent);
    }
    
    public ArrayList<OneTimeEvent> getEvents(Date start, Date end){
        ArrayList<OneTimeEvent> result = new ArrayList<OneTimeEvent>();
        Iterator<Event> userEventsIt = events.iterator();
        while (userEventsIt.hasNext()){
            Iterator<OneTimeEvent> oneTimeEventIt = userEventsIt.next().getEvents(start, end).iterator();
            while (oneTimeEventIt.hasNext()){
                result.add(oneTimeEventIt.next());
            }
        }
        return result;
    }
    
    /**
     * Gets a user's availability on a given day, including scheduled events
     * @param day   the given day
     * @return      a strictly sorted list of times the user is available
     */
    public ArrayList<TimeBlock> getAvailability(Calendar day) {
        ArrayList<TimeBlock> netAvailability = new ArrayList<TimeBlock>();
        ArrayList<OneTimeEvent> oneEvent;
        ArrayList<TimeBlock> busyTimes;
        int year = day.get(Calendar.YEAR);
        int month = day.get(Calendar.MONTH);
        int date = day.get(Calendar.DATE);
        Calendar begin = new GregorianCalendar(year, month, date, 0, 0);
        Calendar end = new GregorianCalendar(year, month, date, TimeBlock.MAX_HOUR, TimeBlock.MAX_MINUTE);
        Calendar eventStart = new GregorianCalendar();
        Calendar eventEnd = new GregorianCalendar();
        ListIterator<Event> it = events.listIterator();
        
        netAvailability.add(availability);
        
        try {
            while (it.hasNext()) {
                oneEvent = it.next().getEvents(begin.getTime(), end.getTime());
                busyTimes = new ArrayList<TimeBlock>();
                for (OneTimeEvent event : oneEvent) {
                    eventStart.setTime(event.getStartDate());
                    eventEnd.setTime(event.getEndDate());
                    busyTimes.add(new TimeBlock(eventStart.get(Calendar.HOUR_OF_DAY), eventStart.get(Calendar.MINUTE),
                                                eventEnd.get(Calendar.HOUR_OF_DAY), eventEnd.get(Calendar.MINUTE)));
                }
                netAvailability = TimeBlock.difference(netAvailability, busyTimes);
            }
        } catch (Exception e){
            // This Exception should never be thrown, so if it is thrown, print a 
            // message to the console.
            System.out.println(e.getMessage());
        }
        
        
        return netAvailability;
    }
    
    /**
     * Getter for name
     * @return  name of the user
     */
    public String getName(){
        return name;
    }
    
    /**
     * Getter for password
     * @return  user's password
     */
    public String getPassword(){
        return password;
    }
}

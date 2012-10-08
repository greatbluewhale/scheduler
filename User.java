import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * User class contains data for user account and functions to get user's events
 * 
 * @author Nicholas Dyszel
 * @version 1.0, 6 Oct 2012
 */
public class User {
    private String name;                            // account username
    private String password;                        // account password
    private ArrayList<Event> events;                // user's events
    private TimeBlock[] availability = new TimeBlock[7];   // user's weekly availability
        // This is used for user's to control what times they are available during the day.
        // For example, when sleeping at night, the user should be unavailable although the
        // user does not have a scheduled event at the time.
        // This 2D array stores an array of 7 
    
    /**
     * Init constructor with default availability
     * @param name      account name
     * @param password  account password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        events = new ArrayList<Event>();
        for (TimeBlock timeBlock : availability) {
            timeBlock = new TimeBlock(8, 0, 18, 0);
        }
    }
    
    /**
     * Init constructor with custom availability
     * @param name          account name
     * @param password      account password
     * @param availability  initial availability
     */
    public User(String name, String password, TimeBlock[] availability) {
        this.name = name;
        this.password = password;
        events = new ArrayList<Event>();
        for (int i = 0; i < 7; i++) {
            this.availability[i] = availability[i];
        }
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
    
    /**
     * Getter for events
     * @return  list of user's events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }
    
    /**
     * Resets user's weekly availability
     * @param newAvailability
     */
    public void setAvailability(TimeBlock[] newAvailability) {
        for (int i = 0; i < 7; i++) {
            availability[i] = newAvailability[i];
        }
    }
    
    /**
     * Gets a user's availability on a given day, including scheduled events
     * @param day   the given day
     * @return      a strictly sorted list of times the user is available
     */
    public ArrayList<TimeBlock> getAvailability(Date day) {
        ArrayList<TimeBlock> netAvailability = new ArrayList<TimeBlock>();
        ArrayList<OneTimeEvent> oneEvent;
        ArrayList<TimeBlock> busyTimes;
        Date begin = day;
        Date end = day;
        Date eventStart;
        Date eventEnd;
        ListIterator<Event> it = events.listIterator();
        
        begin.setHours(0);
        begin.setMinutes(0);
        begin.setSeconds(0);
        end.setHours(24);
        end.setMinutes(0);
        end.setSeconds(0);
        
        netAvailability.add(availability[day.getDay()]);
        
        while (it.hasNext()) {
            oneEvent = it.next().getEvents(begin, end);
            busyTimes = new ArrayList<TimeBlock>();
            for (OneTimeEvent event : oneEvent) {
                eventStart = event.getStartDate();
                eventEnd = event.getEndDate();
                busyTimes.add(new TimeBlock(eventStart.getHours(), eventStart.getMinutes(),
                                            eventEnd.getHours(), eventEnd.getMinutes()));
            }
            TimeBlock.difference(netAvailability, busyTimes);
        }
        
        return netAvailability;
    }
}

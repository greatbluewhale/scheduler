/**
 * 
 * 
 * @author Nick
 *
 */

import java.util.ArrayList;
import java.util.Date;

/**
 * Event abstract superclass for all events
 * 
 * @author Nick
 *
 */
public abstract class Event {
    protected String name;
    protected String location;
    protected User[] attendees;
    protected User creator;
    
    public Event(String name, String location, User[] attendees, User creator){
        this.name = name;
        this.location = location;
        this.attendees = attendees;
        this.creator = creator;
    }
    
    /**
     * Gets all events within a certain time frame
     * 
     * @return  an ArrayList of OneTimeEvent objects representing
     *          the events in the given time interval
     */
    public abstract ArrayList<OneTimeEvent> getEvents(Date min, Date max);
}

/**
 * 
 * 
 * @author Nick
 *
 */

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
    
    public Event() {
        name = "Event";
        location = "";
        attendees = { User };
        creator = User;
    }
    
    /**
     * Gets all events within a certain time frame
     * 
     * @return
     */
    public abstract OneTimeEvent[] getEvents(Date min, Date max);
}

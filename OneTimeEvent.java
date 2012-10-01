import java.sql.Date;


public class OneTimeEvent extends Event {
    private Date start;
    private Date end;
    // Inherited members...
    // protected String name;
    // protected String location;
    // protected User[] attendees;
    // protected User creator;
    
    
    public OneTimeEvent() {
        super();
        start = Date.today;
        end = start + 1 // hour 
    }
    
    public OneTimeEvent(String name, String location, User[] attendees, User creator, Date start, Date end) {
        super(name, location, attendees, creator);
        this.start = start;
        this.end = end;
    }
    
    public OneTimeEvent[] getEvents() {
        return this.getEvents(0, 0);
    }
    
    public OneTimeEvent[] getEvents(Date min, Date max) {
        return {this}
    }
}
import java.util.Date;
import java.util.ArrayList;


public class OneTimeEvent extends Event {
    private Date start;
    private Date end;
    // Inherited members...
    // protected String name;
    // protected String location;
    // protected User[] attendees;
    // protected User creator;
    
    // TODO: maybe get rid of the default constructor? when would we use this?
    /*
    public OneTimeEvent() {
        super();
        start = Date.today;
        end = start + 1 // hour 
    }
    */
    
    public OneTimeEvent(String name, String location, User[] attendees, User creator, Date start, Date end) {
        super(name, location, attendees, creator);
        this.start = start;
        this.end = end;
    }
    
    public Date getStartDate(){
        return start;
    }
    
    public Date getEndDate(){
        return end;
    }
    
    // TODO: maybe get rid of this? it seems trivial since the time interval 
    // from 0 to 0 won't contain any events
    /*
    public OneTimeEvent[] getEvents() {
        return this.getEvents(0, 0);
    }
    */
    
    public ArrayList<OneTimeEvent> getEvents(Date min, Date max) {
        ArrayList<OneTimeEvent> list = new ArrayList<OneTimeEvent>();
        list.add(this);
        return list;
    }
}
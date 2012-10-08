import java.util.Calendar;
import java.util.Date;

// TODO: Fill this class with meaningful content (right now it's just a placeholder)
public class User {
    private String name;
    private String password;
    private Event[] events;         // TODO make this a list?
    private int[] availability;
    
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        availablilty = // TODO
    }
    
    public void addEvent(Event newEvent) {
        events.add(newEvent);
    }
    
    public void deleteEvent(Event deleteEvent) {
        events.delete(deleteEvent);
    }
    
    public Event[] getEvents() {
        return events;
    }
    
    public void setAvailability() {
        // TODO
    }
    
    public Time[] getAvailability() {
        return availability;
    }
}

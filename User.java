import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.ListIterator;

// TODO: Fill this class with meaningful content (right now it's just a placeholder)
public class User {
    private String name;
    private String password;
    private ArrayList<Event> events;
    private int[][] availability = new int[4][7];
    
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        for (int[] timeBlock : availability) {
            timeBlock[0] = 8;
            timeBlock[1] = 0;
            timeBlock[2] = 18;
            timeBlock[3] = 0;
        }
    }
    
    public void addEvent(Event newEvent) {
        events.add(newEvent);
    }
    
    public void deleteEvent(Event deleteEvent) {
        events.remove(deleteEvent);
    }
    
    public ArrayList<Event> getEvents() {
        return events;
    }
    
    public void setAvailability(int[][] newAvailability) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                availability[i][j] = newAvailability[i][j];
            }
        }
    }
    
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
        
        netAvailability.add(new TimeBlock(0, 0, 24, 0));
        
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

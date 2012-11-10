/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

import java.util.Date;
import java.util.ArrayList;

/**
 * This class represents an event that occurs only once (and does not repeat).
 * 
 * @author Nick
 */
public class OneTimeEvent extends Event implements Comparable<OneTimeEvent> {
    private Date start; // the start time of the event
    private Date end;   // the end time of the event
    
    /**
     * Init constructor.
     * 
     * @param name          name of the event
     * @param location      location of the event
     * @param attendees     users attending event
     * @param creator       creator of the event
     * @param start         the start time of the event
     * @param end           the end time of the event
     */
    public OneTimeEvent(String name, String location, User[] attendees, User creator, Date start, Date end) {
        super(name, location, attendees, creator);
        this.start = start;
        this.end = end;
    }
    
    /**
     * Getter for the start date
     * @return  the start date
     */
    public Date getStartDate(){
        return start;
    }
    
    /**
     * Getter for the end date
     * @return  the end date
     */
    public Date getEndDate(){
        return end;
    }
    
    /**
     * This method returns an ArrayList containing this OneTimeEvent 
     * object. Since this event never repeats, there will be at most 
     * one event in the given time interval.
     */
    @Override
    public ArrayList<OneTimeEvent> getEvents(Date min, Date max) {
        ArrayList<OneTimeEvent> list = new ArrayList<OneTimeEvent>();
        if (min.before(start) && max.after(end)){
            list.add(this);
        }
        return list;
    }

    @Override
    public int compareTo(OneTimeEvent other) {
        return this.start.compareTo(other.start);
    }
}
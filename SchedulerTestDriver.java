
/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * This is the test driver for the scheduler program (project phase 1).
 * 
 * @author Amuthan
 *
 */
public class SchedulerTestDriver {
    
    /**
     * This prints the TimeBlock objects in the ArrayList parameter.
     * @param availabilities    an ArrayList of TimeBlocks representing availabilities
     */
    private static void printAvailabilities(ArrayList<TimeBlock> availabilities){
        Iterator<TimeBlock> it = availabilities.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println();
    }
    
    /**
     * This is the main method of the test driver.
     */
    public static void main(String[] args){
        try {
            // Set the interval from Oct. 8 at 11am until Dec. 30 at 10am
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2012, 9, 8, 11, 0);
            Date start = cal.getTime();
            cal.set(2012, 11, 30, 10, 0);
            Date end = cal.getTime();
            
            // Test the User class's constructors
            TimeBlock[] availability = new TimeBlock[7];
            for (int i=0; i<7; i++){
                availability[i] = new TimeBlock(9,0,17,30);
            }
            // userA is available from 9:00 to 17:30
            User userA = new User("Amu", "abc123", availability);
            // userB is available from 8:00 to 18:00
            User userB = new User("Nick", "letmein");
            User[] bothUsers = {userA, userB};
            
            // Print the one-time events for these recurring events
            Event[] events = new Event[6];
            // Daily from 12:00 to 12:30
            events[0] = new DailyRecurringEvent("Lunch", "Anywhere", bothUsers, userA, 12, 0, 12, 30);
            // Mondays from 9:05 to 9:55
            events[1] = new WeeklyRecurringEvent("CMPSC Recitation", "IST", bothUsers, userB, 9, 5, 9, 55, Calendar.MONDAY);
            // The 17th of every month from 12:00 to 12:30
            events[2] = new MonthlyDateRecurringEvent("Special Lunch", "Home", bothUsers, userA, 12, 0, 12, 30, 17);
            // 2nd Wednesday of every month from 17:15 to 17:45
            events[3] = new MonthlyDayRecurringEvent("Club Meeting", "101 Thomas", bothUsers, userB, 17, 15, 17, 45, Calendar.WEDNESDAY, 2);
            // December 29th of every year from 00:00 to 23:59
            events[4] = new YearlyRecurringEvent("Birthday", "Anywhere", bothUsers, userA, 0, 0, 23, 59, Calendar.DECEMBER, 29);
            // November 5th, 2012 from 10:00 to 10:30
            cal.set(2012, 10, 5, 10, 0);
            Date startEvent = cal.getTime();
            cal.set(Calendar.MINUTE, 30);
            Date endEvent = cal.getTime();
            events[5] = new OneTimeEvent("Interview Prep Session", "Bryce Jordan Center", bothUsers, userB, startEvent, endEvent);
            
            // Print all occurrences of these events
            for (Event event : events){
                // Add both users to the event
                userA.addEvent(event);
                userB.addEvent(event);
                // Print the event info
                System.out.println(event.getName());
                System.out.println(event.getLocation());
                // Generate and print the list of occurrences of the event
                List<OneTimeEvent> list = event.getEvents(start, end);
                Iterator<OneTimeEvent> it = list.iterator();
                while (it.hasNext()){
                    OneTimeEvent ev = it.next();
                    System.out.println(ev.getStartDate() + " - " + ev.getEndDate());
                }
                System.out.print("\n\n\n");
            }
            
            // Print the user availabilities on Oct. 8
            cal.setTime(start);
            System.out.println("Available times for " + userA.getName());
            printAvailabilities(userA.getAvailability(cal));
            System.out.println("Available times for " + userB.getName());
            printAvailabilities(userB.getAvailability(cal));
            
            // Remove CMPSC Recitation for userB, and change availabilities to 9:00-17:30
            userB.deleteEvent(events[1]);
            userB.setAvailability(availability);
            // Print the adjusted availabilities for userB
            System.out.println("Available times for " + userB.getName() + " after adjustments.");
            printAvailabilities(userB.getAvailability(cal));
            
            // Create time block for 9:00-10:00 + 12:30-17:00
            ArrayList<TimeBlock> testListA = new ArrayList<TimeBlock>();
            testListA.add(new TimeBlock(9,0,10,0));
            testListA.add(new TimeBlock(12,30,17,0));
            // Create time block for 9:30-13:00
            ArrayList<TimeBlock> testListB = new ArrayList<TimeBlock>();
            testListB.add(new TimeBlock(9,30,13,0));
            // Test the methods in TimeBlock.java
            System.out.println("Printing list A, containing 9:00-10:00 and 12:30-17:00");
            printAvailabilities(testListA);
            System.out.println("Printing list B, containing 9:30-13:00");
            printAvailabilities(testListB);
            System.out.println("Printing intersection of lists A and B");
            printAvailabilities(TimeBlock.intersect(testListA, testListB));
            System.out.println("Printing complement of list A");
            printAvailabilities(TimeBlock.complement(testListA));
            System.out.println("Printing A minus B");
            printAvailabilities(TimeBlock.difference(testListA, testListB));
            
        } catch (Exception e){
            // Just print the error message
            System.out.println(e.getMessage());
        }
        
    }
}

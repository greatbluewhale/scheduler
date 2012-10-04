import java.util.*;

public class SchedulerTestDriver {
    
    public static void printEventTimes(Event ev, Date start, Date end){
        List<OneTimeEvent> list = ev.getEvents(start, end);
        Iterator<OneTimeEvent> it = list.iterator();
        while (it.hasNext()){
            OneTimeEvent event = it.next();
            System.out.println(event.getStartDate() + " " + event.getEndDate());
        }
    }
    
    public static void main(String[] args){
        // Set the interval from now until Nov. 30 at 10am
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        Date start = cal.getTime();
        cal.set(2012, 10, 30, 10, 0);
        Date end = cal.getTime();
        
        // Print the one-time events for these recurring events
        Event ev = new DailyRecurringEvent("Dinner", "Anywhere", null, null, 18, 0, 19, 30);
        printEventTimes(ev, start, end);
        Event ev2 = new WeeklyRecurringEvent("CMPSC Recitation", "IST", null, null, 
                9, 5, 9, 55, Calendar.MONDAY);
        System.out.print("\n\n\n");
        printEventTimes(ev2, start, end);
    }
}

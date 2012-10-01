
public abstract class Event {
    public String name;
    public String location;
    public User[] attendees;
    public User creator;
    public abstract OneTimeEvent[] getEvents();
}

public class OneTimeEvent extends Event {
    public Date start;
    public Date end;
    public OneTimeEvent[] getEvents(){
        return {this};
    }
}

public abstract class RecurringEvent extends Event {
    public abstract OneTimeEvent[] getEvents();
}

public class DailyRecurringEvent extends RecurringEvent {
    
}

public class WeeklyRecurringEvent extends RecurringEvent {
    
}

public class MonthlyByDateRecurringEvent extends RecurringEvent {
    
}

public class MonthlyByDayRecurringEvent extends RecurringEvent {
    
}

public class YearlyRecurringEvent extends RecurringEvent {
    
}
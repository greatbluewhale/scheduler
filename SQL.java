/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Program: Project 3
 * Section: 2
 * Date:    10 Dec 2012
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Nick
 * @version 1.0
 */
public abstract class SQL {
    public static Connection dbConnection;
    public static Statement stmt;
    public static User user;
    
    static {
        user = SchedulerMain.application.currentUser;
        try {
            dbConnection = DriverManager.getConnection("jdbc:derby://localhost:1527/scheduler", "root",
                                                       "root");
            stmt = dbConnection.createStatement();
        } catch (Exception ex) {
            // TODO
        }
    }
    
    /**
     * Gets all the events for the current user
     */
    public static ArrayList<Event> pullEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        try {
            ResultSet results = stmt.executeQuery(String.format(
                    "select * from events where event_id in (select event_id from users_events where user_id='%s'",
                    user.getName()));
            startDate.setTime(results.getDate("start_date"));
            while (results.next()) {
                try {
                    switch (results.getInt("recurrence")) {
                    case 0:
                        endDate = startDate;
                        startDate.set(Calendar.HOUR, results.getTime("start_time").getHours());
                        startDate.set(Calendar.MINUTE, results.getTime("start_time").getMinutes());
                        endDate.set(Calendar.HOUR, results.getTime("end_time").getHours());
                        endDate.set(Calendar.MINUTE, results.getTime("end_time").getMinutes());
                        events.add(new OneTimeEvent(results.getString("title"), 
                                                    results.getString("location"),
                                                    null,
                                                    user,
                                                    startDate.getTime(),
                                                    endDate.getTime()));
                        break;
                    case 1:
                        endDate.setTime(results.getTime("end_date"));
                        events.add(new DailyRecurringEvent(results.getString("title"),
                                                           results.getString("location"),
                                                           null,
                                                           user,
                                                           results.getTime("start_time").getHours(),
                                                           results.getTime("start_time").getMinutes(),
                                                           results.getTime("end_time").getHours(),
                                                           results.getTime("end_time").getMinutes(),
                                                           startDate,
                                                           endDate));
                        break;
                    case 2:
                        endDate.setTime(results.getTime("end_date"));
                        events.add(new WeeklyRecurringEvent(results.getString("title"),
                                                            results.getString("location"),
                                                            null,
                                                            user,
                                                            results.getTime("start_time").getHours(),
                                                            results.getTime("start_time").getMinutes(),
                                                            results.getTime("end_time").getHours(),
                                                            results.getTime("end_time").getMinutes(),
                                                            startDate.get(Calendar.DAY_OF_WEEK),
                                                            startDate,
                                                            endDate));
                        break;
                    case 3:
                        endDate.setTime(results.getTime("end_date"));
                        events.add(new MonthlyDateRecurringEvent(results.getString("title"),
                                                                 results.getString("location"),
                                                                 null,
                                                                 user,
                                                                 results.getTime("start_time").getHours(),
                                                                 results.getTime("start_time").getMinutes(),
                                                                 results.getTime("end_time").getHours(),
                                                                 results.getTime("end_time").getMinutes(),
                                                                 startDate.get(Calendar.DATE),
                                                                 startDate,
                                                                 endDate));
                        break;
                    case 4:
                        endDate.setTime(results.getTime("end_date"));
                        events.add(new MonthlyDayRecurringEvent(results.getString("title"),
                                                                results.getString("location"),
                                                                null,
                                                                user,
                                                                results.getTime("start_time").getHours(),
                                                                results.getTime("start_time").getMinutes(),
                                                                results.getTime("end_time").getHours(),
                                                                results.getTime("end_time").getMinutes(),
                                                                startDate.get(Calendar.DAY_OF_WEEK),
                                                                startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH),
                                                                startDate,
                                                                endDate));
                        break;
                    case 5:
                        endDate.setTime(results.getTime("end_date"));
                        events.add(new YearlyRecurringEvent(results.getString("title"),
                                                            results.getString("location"),
                                                            null,
                                                            user,
                                                            results.getTime("start_time").getHours(),
                                                            results.getTime("start_time").getMinutes(),
                                                            results.getTime("end_time").getHours(),
                                                            results.getTime("end_time").getMinutes(),
                                                            startDate.get(Calendar.MONTH),
                                                            startDate.get(Calendar.DATE),
                                                            startDate,
                                                            endDate));
                        break;
                    default:
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return events;
    }
    
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            ResultSet results = stmt.executeQuery("select * from users");
            while (results.next()) {
                try {
                    users.add(new User(results.getString("user_id"), results.getString("password"),
                                       new TimeBlock(results.getTime("avail_start").getHours(),
                                                     results.getTime("avail_start").getMinutes(),
                                                     results.getTime("avail_end").getHours(),
                                                     results.getTime("avail_end").getMinutes())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return users;
    }
    
    public static void createEvent(String name, String location, Calendar startDate, TimeBlock times, int recurrence, Calendar stopDate, User[] attendees) throws SQLException {
        ResultSet results = stmt.executeQuery("select max(event_id) from events");
        results.next();
        int eventID = results.getInt(1);
        
        if (location == null) {
            if (recurrence == 0) {
                stmt.execute(String.format(
                        "insert into events (event_id, title, start_date, start_time, end_time, recurrence) values (%d, '%s', '%d-%d-%d', '%d:%d', '%d:%d', 0)",
                        eventID, name, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
                        startDate.get(Calendar.DATE), times.getStartHour(), times.getStartMinute(),
                        times.getEndHour(), times.getEndMinute()));
            } else {
                stmt.execute(String.format(
                        "insert into events (event_id, title, start_date, start_time, end_time, recurrence, stop_date) values (%d, '%s', '%d-%d-%d', '%d:%d', '%d:%d', %d, '%d-%d-%d')",
                        eventID, name, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
                        startDate.get(Calendar.DATE), times.getStartHour(), times.getStartMinute(),
                        times.getEndHour(), times.getEndMinute(), recurrence,
                        stopDate.get(Calendar.YEAR), stopDate.get(Calendar.MONTH),
                        stopDate.get(Calendar.DATE)));
            }
        } else {
            if (recurrence == 0) {
                stmt.execute(String.format(
                        "insert into events (event_id, title, location, start_date, start_time, end_time, recurrence) values (%d, '%s', '%s', '%d-%d-%d', '%d:%d', '%d:%d', 0)",
                        eventID, name, location, startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                        times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                        times.getEndMinute()));
            } else {
                stmt.execute(String.format(
                        "insert into events (event_id, title, location, start_date, start_time, end_time, recurrence, stop_date) values (%d, '%s', '%s', '%d-%d-%d', '%d:%d', '%d:%d', %d, '%d-%d-%d')",
                        eventID, name, location, startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                        times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                        times.getEndMinute(), recurrence, stopDate.get(Calendar.YEAR),
                        stopDate.get(Calendar.MONTH), stopDate.get(Calendar.DATE)));
            }
        }
    }
    
    public static void updateEvent(String name, String location, Calendar startDate, TimeBlock times, int recurrence, Calendar endDate, User[] attendees) {
        
    }
}

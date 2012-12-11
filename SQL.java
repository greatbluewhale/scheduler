/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Program: Project 3
 * Section: 2
 * Date:    10 Dec 2012
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.ListIterator;

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
            ex.printStackTrace();
        }
    }
    
    /**
     * Gets all the events for the current user
     */
    public static ArrayList<Event> pullEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        Event event;
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
                        endDate.setTime(startDate.getTime());
                        startDate.set(Calendar.HOUR, results.getTime("start_time").getHours());
                        startDate.set(Calendar.MINUTE, results.getTime("start_time").getMinutes());
                        endDate.set(Calendar.HOUR, results.getTime("end_time").getHours());
                        endDate.set(Calendar.MINUTE, results.getTime("end_time").getMinutes());
                        event = new OneTimeEvent(results.getString("title"), 
                                                 results.getString("location"),
                                                 null,
                                                 user,
                                                 startDate.getTime(),
                                                 endDate.getTime());
                        event.setEventID(results.getInt("event_id"));
                        events.add(event);
                        break;
                    case 1:
                        endDate.setTime(results.getTime("end_date"));
                        event = new DailyRecurringEvent(results.getString("title"),
                                                        results.getString("location"),
                                                        null,
                                                        user,
                                                        results.getTime("start_time").getHours(),
                                                        results.getTime("start_time").getMinutes(),
                                                        results.getTime("end_time").getHours(),
                                                        results.getTime("end_time").getMinutes(),
                                                        startDate,
                                                        endDate);
                        event.setEventID(results.getInt("event_id"));
                        events.add(event);
                        break;
                    case 2:
                        endDate.setTime(results.getTime("end_date"));
                        event = new WeeklyRecurringEvent(results.getString("title"),
                                                         results.getString("location"),
                                                         null,
                                                         user,
                                                         results.getTime("start_time").getHours(),
                                                         results.getTime("start_time").getMinutes(),
                                                         results.getTime("end_time").getHours(),
                                                         results.getTime("end_time").getMinutes(),
                                                         startDate.get(Calendar.DAY_OF_WEEK),
                                                         startDate,
                                                         endDate);
                        event.setEventID(results.getInt("event_id"));
                        events.add(event);
                        break;
                    case 3:
                        endDate.setTime(results.getTime("end_date"));
                        event = new MonthlyDateRecurringEvent(results.getString("title"),
                                                              results.getString("location"),
                                                              null,
                                                              user,
                                                              results.getTime("start_time").getHours(),
                                                              results.getTime("start_time").getMinutes(),
                                                              results.getTime("end_time").getHours(),
                                                              results.getTime("end_time").getMinutes(),
                                                              startDate.get(Calendar.DATE),
                                                              startDate,
                                                              endDate);
                        event.setEventID(results.getInt("event_id"));
                        events.add(event);
                        break;
                    case 4:
                        endDate.setTime(results.getTime("end_date"));
                        event = new MonthlyDayRecurringEvent(results.getString("title"),
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
                                                             endDate);
                        event.setEventID(results.getInt("event_id"));
                        events.add(event);
                        break;
                    case 5:
                        endDate.setTime(results.getTime("end_date"));
                        event = new YearlyRecurringEvent(results.getString("title"),
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
                                                         endDate);
                        event.setEventID(results.getInt("event_id"));
                        events.add(event);
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
    
    /**
     * Creates an event and returns the eventID
     * @param name
     * @param location
     * @param startDate
     * @param times
     * @param recurrence
     * @param stopDate
     * @param attendees
     * @return event_id of created event
     * @throws SQLException
     */
    public static int createEvent(String name, String location, User[] attendees, Calendar startDate, TimeBlock times, int recurrence, Calendar stopDate) throws SQLException {
        ResultSet results = stmt.executeQuery("select max(event_id) from events");
        results.next();
        int eventID = results.getInt(1);
        
        // Add event
        if (location == null) {
            if (recurrence == 0) {
                stmt.execute(String.format(
                        "insert into events (event_id, title, user_id, start_date, start_time, end_time, recurrence) values (%d, '%s', '%s', '%d-%d-%d', '%d:%d', '%d:%d', 0)",
                        eventID, name, user.getName(), startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                        times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                        times.getEndMinute()));
            } else {
                stmt.execute(String.format(
                        "insert into events (event_id, title, user_id, start_date, start_time, end_time, recurrence, stop_date) values (%d, '%s', '%s', '%d-%d-%d', '%d:%d', '%d:%d', %d, '%d-%d-%d')",
                        eventID, name, user.getName(), startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                        times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                        times.getEndMinute(), recurrence, stopDate.get(Calendar.YEAR),
                        stopDate.get(Calendar.MONTH), stopDate.get(Calendar.DATE)));
            }
        } else {
            if (recurrence == 0) {
                stmt.execute(String.format(
                        "insert into events (event_id, title, location, user_id, start_date, start_time, end_time, recurrence) values (%d, '%s', '%s', '%s', '%d-%d-%d', '%d:%d', '%d:%d', 0)",
                        eventID, name, location, user.getName(), startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                        times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                        times.getEndMinute()));
            } else {
                stmt.execute(String.format(
                        "insert into events (event_id, title, location, user_id, start_date, start_time, end_time, recurrence, stop_date) values (%d, '%s', '%s', '%s', '%d-%d-%d', '%d:%d', '%d:%d', %d, '%d-%d-%d')",
                        eventID, name, location, user.getName(), startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                        times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                        times.getEndMinute(), recurrence, stopDate.get(Calendar.YEAR),
                        stopDate.get(Calendar.MONTH), stopDate.get(Calendar.DATE)));
            }
        }
        
        // Add user_events
        stmt.execute(String.format("insert into users_events (user_id, event_id) values ('%s', %d)",
                                   user.getName(), eventID));
        for (User attendee : attendees) {
            stmt.execute(String.format("insert into user_events (user_id, event_id) values ('%s', %d)",
                                       attendee.getName(), eventID));
        }
        
        return eventID;
    }
    
    public static void createUser(User newUser) {
        try {
            stmt.execute(String.format("insert into users (user_id, password, avail_start, avail_end) values ('%s', '%s', '%d:%d', '%d:%d')",
                                       newUser.getName(), newUser.getPassword(),
                                       newUser.getDailyAvailability().getStartHour(),
                                       newUser.getDailyAvailability().getStartMinute(),
                                       newUser.getDailyAvailability().getEndHour(),
                                       newUser.getDailyAvailability().getEndMinute()));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public static void updateEvent(int eventID, String name, String location, Calendar startDate, TimeBlock times, int recurrence, Calendar endDate, User[] attendees) {
        ArrayList<String> newUserIDs = new ArrayList<String>();
        ListIterator<String> newIt; // iterator for newUserIDs
        String newUser;             // next String in newUserIDs
        ArrayList<String> oldUserIDs = new ArrayList<String>();
        ListIterator<String> oldIt; // iterator for oldUserIDs
        String oldUser;             // next String in oldUserIDs
        int compareTo;              // result of newUser.compareTo(oldUser)
        ResultSet results;
        
        try {
            // Update event
            if (recurrence == 0) {
                stmt.execute(String.format("update events set title='%s', location='%s', start_date='%d-%d-%d', start_time='%d:%d', end_time='%d:%d', recurrence=0 where event_id = %d",
                                           name, location, startDate.get(Calendar.YEAR),
                                           startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                                           times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                                           times.getEndMinute(), eventID));
            } else {
                stmt.execute(String.format("update events set title='%s', location='%s', start_date='%d-%d-%d', start_time='%d:%d', end_time='%d:%d', recurrence=&d, stop_date='%d-%d-%d' where event_id = %d",
                                           name, location, startDate.get(Calendar.YEAR),
                                           startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE),
                                           times.getStartHour(), times.getStartMinute(), times.getEndHour(),
                                           times.getEndMinute(), recurrence, endDate.get(Calendar.YEAR),
                                           endDate.get(Calendar.MONTH), endDate.get(Calendar.DATE), eventID));
            }
            
            // Update user_event
            // Check for attendee changes
            for (User attendee : attendees) {
                newUserIDs.add(attendee.getName());
            }
            Collections.sort(newUserIDs);
            results = stmt.executeQuery("select user_id from users_events order by user_id");
            while (results.next()) {
                oldUserIDs.add(results.getString("user_id"));
            }
            newIt = newUserIDs.listIterator();
            oldIt = oldUserIDs.listIterator();
            
            while (newIt.hasNext() && oldIt.hasNext()) {
                newUser = newIt.next();
                oldUser = oldIt.next();
                compareTo = newUser.compareTo(oldUser);
                
                if (compareTo < 0) {
                    // a user was added
                    stmt.execute(String.format("insert into users_events (user_id, event_id) values ('%s', %d)",
                                               newUser, eventID));
                    oldIt.previous();
                } else if (compareTo > 0) {
                    // a user was removed
                    stmt.execute(String.format("delete from users_events where user_id='%s' and event_id=%d",
                                               oldUser, eventID));
                    newIt.previous();
                }
            }
            // Add any remaining new users
            while (newIt.hasNext()) {
                stmt.execute(String.format("insert into users_events (user_id, event_id) values ('%s', %d)",
                                           newIt.next(), eventID));
            }
            // Delete any remaining old users
            while (oldIt.hasNext()) {
                stmt.execute(String.format("delete from users_events where user_id='%s' and event_id=%d",
                                           oldIt.next(), eventID));
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public static void updateUser(User newUser) {
        try {
            stmt.execute(String.format("update users set password='%s', avail_start='%d:%d', avail_end='%d:%d' where user_id='%s'",
                                       newUser.getPassword(),
                                       newUser.getDailyAvailability().getStartHour(),
                                       newUser.getDailyAvailability().getStartMinute(),
                                       newUser.getDailyAvailability().getEndHour(),
                                       newUser.getDailyAvailability().getEndMinute(),
                                       newUser.getName()));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public static void deleteEvent(int eventID) {
        try {           
            stmt.execute(String.format("delete from users_events where event_id=%d", eventID));
            stmt.execute(String.format("delete from events where event_id=%d", eventID));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public static void deleteEvent(Event event) {
        deleteEvent(event.getEventID());
    }
    
    /**
     * Deletes user and all their events
     * @param user
     */
    public static void deleteUser(User user) {
        try {
            stmt.execute(String.format("delete from users_events where user_id='%s'", user.getName()));
            stmt.execute(String.format("delete from events where creator_id='%s'", user.getName()));
            stmt.execute(String.format("delete from users where user_id='%s'", user.getName()));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}

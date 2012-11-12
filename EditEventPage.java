/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    11/1/2012
 */

import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.*;

/**
 * EditEventPage class to display panel for user to create/edit an event
 * 
 * @author Nicholas Dyszel
 * @version 1.0  11/1/2012
 */
public class EditEventPage extends JPanel {
    private Event      newEvent;            // event object to create / edit
    private String     title;               // object to store title
    private JTextField titleField;          // text field for user to enter title
    private String     location;            // object to store location
    private JTextField locationField;       // text field for user to enter location
    private String     attendees;           // who is attending
    private JTextField addAttendeeField;    // text field for user to add new attendee
    private JList      attendeesList;       // list of all attendees for user to edit
    private Calendar   date;                // Date object to store in event
    private JComboBox  monthDropDown;       // drop-down list to choose month
    private JTextField dayField;            // text field for user to enter day
    private JTextField yearField;           // text field for user to enter year
    private TimeBlock  times;               // block of time stored in event
    private JTextField startHourField;      // text field for user to enter hour of start time
    private JTextField startMinField;       // text field for user to enter minute of start time
    private JComboBox  startAMPMDropDown;   // drop-down list to choose AM/PM of start time
    private JTextField endHourField;        // text field for user to enter hour of end time
    private JTextField endMinField;         // text field for user to enter minute of end time
    private JComboBox  endAMPMDropDown;     // drop-down list to choose AM/PM of end time
    private Boolean    isRecurring;         // is the event a recurring event?
    private JCheckBox  isRecurringBox;      // check box for user to select recurring event
    private RecurType  recurType;           // type of recurrance
                                                // NOTE: recurrance parameters inferred from date
    private JComboBox  recurDropDown;       // drop-down list for types of recurring events
    private Calendar   endInterval;
    
    // enumeration of different recurring event types
    private enum RecurType { DAILY, WEEKLY, MONTHLY_BY_DATE, MONTHLY_BY_DAY, YEARLY };
    
    /**
     * Default constructor
     * Creates new event
     */
    public EditEventPage() {
        this(new OneTimeEvent("", "", { getUser() }, getUser(), new Date(), new Date()));
    }
    
    /**
     * Init constructor
     * Edits an existing event
     * @throws Exception  a TimeBlock exception
     */
    public EditEventPage(Event eventToEdit) throws Exception {
        Calendar tempEnd;
        
        newEvent = eventToEdit;
        title = eventToEdit.getName();
        titleField = new JTextField(title);
        location = eventToEdit.getLocation();
        locationField = new JTextField(location);
            
        if (eventToEdit instanceof OneTimeEvent) {
            (date = new GregorianCalendar()).setTime(((OneTimeEvent) eventToEdit).getStartDate());
            (tempEnd = new GregorianCalendar()).setTime(((OneTimeEvent) eventToEdit).getEndDate());
            times = new TimeBlock(date.get(Calendar.HOUR_OF_DAY),
                                  date.get(Calendar.MINUTE),
                                  tempEnd.get(Calendar.HOUR_OF_DAY),
                                  tempEnd.get(Calendar.MINUTE));
            isRecurring = false;
        } else {
            date = ((RecurringEvent) eventToEdit).getIntervalStart();
            endInterval = ((RecurringEvent) eventToEdit).getIntervalEnd();
            times = ((RecurringEvent) eventToEdit).getTimes();
            isRecurring = true;
            if (eventToEdit instanceof DailyRecurringEvent) {
                recurType = RecurType.DAILY;
            } else if (eventToEdit instanceof WeeklyRecurringEvent) {
                recurType = RecurType.WEEKLY;
            } else if (eventToEdit instanceof MonthlyDateRecurringEvent) {
                recurType = RecurType.MONTHLY_BY_DATE;
            } else if (eventToEdit instanceof MonthlyDayRecurringEvent) {
                recurType = RecurType.MONTHLY_BY_DAY;
            } else {
                recurType = RecurType.YEARLY;
            }
        }
    }
    
    /**
     * Paints panel
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

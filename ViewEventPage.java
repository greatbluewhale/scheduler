/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    11/14/2012
 */

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;

/**
 * ViewEventPage class to view an event without editing it
 * @author Nicholas Dyszel
 * @version 1.0  14 Nov 2012
 */
@SuppressWarnings("serial")
public class ViewEventPage extends PagePanel implements ActionListener {
    private JLabel  title;
    private JLabel  location;
    private JLabel  attendees;
    private JLabel  date;
    private JLabel  times;
    private JLabel  recur;
    private JButton ok;
    private JButton edit;
    
    private Event   currentEvent;
    
    public ViewEventPage() {
        super();
        
        JPanel titlePanel = new JPanel();
        JPanel locationPanel = new JPanel();
        JPanel attendeesPanel = new JPanel();
        JPanel datePanel = new JPanel();
        JPanel timesPanel = new JPanel();
        JPanel recurPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        titlePanel.add(new JLabel("Name:"));
        title = new JLabel(" ");
        titlePanel.add(title);
        
        locationPanel.add(new JLabel("Location:"));
        location = new JLabel(" ");
        locationPanel.add(location);
        
        attendeesPanel.add(new JLabel("Attendees:"));
        attendees = new JLabel(" ");
        attendeesPanel.add(attendees);
        
        datePanel.add(new JLabel("Date:"));
        date = new JLabel(" ");
        datePanel.add(date);
        
        timesPanel.add(new JLabel("Time:"));
        times = new JLabel(" ");
        timesPanel.add(times);
        
        recur = new JLabel("This event does not repeat.");
        recurPanel.add(recur);
        
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        ok = new JButton("OK");
        ok.addActionListener(this);
        buttonPanel.add(ok);
        edit = new JButton("Edit");
        edit.addActionListener(this);
        buttonPanel.add(edit);
        
        JPanel[] mainPanels = {titlePanel, locationPanel, attendeesPanel, datePanel, timesPanel, recurPanel, buttonPanel};
        add(Utils.stackPanels(mainPanels));
        
        this.setEnabled(false);
    }
    
    @Override
    public void activate() {
        title.setText(" ");
        location.setText(" ");
        attendees.setText(" ");
        date.setText(" ");
        times.setText(" ");
        recur.setText("This event does not repeat.");  
        
        this.setEnabled(true);
    }
    
    @Override
    public void setFields(Event eventToView, OneTimeEvent childEvent) throws Exception {
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        TimeBlock timeBlock;
        String recurType;
        String dayOfWeek;
        String weekOfMonth;
        
        currentEvent = eventToView;
        
        title.setText(eventToView.getName());
        location.setText(eventToView.getLocation());
        
        StringBuilder attendeesText = new StringBuilder();
        User[] attendeesArray = eventToView.getAttendees();
        if (attendeesArray != null && attendeesArray.length > 0){
            attendeesText.append(attendeesArray[0].getName());
            for (int i=1; i<attendeesArray.length; i++){
                attendeesText.append(", ");
                attendeesText.append(attendeesArray[i].getName());
            }
        } else {
            attendeesText.append("none");
        }
        attendees.setText(attendeesText.toString());
        
        if (eventToView instanceof OneTimeEvent) {
            startDate.setTime(((OneTimeEvent) eventToView).getStartDate());
            endDate.setTime(((OneTimeEvent) eventToView).getEndDate());
            timeBlock = new TimeBlock(startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE),
                                      endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE));
            
            recur.setText("This event does not repeat.");
        } else {
            if (childEvent != null){
                startDate.setTime(childEvent.getStartDate());
            } else {
                startDate = ((RecurringEvent) eventToView).getIntervalStart();
            }
            
            endDate = ((RecurringEvent) eventToView).getIntervalEnd();
            timeBlock = ((RecurringEvent) eventToView).getTimes();
            
            if (eventToView instanceof DailyRecurringEvent) {
                recurType = "day";
            } else if (eventToView instanceof WeeklyRecurringEvent) {
                recurType = dayToString(startDate.get(Calendar.DAY_OF_WEEK));
            } else if (eventToView instanceof MonthlyDateRecurringEvent) {
                recurType = "month";
            } else if (eventToView instanceof MonthlyDayRecurringEvent) {
                switch (startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
                case 1:
                    weekOfMonth = "first";
                    break;
                case 2:
                    weekOfMonth = "second";
                    break;
                case 3:
                    weekOfMonth = "third";
                    break;
                case 4:
                    weekOfMonth = "fourth";
                    break;
                case 5:
                    weekOfMonth = "fifth";
                    break;
                default:
                    weekOfMonth = startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) + "th";
                }
                dayOfWeek = dayToString(startDate.get(Calendar.DAY_OF_WEEK));
                recurType = weekOfMonth + " " + dayOfWeek;
            } else if (eventToView instanceof YearlyRecurringEvent) {
                recurType = "year";
            } else {
                recurType = "";
            }
            
            recur.setText("This event repeats every " + recurType + " and stops on: " +
                          (endDate.get(Calendar.MONTH)+1) + "/" + endDate.get(Calendar.DATE) + "/" +
                          endDate.get(Calendar.YEAR));
        }
        
        date.setText((startDate.get(Calendar.MONTH)+1) + "/" + startDate.get(Calendar.DATE) + "/" +
                     startDate.get(Calendar.YEAR));
        times.setText(timeBlock.toString());
        
        edit.setEnabled(SQL.isCreator(currentEvent));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            this.setEnabled(false);
            SchedulerMain.application.showMonthlyView();
        } else if (e.getSource() == edit) {
            SchedulerMain.application.showEditEventPage(currentEvent);
        }
    }
    
    public static String dayToString(int day) {
        switch (day) {
        case Calendar.MONDAY:
            return "Monday";
        case Calendar.TUESDAY:
            return "Tuesday";
        case Calendar.WEDNESDAY:
            return "Wednesday";
        case Calendar.THURSDAY:
            return "Thursday";
        case Calendar.FRIDAY:
            return "Friday";
        case Calendar.SATURDAY:
            return "Saturday";
        case Calendar.SUNDAY:
            return "Sunday";
        }
        return "";
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

@SuppressWarnings("serial")
public class MonthlyViewPage extends PagePanel implements ActionListener{
    
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM yyyy");
    private static final int DAYS_IN_WEEK = 7;
    private static final String[] NAMES_OF_DAYS = {"Sunday", "Monday", "Tuesday", 
                                                   "Wednesday", "Thursday", "Friday", "Saturday"};
    
    private static final int MONTH_STRING_SIZE = 30;
    
    private JPanel topPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel weekLabelPanel = new JPanel();
    private JPanel calendarPanel = new JPanel();
    private JButton previous = new JButton("Previous");
    private JLabel monthLabel = new JLabel();
    private JButton next = new JButton("Next");
    private CalendarCell[] cells;
    
    private Calendar month;
    
    public MonthlyViewPage(){
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        topPanel.setLayout(new BorderLayout());
        topPanel.add(Utils.createPanelWrapper(previous), BorderLayout.WEST);
        topPanel.add(Utils.createPanelWrapper(monthLabel), BorderLayout.CENTER);
        topPanel.add(Utils.createPanelWrapper(next), BorderLayout.EAST);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(weekLabelPanel, BorderLayout.NORTH);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        
        monthLabel.setFont(new Font(monthLabel.getFont().getFontName(), Font.BOLD, MONTH_STRING_SIZE));
        previous.addActionListener(this);
        next.addActionListener(this);
        
        weekLabelPanel.setLayout(new GridLayout(1, DAYS_IN_WEEK));
        for (String dayName : NAMES_OF_DAYS){
            weekLabelPanel.add(new JLabel(dayName, SwingConstants.CENTER));
        }
        
        month = new GregorianCalendar();
    }
    
    @Override
    public void activate(){
        loadMonth();
    }
    
    private void changeMonth(boolean increase){
        month.add(Calendar.MONTH, increase ? 1 : -1);
        loadMonth();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        changeMonth(event.getSource() == next);
    }
    
    private void loadMonth(){
        int numWeeksInMonth = month.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int calendarMonth = month.get(Calendar.MONTH);
        Calendar calendar = new GregorianCalendar();
        
        // Update month label and clear calendar
        monthLabel.setText(MONTH_FORMAT.format(month.getTime()));
        calendarPanel.setVisible(false);
        calendarPanel.removeAll();
        
        // Set up grid
        calendarPanel.setLayout(new GridLayout(numWeeksInMonth, DAYS_IN_WEEK));
        cells = new CalendarCell[DAYS_IN_WEEK*numWeeksInMonth];
        
        // Get the first Sunday of the calendar (not necessarily the 1st of the month)
        calendar.set(month.get(Calendar.YEAR), calendarMonth, 1, 0, 0);
        calendar.getTime(); // hack
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        
        // Get a sorted list of relevant OneTimeEvent objects
        Date start = calendar.getTime();
        calendar.add(Calendar.DATE, DAYS_IN_WEEK*numWeeksInMonth);
        Date end = calendar.getTime();
        ArrayList<OneTimeEvent> events = new ArrayList<OneTimeEvent>();
        Iterator<Event> userEventsIt = SchedulerMain.application.currentUser.getEvents().iterator();
        while (userEventsIt.hasNext()){
            Iterator<OneTimeEvent> oneTimeEventIt = userEventsIt.next().getEvents(start, end).iterator();
            while (oneTimeEventIt.hasNext()){
                events.add(oneTimeEventIt.next());
            }
        }
        Collections.sort(events);
        
        // Start at the first Sunday of the calendar
        calendar.set(month.get(Calendar.YEAR), calendarMonth, 1, 0, 0);
        calendar.getTime(); // hack
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Iterator<OneTimeEvent> eventsIt = events.iterator();
        OneTimeEvent currentEvent = eventsIt.hasNext() ? eventsIt.next() : null;
        for (int cellIndex=0; cellIndex<numWeeksInMonth*DAYS_IN_WEEK; cellIndex++){
            // Create a new CalendarCell object
            cells[cellIndex] = new CalendarCell(calendar, (calendar.get(Calendar.MONTH) == calendarMonth));
            calendarPanel.add(cells[cellIndex]);
            
            // Add events to the cell if necessary
            calendar.add(Calendar.DATE, 1);
            while (currentEvent != null && currentEvent.getStartDate().before(calendar.getTime())){
                cells[cellIndex].addEvent(currentEvent);
                currentEvent = eventsIt.hasNext() ? eventsIt.next() : null;
            }
        }
        calendarPanel.setVisible(true);
    }
}

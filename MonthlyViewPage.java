import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

@SuppressWarnings("serial")
public class MonthlyViewPage extends PagePanel implements ActionListener{
    
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM yyyy");
    public static final int DAYS_IN_WEEK = 7;
    private static final String[] NAMES_OF_DAYS = {"Sunday", "Monday", "Tuesday", 
                                                   "Wednesday", "Thursday", "Friday", "Saturday"};
    
    private static final int MONTH_STRING_SIZE = 30;
    
    private static final Calendar START_OF_TODAY;
    private static final Calendar END_OF_TODAY;
    static {
        START_OF_TODAY = new GregorianCalendar();
        END_OF_TODAY = new GregorianCalendar();
        START_OF_TODAY.set(Calendar.HOUR_OF_DAY, 0);
        START_OF_TODAY.set(Calendar.MINUTE, 0);
        START_OF_TODAY.set(Calendar.SECOND, 0);
        END_OF_TODAY.set(Calendar.HOUR_OF_DAY, 23);
        END_OF_TODAY.set(Calendar.MINUTE, 59);
        END_OF_TODAY.set(Calendar.SECOND, 59);
    }
    
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
        int todayCell = -1;
        
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
        ArrayList<OneTimeEvent> events = SchedulerMain.application.currentUser.getEvents(start, end);
        
        // Start at the first Sunday of the calendar
        calendar.set(month.get(Calendar.YEAR), calendarMonth, 1, 0, 0);
        calendar.getTime(); // hack
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Iterator<OneTimeEvent> eventsIt = events.iterator();
        OneTimeEvent currentEvent = eventsIt.hasNext() ? eventsIt.next() : null;
        for (int cellIndex=0; cellIndex<numWeeksInMonth*DAYS_IN_WEEK; cellIndex++){
            boolean isToday = (START_OF_TODAY.before(calendar) && END_OF_TODAY.after(calendar));
            // Check if it's today (TODO: inefficient)
            if (isToday){
                todayCell = cellIndex;
            }
            
            // Create a new CalendarCell object
            cells[cellIndex] = new CalendarCell(calendar, (calendar.get(Calendar.MONTH) == calendarMonth), isToday);
            calendarPanel.add(cells[cellIndex]);
            
            // Add events to the cell if necessary
            calendar.add(Calendar.DATE, 1);
            while (currentEvent != null && currentEvent.getStartDate().before(calendar.getTime())){
                cells[cellIndex].addEvent(currentEvent);
                currentEvent = eventsIt.hasNext() ? eventsIt.next() : null;
            }
        }
        
        // Add weather icons
        if (SchedulerMain.application.checkIsWeatherEnabled() && todayCell != -1){
            // TODO: don't hardcode zip code
            ImageIcon[] icons = Weather.getWeather("16802");
            if (icons != null){
                for (ImageIcon icon : icons){
                    cells[todayCell].addWeatherIcon(icon);
                    todayCell++;
                    if (todayCell >= cells.length){
                        break;
                    }
                }
            }
        }
        
        calendarPanel.setVisible(true);
    }
}

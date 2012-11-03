import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressWarnings("serial")
public class MonthlyViewPage extends JPanel implements ActionListener{
    
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM yyyy");
    private static final int DAYS_IN_WEEK = 7;
    private static final String[] NAMES_OF_DAYS = {"Sunday", "Monday", "Tuesday", 
                                                   "Wednesday", "Thursday", "Friday", "Saturday"};
    
    private static final int MONTH_STRING_SIZE = 30;
    
    private static JPanel createPanelWrapper(Component object){
        JPanel panel = new JPanel();
        panel.add(object);
        return panel;
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
    
    public MonthlyViewPage(Calendar monthInit){
        super();
        
        month = monthInit;
        monthLabel.setFont(new Font("Arial", Font.BOLD, MONTH_STRING_SIZE));
        loadMonth();
        
        previous.addActionListener(this);
        next.addActionListener(this);
        
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        topPanel.setLayout(new BorderLayout());
        topPanel.add(createPanelWrapper(previous), BorderLayout.WEST);
        topPanel.add(createPanelWrapper(monthLabel), BorderLayout.CENTER);
        topPanel.add(createPanelWrapper(next), BorderLayout.EAST);
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(weekLabelPanel, BorderLayout.NORTH);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        
        weekLabelPanel.setLayout(new GridLayout(1, DAYS_IN_WEEK));
        for (String dayName : NAMES_OF_DAYS){
            weekLabelPanel.add(new JLabel(dayName, SwingConstants.CENTER));
        }
    }
    
    public void changeMonth(boolean increase){
        month.add(Calendar.MONTH, increase ? 1 : -1);
        loadMonth();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        changeMonth(event.getSource() == next);
    }
    
    private void loadMonth(){
        Calendar calendar = new GregorianCalendar();
        int numWeeksInMonth = month.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int calendarMonth = month.get(Calendar.MONTH);
        monthLabel.setText(MONTH_FORMAT.format(month.getTime()));
        calendarPanel.setVisible(false);
        calendarPanel.removeAll();
        
        calendarPanel.setLayout(new GridLayout(numWeeksInMonth, DAYS_IN_WEEK));
        cells = new CalendarCell[DAYS_IN_WEEK*numWeeksInMonth];
        
        calendar.set(month.get(Calendar.YEAR), calendarMonth, 1);
        // TODO: Calling getTime is just a hack to get calendar to properly
        // load the time.
        calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        for (int cellIndex=0; cellIndex<numWeeksInMonth*DAYS_IN_WEEK; cellIndex++){
            cells[cellIndex] = new CalendarCell(calendar, (calendar.get(Calendar.MONTH) == calendarMonth));
            calendarPanel.add(cells[cellIndex]);
            calendar.add(Calendar.DATE, 1);
        }
        calendarPanel.setVisible(true);
    }
}

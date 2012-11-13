import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

@SuppressWarnings("serial")
public class SchedulerApplication extends JFrame {
    
    private final int STARTING_WIDTH = 1000;
    private final int STARTING_HEIGHT = 700;
    private final int MINIMUM_WIDTH = 800;
    private final int MINIMUM_HEIGHT = 600;
    
    private final String TITLE = "Scheduler Manager";
    
    private JPanel mainPanel = new JPanel();
    private JPanel currentPage;
    
    public User currentUser;
    
    public void startup(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(TITLE);
        this.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        
        // TODO: temporary
        currentUser = new User("Test User", "letmein");
        /* try {
            Calendar cal = new GregorianCalendar();
            cal.set(2012, Calendar.NOVEMBER, 15);
            currentUser.addEvent(new OneTimeEvent("Project Due Date", null, null, currentUser, cal.getTime(), cal.getTime()));
            currentUser.addEvent(new YearlyRecurringEvent("Birthday", null, null, currentUser, 0, 0, 23, 59, Calendar.AUGUST, 9));
            currentUser.addEvent(new MonthlyDayRecurringEvent("1st Monday", null, null, currentUser, 12, 0, 12, 30, Calendar.MONDAY, 1));
            currentUser.addEvent(new MonthlyDateRecurringEvent("28th of the month", null, null, currentUser, 18, 0, 18, 45, 28));
            currentUser.addEvent(new WeeklyRecurringEvent("CMPSC 221", "IST 220", null, currentUser, 9, 5, 9, 55, Calendar.MONDAY));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */
        
        try {
            Calendar cal = new GregorianCalendar();
            cal.set(2012, Calendar.NOVEMBER, 15);
            currentPage = new EditEventPage(new OneTimeEvent("Project Due Date", null, null, currentUser, cal.getTime(), cal.getTime()));
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(currentPage, BorderLayout.CENTER);
        }
        catch (Exception e) {}
        
        setSize(STARTING_WIDTH, STARTING_HEIGHT);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }
}

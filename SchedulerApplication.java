import java.awt.BorderLayout;
import java.awt.CardLayout;
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
    private CardLayout cardLayout = new CardLayout();
    
    private enum Page {
        LOGIN_PAGE(0), MONTHLY_VIEW_PAGE(1);
        private int index;
        private Page(int index){
            this.index = index;
        }
        public int getIndex(){
            return this.index;
        }
    }
    
    private PagePanel[] pages = {new LoginPage(), new MonthlyViewPage()};
    
    public User currentUser;
    
    public void startup(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(TITLE);
        this.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(cardLayout);
        for (int i=0; i<pages.length; i++){
            mainPanel.add(pages[i], ""+i);
        }
        setCurrentPage(Page.LOGIN_PAGE);
        
        setSize(STARTING_WIDTH, STARTING_HEIGHT);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }
    
    public void setCurrentPage(Page pageName){
        cardLayout.show(mainPanel, ""+pageName.getIndex());
        pages[pageName.getIndex()].activate();
    }
    
    public void logIn(User user){
        currentUser = user;
        // TODO: remove this try-catch block later, used only for testing purposes
        try {
            Calendar cal = new GregorianCalendar();
            cal.set(2012, Calendar.NOVEMBER, 15);
            currentUser.addEvent(new OneTimeEvent("Project Due Date", null, null, currentUser, cal.getTime(), cal.getTime()));
            currentUser.addEvent(new YearlyRecurringEvent("Birthday", null, null, currentUser, 0, 0, 23, 59, Calendar.AUGUST, 9, null, null));
            currentUser.addEvent(new MonthlyDayRecurringEvent("1st Monday", null, null, currentUser, 12, 0, 12, 30, Calendar.MONDAY, 1, null, null));
            currentUser.addEvent(new MonthlyDateRecurringEvent("28th of the month", null, null, currentUser, 18, 0, 18, 45, 28, null, null));
            currentUser.addEvent(new WeeklyRecurringEvent("CMPSC 221", "IST 220", null, currentUser, 9, 5, 9, 55, Calendar.MONDAY, null, null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        setCurrentPage(Page.MONTHLY_VIEW_PAGE);
    }
}

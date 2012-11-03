import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.GregorianCalendar;

import javax.swing.*;

@SuppressWarnings("serial")
public class SchedulerApplication extends JFrame {
    
    private static final int STARTING_WIDTH = 1000;
    private static final int STARTING_HEIGHT = 700;
    private static final int MINIMUM_WIDTH = 800;
    private static final int MINIMUM_HEIGHT = 600;
    
    private static final String TITLE = "Scheduler Manager";
    
    private JPanel mainPanel = new JPanel();
    private JPanel currentPage;
    
    public SchedulerApplication(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(TITLE);
        this.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        
        currentPage = new MonthlyViewPage(new GregorianCalendar());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(currentPage, BorderLayout.CENTER);
        
        setSize(STARTING_WIDTH, STARTING_HEIGHT);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }
}

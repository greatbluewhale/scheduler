import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class SchedulerApplication extends JFrame implements ActionListener {
    
    private final int STARTING_WIDTH = 1000;
    private final int STARTING_HEIGHT = 700;
    private final int MINIMUM_WIDTH = 800;
    private final int MINIMUM_HEIGHT = 600;
    
    private final String ENABLE_WEATHER = "Enable Weather Feature";
    private final String DISABLE_WEATHER = "Disable Weather Feature";
    
    private boolean isWeatherEnabled = true;
    
    private JPanel mainPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    
    private JMenuItem addEvent = new JMenuItem("Create Event");
    private JMenuItem viewUser = new JMenuItem("View User Info");
    private JMenuItem viewCalendar = new JMenuItem("View Calendar");
    private JMenuItem weatherItem = new JMenuItem(DISABLE_WEATHER);
    
    private enum Page {
        LOGIN_PAGE(0), MONTHLY_VIEW_PAGE(1), EDIT_EVENT_PAGE(2), VIEW_EVENT_PAGE(3), VIEW_USER_PAGE(4);
        private int index;
        private Page(int index){
            this.index = index;
        }
        public int getIndex(){
            return this.index;
        }
    }
    
    private PagePanel[] pages = {new LoginPage(), new MonthlyViewPage(), new EditEventPage(), new ViewEventPage(), new ViewUserPage()};
    
    public User currentUser;
    
    public void startup(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(SchedulerMain.TITLE);
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu options = new JMenu("Options");
        addEvent.setEnabled(false);
        viewUser.setEnabled(false);
        viewCalendar.setEnabled(false);
        options.add(addEvent);
        options.add(viewUser);
        options.add(viewCalendar);
        menuBar.add(options);
        
        JMenu features = new JMenu("Features");
        features.add(weatherItem);
        menuBar.add(features);
        
        setJMenuBar(menuBar);
        
        addEvent.addActionListener(this);
        viewUser.addActionListener(this);
        viewCalendar.addActionListener(this);
        weatherItem.addActionListener(this);
        
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
    
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addEvent){
            showCreateEventPage();
        } else if (e.getSource() == viewUser){
            showUserView();
        } else if (e.getSource() == viewCalendar){
            showMonthlyView();
        } else if (e.getSource() == weatherItem){
            toggleWeatherFeature();
        }
    }
    
    public void setCurrentPage(Page pageName){
        cardLayout.show(mainPanel, ""+pageName.getIndex());
        pages[pageName.getIndex()].activate();
    }
    
    public void logIn(User user){
        currentUser = user;
        addEvent.setEnabled(true);
        viewUser.setEnabled(true);
        viewCalendar.setEnabled(true);
        
        showMonthlyView();
    }
    
    public void showCreateEventPage(){
        setCurrentPage(Page.EDIT_EVENT_PAGE);
    }
    
    public void showEditEventPage(Event event){
        setCurrentPage(Page.EDIT_EVENT_PAGE);
        try {
            pages[Page.EDIT_EVENT_PAGE.getIndex()].setFields(event);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void showViewEventPage(Event event, OneTimeEvent childEvent){
        // childEvent can be null (if event is already a OneTimeEvent)
        setCurrentPage(Page.VIEW_EVENT_PAGE);
        try {
            pages[Page.VIEW_EVENT_PAGE.getIndex()].setFields(event, childEvent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void showMonthlyView(){
        setCurrentPage(Page.MONTHLY_VIEW_PAGE);
    }
    
    public void showUserView(){
        setCurrentPage(Page.VIEW_USER_PAGE);
    }
    
    private void toggleWeatherFeature(){
        isWeatherEnabled = !isWeatherEnabled;
        weatherItem.setText(isWeatherEnabled ? DISABLE_WEATHER : ENABLE_WEATHER);
        if (currentUser != null){
            showMonthlyView();
        }
    }
    
    public boolean checkIsWeatherEnabled(){
        return isWeatherEnabled;
    }
    
    public void showDeletePopup(Event ev){
        // TODO: make a confirmation popup
        deleteEvent(ev);
        showMonthlyView();
    }
    
    public void addEvent(Event ev){
        ev.creator.addEvent(ev);
        if (ev.attendees != null){
            for (User person : ev.attendees){
                person.addEvent(ev);
            }
        }
    }
    
    public void editEvent(Event oldEvent, Event newEvent){
        // TODO: risky! we're changing the pointer instead of the object
        User user = oldEvent.creator;
        user.deleteEvent(oldEvent);
        user.addEvent(newEvent);
    }
    
    public void deleteEvent(Event ev){
        ev.creator.deleteEvent(ev);
        if (ev.attendees != null){
            for (User person : ev.attendees){
                person.deleteEvent(ev);
            }
        }
    }
}

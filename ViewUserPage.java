import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;


@SuppressWarnings("serial")
public class ViewUserPage extends PagePanel {
    
    private static final int UPCOMING_EVENTS_SIZE = 10;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a");
    
    private JLabel usernameLabel = new JLabel();
    private JLabel[] upcomingEventsLabels = new JLabel[UPCOMING_EVENTS_SIZE];
    private JPanel availabilityPanel = new JPanel();
    
    public ViewUserPage(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel infoPanel = new JPanel();
        JPanel upcomingEventsPanel = new JPanel();
        
        JPanel[] panels = {infoPanel, upcomingEventsPanel, availabilityPanel};
        add(Utils.stackPanels(panels));
        
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
        JLabel nameHeadingLabel = new JLabel("Name: ");
        infoPanel.add(nameHeadingLabel);
        infoPanel.add(usernameLabel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));
        
        upcomingEventsPanel.setLayout(new BoxLayout(upcomingEventsPanel, BoxLayout.PAGE_AXIS));
        JLabel eventsLabel = new JLabel("Upcoming Events:");
        upcomingEventsPanel.add(eventsLabel);
        upcomingEventsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        Font headingFont = new Font(getFont().getFontName(), Font.BOLD, 20);
        Font regularFont = new Font(getFont().getFontName(), Font.PLAIN, 20);
        
        for (int i=0; i<UPCOMING_EVENTS_SIZE; i++){
            upcomingEventsLabels[i] = new JLabel();
            upcomingEventsPanel.add(upcomingEventsLabels[i]);
            upcomingEventsLabels[i].setFont(regularFont);
        }
        
        nameHeadingLabel.setFont(headingFont);
        usernameLabel.setFont(regularFont);
        eventsLabel.setFont(headingFont);
    }
    
    @Override
    public void activate() {
        usernameLabel.setText(SchedulerMain.application.currentUser.getName());
        
        Calendar cal = new GregorianCalendar();
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date end = cal.getTime();
        Iterator<OneTimeEvent> eventsIt = SchedulerMain.application.currentUser.getEvents(start, end).iterator();
        boolean hasEvents = eventsIt.hasNext();
        for (int i=0; i<UPCOMING_EVENTS_SIZE; i++){
            if (eventsIt.hasNext()){
                OneTimeEvent ev = eventsIt.next();
                upcomingEventsLabels[i].setText(DATE_FORMAT.format(ev.getStartDate()) + " at " + TIME_FORMAT.format(ev.getStartDate()) + " -- " + ev.getName());
            } else {
                upcomingEventsLabels[i].setText("");
            }
        }
        if (!hasEvents){
            upcomingEventsLabels[0].setText("There are no upcoming events.");
        }
    }

}

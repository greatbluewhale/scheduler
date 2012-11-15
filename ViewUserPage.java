import javax.swing.*;
import java.awt.*;


@SuppressWarnings("serial")
public class ViewUserPage extends PagePanel {
    
    private JLabel usernameLabel = new JLabel();
    private JPanel contactsPanel = new JPanel();
    
    public ViewUserPage(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JPanel infoPanel = new JPanel();
        JPanel upcomingEventsPanel = new JPanel();
        add(infoPanel, BorderLayout.NORTH);
        add(upcomingEventsPanel, BorderLayout.CENTER);
        
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(usernameLabel, BorderLayout.NORTH);
        infoPanel.add(contactsPanel, BorderLayout.CENTER);
        
        upcomingEventsPanel.add(new JLabel("third"));
        
        Font font = new Font(getFont().getFontName(), Font.BOLD, 20);
        usernameLabel.setFont(font);
    }
    
    @Override
    public void activate() {
        usernameLabel.setText("Name: " + SchedulerMain.application.currentUser.getName());
    }

}

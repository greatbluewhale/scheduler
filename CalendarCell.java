import java.awt.Color;
import java.util.Calendar;

import javax.swing.*;

@SuppressWarnings("serial")
public class CalendarCell extends JPanel {
    
    private static final Color NOT_IN_MONTH_COLOR = new Color(220, 220, 220);
    
    public CalendarCell(Calendar calendar, boolean isInMonth){
        // Calendar calendar will be modified outside of this class, so do NOT 
        // store a reference to the object
        setBackground(isInMonth ? Color.WHITE : NOT_IN_MONTH_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel dayNumberLabel = new JLabel(""+calendar.get(Calendar.DATE));
        add(dayNumberLabel);
    }
    
    public void addEvent(OneTimeEvent event){
        add(new JLabel(event.getName()));
    }
}

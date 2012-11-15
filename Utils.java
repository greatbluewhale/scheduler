import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JPanel;


public class Utils {
    
    public static JPanel createPanelWrapper(Component object){
        JPanel panel = new JPanel();
        panel.add(object);
        return panel;
    }
    
    public static Calendar createCalendar(int year, int month, int date, boolean startOfDay){
        Calendar result = new GregorianCalendar();
        result.set(year, month, date, startOfDay ? 0 : 23, startOfDay ? 0 : 59);
        return result;
    }
    
    public static boolean dateIsValid(int year, int month, int date){
        Calendar cal = createCalendar(year, month, date, true);
        // Calendar object automatically turns invalid dates like Jan 32nd into valid dates like Feb 1st, 
        // so we just check to see if the values of the Calendar object have changed
        return (cal.get(Calendar.DATE) == date && cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year);
    }
    
    public static boolean dateIsLessThan(int startYear, int startMonth, int startDate, int endYear, int endMonth, int endDate){
        return createCalendar(startYear, startMonth, startDate, true).before(createCalendar(endYear, endMonth, endDate, false));
    }
    
    /**
     * This method takes the array of panels, and returns a JPanel that
     * contains a vertical stack of the given panels.
     * 
     * @param panels
     * @return
     */
    public static JPanel stackPanels(JPanel[] panels){
        JPanel current = panels[panels.length-1];
        // For every panel except the last one
        for (int i=panels.length-2; i>=0; i--){
            JPanel container = new JPanel();
            container.setLayout(new BorderLayout());
            container.add(panels[i], BorderLayout.NORTH);
            container.add(current, BorderLayout.CENTER);
            current = container;
        }
        return current;
    }
}

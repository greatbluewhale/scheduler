import javax.swing.JPanel;

// TODO: using a PagePanel seems like bad design

@SuppressWarnings("serial")
public abstract class PagePanel extends JPanel{
    public abstract void activate();
    public void setFields(Event event) throws Exception {
        // Intentionally empty
    }
    public void setFields(Event event, OneTimeEvent child) throws Exception {
        // Intentionally empty
    }
}

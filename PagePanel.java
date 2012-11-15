import javax.swing.JPanel;


@SuppressWarnings("serial")
public abstract class PagePanel extends JPanel{
    public abstract void activate();
    public void setFields(Event eventToEdit) throws Exception {
        // Intentionally empty
    }
}


public class SchedulerMain {
    
    // TODO: The following members are temporary and should be removed once 
    // SQL support is introduced
    public static String testUsername = "nickdyszel";
    public static String testPassword = "password";
    
    public static SchedulerApplication application;
    
    public static final String TITLE = "My Organizer";
    
    public static void main(String[] args){
        application = new SchedulerApplication();
        application.startup();
        application.setVisible(true);
    }
}


public class SchedulerMain {
    
    public static SchedulerApplication application;
    
    public static void main(String[] args){
        application = new SchedulerApplication();
        application.startup();
        application.setVisible(true);
    }
}

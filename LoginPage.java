import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


@SuppressWarnings("serial")
public class LoginPage extends PagePanel implements ActionListener {
    
    private static final String INVALID_LOGIN_TEXT = "The username/password you entered was invalid.";
    private static final String NO_QUOTES_TEXT = "The username/password cannot have any single quotes.";
    private static final String DUPLICATE_USER_TEXT = "This username is already in use. Use a different username.";
    private static final int TITLE_SIZE = 30;
    
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton submit = new JButton("Log In");
    private JButton createUser = new JButton("Create New User");
    private JLabel invalidLogin = new JLabel();
    
    public LoginPage(){
        JPanel titlePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel invalidLoginPanel = new JPanel();
        JPanel[] panels = {titlePanel, buttonPanel, invalidLoginPanel};
        
        setLayout(new BorderLayout());
        add(Utils.stackPanels(panels), BorderLayout.CENTER);
        JLabel title = new JLabel(SchedulerMain.TITLE);
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, TITLE_SIZE));
        
        titlePanel.add(title);
        buttonPanel.add(new JLabel("Username: "));
        buttonPanel.add(usernameField);
        buttonPanel.add(new JLabel("Password: "));
        buttonPanel.add(passwordField);
        buttonPanel.add(submit);
        buttonPanel.add(createUser);
        invalidLoginPanel.add(invalidLogin);
        invalidLogin.setForeground(Color.RED);
        
        usernameField.addActionListener(this);
        passwordField.addActionListener(this);
        submit.addActionListener(this);
        createUser.addActionListener(this);
    }
    
    @Override
    public void activate(){
        usernameField.setText(SchedulerMain.testUsername);
        passwordField.setText(SchedulerMain.testPassword);
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.indexOf('\'') == -1 && password.indexOf('\'') == -1){
            if (event.getSource() == createUser){
                User user = new User(username, password);
                if (SQL.createUser(user)){
                    SchedulerMain.application.logIn(user);
                } else {
                    invalidLogin.setText(DUPLICATE_USER_TEXT);
                }
            } else {
                // If the event originated from passwordField or submit button, then try to log in
                User user = SQL.checkLoginCredentials(username, password);
                if (user != null){
                    SchedulerMain.application.logIn(user);
                } else {
                    invalidLogin.setText(INVALID_LOGIN_TEXT);
                }
            }
        } else {
            invalidLogin.setText(NO_QUOTES_TEXT);
        }
    }
}

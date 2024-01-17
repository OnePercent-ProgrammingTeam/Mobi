package gr.aueb.dmst.onepercent.programming.graphics;

import exceptions.UserNotFoundException;
import graphics.DataUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginPageController {

    @FXML
    private Text failedAuthText;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Button exit;

    @FXML
    void login(ActionEvent event) {
        DataUsers userTable = new DataUsers();

        //key has the answer to the question "does the user exist?" (true or false)
        boolean key = userTable.getUserExistance(usernameField.getText(), passwordField.getText());

        try {
            if (key) {
                System.out.println("User exists");
            } else {
                throw new UserNotFoundException(usernameField.getText());
            }
        } catch (UserNotFoundException e) {
            failedAuthText.setText(e.getMessage());
        }
    }

    @FXML
    void signup(ActionEvent event) {

    }

    @FXML
    void closeApp(ActionEvent event) {
        System.exit(0);
    }

}

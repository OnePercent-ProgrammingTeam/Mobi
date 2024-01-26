package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.IOException;

import exceptions.UserExistsException;
import exceptions.UserNotFoundException;
import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.gui.UserAuthenticationGUI;
import graphics.DataUsers;
import io.github.palexdev.materialfx.controls.MFXButton;
//import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class LoginPageController {

    @FXML
    private Text failedAuthText;

    @FXML
    private MFXButton loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private MFXButton signupButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Button exit;

    MainPageController mainPageController = new MainPageController();

    DataUsers users = new DataUsers();
    DataBase metrics = DataBase.getInstance();

    @FXML
    void login(ActionEvent event) {
        //new check 

        failedAuthText.setText(null);
        UserAuthenticationGUI userAuthGUI = new UserAuthenticationGUI();
        userAuthGUI.setCredentials(usernameField.getText(), passwordField.getText());
        userAuthGUI.checkAuth();
        
        try {
            if (userAuthGUI.getUserExistanceInDocker()) {
                System.out.println("User exists");

                mainPageController.setUsernameString(usernameField.getText());
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
                Scene mainPageScene = new Scene(root, 1300, 700);

                MainGUI.window.setScene(mainPageScene);
                users.handleDataUsers(userAuthGUI.getUsername(), userAuthGUI.getPassword());
                metrics.setURL(userAuthGUI.getUsername());
                metrics.createDatabaseMetrics();
            } else {
                throw new UserNotFoundException(usernameField.getText());
            }
        } catch (UserNotFoundException e) {
            failedAuthText.setText(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error loading the fxml file");
        }
        

        /* 
        //key has the answer to the question "does the user exist?" (true or false)
        boolean key = users.getUserExistanceInDatabase(usernameField.getText(),
                     passwordField.getText());
        failedAuthText.setText(null);

        try {
            if (key) {
                System.out.println("User exists");
                mainPageController.setUsernameString(usernameField.getText());
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
                Scene mainPageScene = new Scene(root, 1000, 600);
                MainGUI.window.setScene(mainPageScene);
            } else {
                throw new UserNotFoundException(usernameField.getText());
            }
        } catch (UserNotFoundException e) {
            failedAuthText.setText(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error loading the fxml file");
        }
        */
    }

    @FXML
    void signup(ActionEvent event) {
        //key has the answer to the question "does the user exist?" (true or false)
        boolean key = users.getUserExistanceInDatabase(usernameField.getText(),
                     passwordField.getText());
        failedAuthText.setText(null);

        try {
            if (!key) {
                System.out.println("User does not exist");

                
                users.insertUsers(usernameField.getText(), passwordField.getText());
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
                Scene mainPageScene = new Scene(root, 1000, 600);

                MainGUI.window.setScene(mainPageScene);
            } else {
                throw new UserExistsException(usernameField.getText());
            }   
        } catch (UserExistsException e) {
            failedAuthText.setText(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error loading the fxml file");
        } 
        users.getAllUsers();
    }

    @FXML
    void closeApp(ActionEvent event) {
        System.exit(0);
    }

}

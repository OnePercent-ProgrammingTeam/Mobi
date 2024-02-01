package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.IOException;

import exceptions.UserNotFoundException;
import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.data.DataUsers;
import gr.aueb.dmst.onepercent.programming.gui.UserAuthenticationGUI;
import io.github.palexdev.materialfx.controls.MFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * ok
 */
public class LoginPageController {
    /** ok */
    public LoginPageController() { }

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



    @FXML
    private CheckBox rememberBox;


    static MainPageController MAIN_PAGE_CONTROLLER;

    DataUsers users = new DataUsers();


    @FXML 
    void initialize() {
        users.createUser();
    }


    @FXML
    void passwordAction() {
        if (users.remember(usernameField.getText()) == null) {
            rememberBox.setSelected(false);
        } else {
            if (passwordField.getText().length() == 0) {
                rememberBox.setSelected(true);
                passwordField.setText(users.remember(usernameField.getText()));
            } 
        }
    }


  


    @FXML
    void login(ActionEvent event) {
    
        
        DataBase metrics = DataBase.getInstance();
        UserAuthenticationGUI userAuthGUI = new UserAuthenticationGUI();
        
        failedAuthText.setText(null);
        userAuthGUI.setCredentials(usernameField.getText(), passwordField.getText());
        userAuthGUI.checkAuth();
        
        try {
            if (userAuthGUI.getUserExistanceInDocker()) {
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml")); 
                Parent root = loader.load();
                
                MAIN_PAGE_CONTROLLER = loader.getController();
                MAIN_PAGE_CONTROLLER.setUsernameString(usernameField.getText());
                Scene mainPageScene = new Scene(root, 1300, 700);

                MainGUI.window.setScene(mainPageScene);


                Boolean check = rememberBox.isSelected();
                users.handleDataUsers(userAuthGUI.getUsername(), userAuthGUI.getPassword(), check);
                int index = users.getUser(userAuthGUI.getUsername(), userAuthGUI.getPassword());
                
                MAIN_PAGE_CONTROLLER.setImageIndex(index);
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
        
    }
    


    @FXML
    void closeApp(ActionEvent event) {
        System.exit(0);
    }

}

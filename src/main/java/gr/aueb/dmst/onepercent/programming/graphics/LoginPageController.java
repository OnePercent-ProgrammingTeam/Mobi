package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.IOException;

import gr.aueb.dmst.onepercent.programming.data.Database;
import gr.aueb.dmst.onepercent.programming.data.User;
import gr.aueb.dmst.onepercent.programming.exceptions.UserNotFoundException;
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
 * A UI controler that manages the user interface and functionalities of the login page
 * in the application.
 * 
 * <P>This page allows users to log in with their credentials, authenticate them, 
 * and proceed to the main page upon successful authentication. It also provides functionality 
 * for remembering the user if the remember me checkbox is checked. 
 * 
 * <p>Not that the login is connected with Docker Hub via 
 * <a href="https://docs.docker.com/docker-hub/api/latest/">Docker Hub API</a>.
 * So the user has to connect with his Docker Hub credentials.
 */
public class LoginPageController {

    /** Static instance of the controller of the main page. */
    static MainPageController MAIN_PAGE_CONTROLLER;

    /** Instance of the database containg the users. */
    User users = new User();

    /** The text appearing when authentication is failes (credentials didn't match). */
    @FXML
    private Text failedAuthText;

    /** The button to proceed to the app after logging in */
    @FXML
    private MFXButton loginButton;

    /** The field, in which the password is submitted, without being visible. */
    @FXML
    private PasswordField passwordField;

    /** The button to sign up.  */
    @FXML
    private MFXButton signupButton;

    /** The field in which the name of the user is submitted.  */
    @FXML
    private TextField usernameField;

    /** The buttons, which is used to exit the program. */
    @FXML
    private Button exit;

    /** 
     * The checkbox, used to make the app remember it's name and automatically provide
     * his password in the future when user name is submitted.
     */
    @FXML
    private CheckBox rememberBox;

    /** Default constructor. */
    public LoginPageController() { }

    /** Initializes the page and calls method that creates the user table in users' database.  */
    @FXML 
    void initialize() {
        users.createUser();
    }


    @FXML
    void passwordAction() {
        if (users.getPassword(usernameField.getText()) == null) {
            rememberBox.setSelected(false);
        } else {
            if (passwordField.getText().length() == 0) {
                rememberBox.setSelected(true);
                passwordField.setText(users.getPassword(usernameField.getText()));
            } 
        }
    }

    /** Processes the log in and authenticates the user if credentials do match.
     * @param event The event to be executed.
     */
    @FXML
    void login(ActionEvent event) {
        Database metrics = Database.getInstance();
        UserAuthenticationGUI userAuthGUI = new UserAuthenticationGUI();
        
        failedAuthText.setText(null);
        userAuthGUI.setCredentials(usernameField.getText(), passwordField.getText());
        userAuthGUI.checkAuth(); 
        try {
            if (userAuthGUI.isSignedIn()) { //Load the main page when authentication is succesfull.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml")); 
                Parent root = loader.load();
                MAIN_PAGE_CONTROLLER = loader.getController();
                MAIN_PAGE_CONTROLLER.setUsernameString(usernameField.getText());
                //Set the scene and window.
                Scene mainPageScene = new Scene(root, 1300, 700);
                MainGUI.window.setScene(mainPageScene);
                //Remember the user if remember checkbox is clicked.
                Boolean check = rememberBox.isSelected();
                users.handleDataUsers(userAuthGUI.getUsername(), userAuthGUI.getPassword(), check);
                int index = users.getUserIconIndex(userAuthGUI.getUsername(), 
                                                   userAuthGUI.getPassword());
                MAIN_PAGE_CONTROLLER.setImageIndex(index);
                metrics.setDatabaseName(userAuthGUI.getUsername());
                metrics.createTables();
            } else {
                throw new UserNotFoundException(usernameField.getText());
            }
        } catch (UserNotFoundException e) {
            failedAuthText.setText(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error loading the fxml file");
        }
    }

    /** Closed the application. */
    @FXML
    void closeApp(ActionEvent event) {
        System.exit(0);
    }
}

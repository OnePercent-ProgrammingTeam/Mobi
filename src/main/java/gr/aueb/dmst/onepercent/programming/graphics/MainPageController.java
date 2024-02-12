package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.controlsfx.control.ToggleSwitch;

import javafx.application.Platform;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.scene.text.Text;

/**
 * Controller class for the main page of the application.
 * 
 * <p>This page includes the left-hand-side menu, top bar, and a blank content area where other 
 * pages are loaded. The left-hand-side menu contains buttons for navigating to different 
 * sections of the application, while the top bar displays user information and controls for 
 * switching between light and dark mode.
 * 
 * <p>It is the started page: the first page to be loaded and always to stay static, after the
 * successfull authentication of the user.
 */
public class MainPageController {

    //Buttons of Left Hand Side menu:

    /** The 'Containers' button of the LHS menu. */
    @FXML
    private Button containersButton;

    /** The 'Images' button of the LHS menu. */
    @FXML
    private Button imagesButton;

    /** The 'Docker Hub' button of the LHS menu. */
    @FXML
    private Button searchButton;

    /** The 'Analytics' button of the LHS menu. */
    @FXML
    private Button analyticsButton;

    /** The 'Help' button of the LHS menu. */
    @FXML
    private Button helpButton;

    /** The 'System' button of the LHS menu. */
    @FXML
    private Button systemButton;

    /** The empty content area in the middle, where pages are loaded. */
    @FXML
    public AnchorPane contentArea;
    
    //Top bar components:

    /** The top bar. */
    @FXML
    private HBox topBar;

    /** The username or email with which the user has logged in. */
    @FXML
    private Text username;

    /** The sun icon, indicating wheter light or dark mode of the application is used. */
    @FXML
    private ImageView sun;

    /** The toggle switch to switch from light to dark mode and vice versa. */
    @FXML
    private ToggleSwitch toggleButton;

    /** The user icon, loaded from our application. */
    @FXML
    private ImageView userIcon;

    //Extras: 

    /** The selected Button */
    private Button selectedButton;
    
    /** Axis X offset. */
    private double xOffset = 0;

    /** Axis Y offset. */
    private double yOffset = 0;

    /** The username. */
    private static String usernameString; 

    /** 
     * The index of the user icon to be assigned to the user from the available.
     * <p>Check the available user icos in resources/images/userIcons
     */
    private static int imageIndex;

    /** Default constructor. */
    public MainPageController() { }

    /**
     * Initializes the appropriate components.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            String path = "src/main/resources/images/userIcons/user" + imageIndex + ".png";
            Path pathToFile = Paths.get(path);
            Image image = new Image(pathToFile.toUri().toString());
            userIcon.setImage(image);    
            username.setText(usernameString);
        });
        
        loadPage("ContainersPage.fxml");
        selectedButton = containersButton;
        selectedButton.getStyleClass().add("selected");
        
        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        topBar.setOnMouseDragged(event -> {
            MainGUI.window.setX(event.getScreenX() - xOffset);
            MainGUI.window.setY(event.getScreenY() - yOffset);
        });
    }

    //TO DO(Cristian Scobioala-Nicoglu): Implement the dark mode to the GUI.
    @FXML 
    void changeToDarkMode() {
        
        // contentArea.getStylesheets()
        //            .add(getClass()
        //            .getResource("../../../../../../../resources/dark-theme.css")
        //            .toExternalForm());
    }

    /**
     * Sets functionality for 'Log Out' button.
     */
    @FXML
    void logOut() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
            Scene mainPageScene = new Scene(root, 1300, 700);
            MainGUI.window.setScene(mainPageScene);
        } catch (IOException e) {
            System.out.println("Error loading the fxml file.");
        }
    }

    /**
     * Sets functionality for 'Containers' button.
     * @param event The event to be executed
     */
    @FXML
    void loadContainers(ActionEvent event) {
        loadPage("ContainersPage.fxml");
        setMenuButtonSelected(containersButton);
    }

    /**
     * Sets functionality for 'Images' button.
     * @param event The event to be executed
     */
    @FXML
    void loadImages(ActionEvent event) {
        loadPage("ImagesPage.fxml");
        setMenuButtonSelected(imagesButton);
    }

    /**
     * Sets functionality for 'Docker Hub' button.
     * @param event The event to be executed
     */
    @FXML
    void loadSearch(ActionEvent event) {
        loadPage("SearchPage.fxml");
        setMenuButtonSelected(searchButton);
    }

    /**
     * Sets functionality for 'Analytics' button.
     * @param event The event to be executed
     */
    @FXML
    void loadAnalytics(ActionEvent event) {
        loadPage("AnalyticsPage.fxml");
        setMenuButtonSelected(analyticsButton);
    }

    /**
     * Sets functionality for 'System' button.
     * @param event The event to be executed
     */
    @FXML
    void loadSystem(ActionEvent event) {
        loadPage("SystemPage.fxml");
        setMenuButtonSelected(systemButton);
    }

    /**
     * Sets functionality for 'Help' button.
     * @param event The event to be executed
     */
    @FXML
    void loadHelp(ActionEvent event) {
        loadPage("HelpPage.fxml");
        setMenuButtonSelected(helpButton);
    }

    /**
     * Sets functionality for the button in the top bar with the
     * three dots.
     * @param event The event to be executed
     */
    @FXML 
    void loadMisc(ActionEvent event) {
        loadPage("MiscPage.fxml");
    }

    /**
     * Sets functionality for the exit (power-off) button at the topbar.
     * @param event The event to be executed
     */
    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Sets image index.
     * @param imageIndex The image of the index.
     */
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    /**
     * Sets username.
     * @param usernameString The name of the user.
     */
    public void setUsernameString(String usernameString) {
        this.usernameString = usernameString;
    }

    /**
     * Sets the content area. 
     * 
     * <p>The area in the middle that is always blank.
     *  Every page is loaded there because top bar and left-hand-side menu are
     *  always static).
     * @param contentArea  the blank content area in the middle of the page.
     */
    public void setContentArea(AnchorPane contentArea) {
        this.contentArea = contentArea;
    }
    
    /**
     * Sets the menu button selected.
     * @param button The button that will be set as selected.
     * 
     * <p>By setting the buttons of the LHS menu selected, user
     * can se in which page is currently in.
     */
    private void setMenuButtonSelected(Button button) {
        selectedButton.getStyleClass().remove("selected");
        selectedButton = button;
        button.getStyleClass().add("selected");
    }

    /** Loads the specified page name.
     * 
     * @param pageName The name of the page that will be loaded.
     */
    void loadPage(String pageName) {
        try {
            // Load the FXML file for the selected page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + pageName));
            Parent pageRoot = loader.load();
            // Set the content area with the loaded page
            contentArea.getChildren().clear();
            contentArea.getChildren().add(pageRoot);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}

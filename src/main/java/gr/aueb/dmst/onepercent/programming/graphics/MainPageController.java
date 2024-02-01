package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.controlsfx.control.ToggleSwitch;

import javafx.scene.image.Image;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * ok
 */

public class MainPageController {

    /** ok */
    public MainPageController() { }
    @FXML
    private Button containersButton;

    @FXML
    public AnchorPane contentArea;

    @FXML
    private Button imagesButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button analyticsButton;


    @FXML
    private Button helpButton;

    @FXML
    private HBox topBar;

    @FXML
    private Text username;

    @FXML
    private ImageView sun;

    @FXML
    private ToggleSwitch toggleButton;

    @FXML
    private ImageView userIcon;

    @FXML
    private Button systemButton;

    private Button selectedButton;
    private double xOffset = 0;
    private double yOffset = 0;
    private static String usernameString; 

    
    /** ok */
    private static int imageIndex;
    /**
     * ok
     * @param imageIndex
     */
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    /**
     * ok
     * @param usernameString
     */
    public void setUsernameString(String usernameString) {
        this.usernameString = usernameString;
    }


    /**
     * ok
     * @param contentArea
     */
    public void setContentArea(AnchorPane contentArea) {
        this.contentArea = contentArea;
    }
    
    /**
     * ok
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

    @FXML 
    void changeToDarkMode() {
        
        contentArea.getStylesheets()
                   .add(getClass()
                   .getResource("../../../../../../../resources/dark-theme.css")
                   .toExternalForm());
    }

    @FXML
    void logOut() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
            Scene mainPageScene = new Scene(root, 1300, 700);
            MainGUI.window.setScene(mainPageScene);
        } catch (IOException e) {
            System.out.println("Error loading the fxml file");
        }
    }

    @FXML
    void loadContainers(ActionEvent event) {
        loadPage("ContainersPage.fxml");
        setMenuButtonSelected(containersButton);
    }

    @FXML
    void loadImages(ActionEvent event) {
        loadPage("ImagesPage.fxml");
        setMenuButtonSelected(imagesButton);
    }

    @FXML
    void loadSearch(ActionEvent event) {
        loadPage("SearchPage.fxml");
        setMenuButtonSelected(searchButton);
    }

    //TODO
    @FXML
    void loadAnalytics(ActionEvent event) {
        loadPage("AnalyticsPage.fxml");
        setMenuButtonSelected(analyticsButton);
    }

    @FXML
    void loadSystem(ActionEvent event) {
        loadPage("SystemPage.fxml");
        setMenuButtonSelected(systemButton);
    }

    @FXML
    void loadHelp(ActionEvent event) {
        loadPage("HelpPage.fxml");
        setMenuButtonSelected(helpButton);
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    private void setMenuButtonSelected(Button button) {
        selectedButton.getStyleClass().remove("selected");
        selectedButton = button;
        button.getStyleClass().add("selected");
    }

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

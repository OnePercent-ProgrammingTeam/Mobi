package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainPageController {

    @FXML
    private Button containersButton;

    @FXML
    public AnchorPane contentArea;

    @FXML
    private Button imagesButton;

    @FXML
    private Button searchButton;

    @FXML
    private void initialize() {
        loadPage("ContainersPage.fxml");
    }

    @FXML
    void loadContainers(ActionEvent event) {
        loadPage("ContainersPage.fxml");
    }

    @FXML
    void loadImages(ActionEvent event) {
        loadPage("ImagesPage.fxml");
    }

    @FXML
    void loadSearch(ActionEvent event) {
        loadPage("SearchPage.fxml");
    }

    private void loadPage(String pageName) {
        try {
            // Load the FXML file for the selected page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + pageName));
            Parent pageRoot = loader.load();
            // Set the content area with the loaded page
            contentArea.getChildren().setAll(pageRoot);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

}

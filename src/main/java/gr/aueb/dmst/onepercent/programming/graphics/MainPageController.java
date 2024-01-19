package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
    private Button helpButton;

    @FXML
    private ScrollPane scrollArea;

    private Button selectedButton;

    @FXML
    private void initialize() {
        loadPage("ContainersPage.fxml");
        selectedButton = containersButton;
        selectedButton.setStyle(selectedButton.getStyle() + "-fx-background-color: #7d7dcf;" +
            "-fx-border-width: 0px 0px 0px 0px;");
    }

    @FXML
    void menuOnHoverEnter(MouseEvent event) {
        Button sourceButton = (Button) event.getSource();
        sourceButton.setStyle(sourceButton.getStyle() + "-fx-background-color: #7d7dcf; " +
            "-fx-border-width: 0px 0px 0px 4px; -fx-border-color: #ffffff;");
    }

    @FXML
    void menuOnHoverExit(MouseEvent event) {
        Button sourceButton = (Button) event.getSource();
        sourceButton.setStyle(sourceButton.getStyle() + "-fx-background-color: #2a2a72; " +
            "-fx-border-width: 0px 0px 0px 0px; -fx-border-color: transparent;");
        setMenuButtonSelected(selectedButton);
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

    @FXML
    void loadHelp(ActionEvent event) {
        loadPage("HelpPage.fxml");
        setMenuButtonSelected(helpButton);
    }

    private void setMenuButtonSelected(Button button) {
        selectedButton.setStyle(selectedButton.getStyle() + "-fx-background-color: transparent;");
        selectedButton = button;
        button.setStyle(button.getStyle() + "-fx-background-color: #7d7dcf;" +
            "-fx-border-width: 0px 0px 0px 0px;");
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

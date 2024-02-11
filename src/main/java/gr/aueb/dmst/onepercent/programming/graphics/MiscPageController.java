package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * ok
 */
public class MiscPageController {

    /** ok */
    public MiscPageController() {
        
    }

    @FXML
    private Button historyButton;

    @FXML
    private VBox miscContentArea;

    @FXML
    private Button notifButton;

    private final String SELECTED_TAB_STYLE = "-fx-background-color: transparent; "
        + "-fx-text-fill: #6200ee; -fx-border-color: #6200ee; -fx-border-width: 0 0 2 0;";

    private final String SECONDARY_TAB_STYLE = "-fx-background-color: transparent; "
        + "-fx-text-fill: #888; -fx-border-color: #888; -fx-border-width: 0 0 2 0;";


    @FXML
    public void initialize() {
        loadNotifications(null);
    }

    @FXML
    public void loadHistory(ActionEvent event) {
        if (historyButton.getStyle().equals(SELECTED_TAB_STYLE)) return;
        notifButton.setStyle(SECONDARY_TAB_STYLE);
        historyButton.setStyle(SELECTED_TAB_STYLE);
        loadPage("HistoryPage.fxml");
    }

    @FXML
    public void loadNotifications(ActionEvent event) {
        if (notifButton.getStyle().equals(SELECTED_TAB_STYLE)) return;
        notifButton.setStyle(SELECTED_TAB_STYLE);
        historyButton.setStyle(SECONDARY_TAB_STYLE);
        loadPage("NotificationsPage.fxml");
    }

    private void loadPage(String pageName) {
        try {
            // Load the FXML file for the selected page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + pageName));
            Parent pageRoot = loader.load();
            // Set the content area with the loaded page
            miscContentArea.getChildren().clear();
            miscContentArea.getChildren().add(pageRoot);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

}

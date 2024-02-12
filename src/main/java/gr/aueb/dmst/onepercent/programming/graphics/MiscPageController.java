package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.control.Button;

import javafx.scene.layout.VBox;

/**
 * Controller class for the Miscellaneous page of the application.
 * 
 * <p>This page includes buttons for navigating between different miscellaneous 
 * sections of the application.
 * 
 * <p>The content area of this page dynamically loads different pages based on user interactions.
 */
public class MiscPageController {

    /** The 'History' button. */
    @FXML
    private Button historyButton;

    /** The content area. */
    @FXML
    private VBox miscContentArea;

    /** The 'Notifications' button. */
    @FXML
    private Button notifButton;

    /** The CSS style for when the tab is selected. */
    private final String SELECTED_TAB_STYLE = "-fx-background-color: transparent; "
        + "-fx-text-fill: #6200ee; -fx-border-color: #6200ee; -fx-border-width: 0 0 2 0;";

    /** The CSS style for when the tab is selected. */
    private final String SECONDARY_TAB_STYLE = "-fx-background-color: transparent; "
        + "-fx-text-fill: #888; -fx-border-color: #888; -fx-border-width: 0 0 2 0;";

    /** Default Constructor. */
    public MiscPageController() { }

    /** Initializer of Notifications. */
    @FXML
    public void initialize() {
        loadNotifications(null);
    }

    /** Loader of History page.
     * @param event The event.
     */
    @FXML
    public void loadHistory(ActionEvent event) {
        //TO DO(Scobioala & Koronellou): Add components and functionality to history.
        if (historyButton.getStyle().equals(SELECTED_TAB_STYLE)) return;
        notifButton.setStyle(SECONDARY_TAB_STYLE);
        historyButton.setStyle(SELECTED_TAB_STYLE);
        loadPage("HistoryPage.fxml");
    }

    /** Loader of Notifications page. 
     * @param event The event.
    */
    @FXML
    public void loadNotifications(ActionEvent event) {
        if (notifButton.getStyle().equals(SELECTED_TAB_STYLE)) return;
        notifButton.setStyle(SELECTED_TAB_STYLE);
        historyButton.setStyle(SECONDARY_TAB_STYLE);
        loadPage("NotificationsPage.fxml");
    }

    /** Helper for loading the specified page.
     * @param pageName The page to be loaded.
     */
    private void loadPage(String pageName) {
        try {
            //Load the FXML file for the selected page.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + pageName));
            Parent pageRoot = loader.load();
            ///Set the content area with the loaded page
            miscContentArea.getChildren().clear();
            miscContentArea.getChildren().add(pageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

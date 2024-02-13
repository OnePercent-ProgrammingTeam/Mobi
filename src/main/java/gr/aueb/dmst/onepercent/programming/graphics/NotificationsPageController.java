package gr.aueb.dmst.onepercent.programming.graphics;

import java.nio.file.Path;
import java.nio.file.Paths;

import gr.aueb.dmst.onepercent.programming.gui.MonitorGUI;

import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

/**
 * Controller class for the Notifications page of the application.
 * 
 * <p>This page displays notifications retrieved from the Docker System.
 * It accesses system information via the Docker Engine API and presents warnings to the user.
 */
public class NotificationsPageController {

    @FXML
    private VBox notifContentArea;

    /** Default constructor. */
    public NotificationsPageController() { }

    /** Initializer of the page. */
    @FXML
    public void initialize() {
        setupNotifications();
    }

    /**
     * Sets up the Notifications, based on a warning list that is provided
     * from the Docker System.
     * 
     * <p>These info is accessed via Docker Engine API.
     */
    private void setupNotifications() {
        MonitorGUI monitor = new MonitorGUI(); 
        monitor.systemInfo();
        StringBuilder warnings = monitor.getWarnings();
        String[] warningList = warnings.toString().split("\n");
        // Access the warnings and customize them.
        for (int i = 0; i < warningList.length; i++) {
            warningList[i] = warningList[i].replace("WARNING: ", "");
            //Add image to the icons.
            String path = "src\\main\\resources\\images\\notificationsPage\\dangerIcon.png";
            Path imagePath = Paths.get(path);
            Image image = new Image(imagePath.toUri().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
            
            Text titleText = new Text("Warning");
            titleText.setStyle("-fx-font-size: 15px;"
                .concat("-fx-font-weight: bold; -fx-fill: #000;"));            
            Text descText = new Text(warningList[i]);
            descText.setStyle("-fx-font-size: 12px; -fx-fill: #555;");
            VBox titleDescBox = new VBox(titleText, descText);
            titleDescBox.setSpacing(5);
            titleDescBox.setMinWidth(700);
            titleDescBox.setMaxWidth(700);
            HBox item = new HBox(imageView, titleDescBox);
            item.setSpacing(30);
            item.setPadding(new Insets(20, 20, 20, 20));
            item.setStyle("-fx-background-color: #fff; -fx-background-radius: 20;");
            item.setMinHeight(70);
            item.setMaxHeight(70);
            item.setMinWidth(930);
            item.setMaxWidth(930);
            item.setAlignment(Pos.CENTER_LEFT);
            notifContentArea.getChildren().add(item);
        }
    }
}

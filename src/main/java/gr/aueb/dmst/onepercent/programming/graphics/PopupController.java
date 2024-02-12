package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.util.Duration;

/**
 * Controller class for handling the pop-up message shown when a user attempts to simultaneously
 * select two containers in the Analytics page to view CPU and memory usage diagrams.
 * 
 * This pop-up message alerts the user about the limitation of selecting only 
 * one container at a time.
 * 
 * Additional implementations may be added to handle exceptional cases where exceptions occur.
 */
public class PopupController {

    /** The pop up to be appeared.*/
    @FXML
    private BorderPane popupWindow;

    /** The text of the pop up indicating the problem that has occured. */
    @FXML
    private Text popupErrorText;
    
    /** Axis X offset. */
    private double xOffset = 0;

    /** Axis Y offset. */
    private double yOffset = 0;

    /** Default Constructor. */
    public PopupController() { }

    /** Initliazes and add functionalities.*/
    @FXML
    void initialize() {
        popupWindow.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        popupWindow.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /** Closes pop up.  */
    @FXML
    void closePopup(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Show the pop up.
     * @param event The event to be executed.
     */
    public void showPopup(ActionEvent event) {
        //TO DO(Scobioala - Selar): Think if it can be shown when any kind of exceptions occur.
        Stage errorStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double x = errorStage.getX();
        double y = errorStage.getY();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Popup.fxml"));
            Parent root = loader.load();
            ScaleTransition st = new ScaleTransition(Duration.millis(500), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);

            Stage stage = new Stage();
            stage.setTitle("Error");
            Scene scene = new Scene(root, 500, 200);
            scene.setFill(null);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            stage.setX(x + 400);
            stage.setY(y + 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter for the error text.
     * @param message The message to be displayed.
     */
    public void setErrorMessage(String message) {
        popupErrorText.setText(message);
    }
}

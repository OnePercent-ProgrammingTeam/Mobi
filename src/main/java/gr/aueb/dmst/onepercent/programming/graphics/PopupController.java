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

public class PopupController {

    @FXML
    private BorderPane popupWindow;

    @FXML
    private Text popupErrorText;
    
    private double xOffset = 0;
    private double yOffset = 0;
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


    @FXML
    void closePopup(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void showPopup(ActionEvent event) {
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

    public void setErrorMessage(String message) {
        popupErrorText.setText(message);
    }
}

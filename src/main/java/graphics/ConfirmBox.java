package graphics;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class ConfirmBox {

    /** Method: display(String, String) creates a confirmation box. 
     * This box appears when the user presses the X button.
     * @param title is the title of the confirmation box.
     * @param message is the message of the confirmation box.
     * @return true if the user presses OK, false otherwise.
     */
    public static boolean display(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}

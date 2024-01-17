package gr.aueb.dmst.onepercent.programming.graphics;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
        primaryStage.setTitle("One Percent");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setResizable(false); //does not allow the user to resize the window
        primaryStage.initStyle(StageStyle.DECORATED.UNDECORATED);
        primaryStage.show();
    }
}

package gr.aueb.dmst.onepercent.programming.graphics;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
        primaryStage.setTitle("One Percent");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}

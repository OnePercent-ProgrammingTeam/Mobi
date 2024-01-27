package gr.aueb.dmst.onepercent.programming.graphics;
import java.nio.file.Path;
import java.nio.file.Paths;

import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * ok
 */
public class MainGUI extends Application {
    /** ok */
    public MainGUI() { }
    static Stage window;

    static MenuThreadGUI menuThreadGUI;

    /**
     * ok
     * @param args ok
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        createMenuThread();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
        String style = "src\\main\\resources\\styles.css";
        Path stylepath = Paths.get(style);
        root.getStylesheets().add(getClass().
            getResource("/styles.css").toExternalForm());
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
        window = primaryStage;
        window.setTitle("One Percent");
        Scene scene = new Scene(root, 1300, 700);
        scene.setFill(null);
        window.setScene(scene);
        window.setResizable(false); //does not allow the user to resize the window
        window.initStyle(StageStyle.UNDECORATED);
        

        window.show();
    }

    /** Method: createMenuThread() is the method that creates the thread
     *  that runs the menu. This thread is the underlying power of the
     *  application.
     */
    private void createMenuThread() {
        menuThreadGUI = new MenuThreadGUI();
        Thread thread = new Thread(menuThreadGUI);
        thread.start();
    }
}

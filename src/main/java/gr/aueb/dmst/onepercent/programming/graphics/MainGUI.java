package gr.aueb.dmst.onepercent.programming.graphics;

import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** The Main class runs the graphical-user interface (GUI) of the application. */
public class MainGUI extends Application {
    
    /** The main stage-window of the graphical application. */
    static Stage window;

    /** An instance of the menu thread. */
    static MenuThreadGUI menuThreadGUI;

    /** Default constructor. */
    public MainGUI() { }

    /**
     * The main method of the application.
     * This method serves as the entry point for the GUI application.
     * @param args The command-line arguments passed to the application (used for launching GUI).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the GUI of the application.
     */
    public void start(Stage primaryStage) throws Exception {
        createMenuThread();
        /* Loads the Login Page which is always the first page to be introduced.  */
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
        String style = "src\\main\\resources\\styles.css";
        Path stylepath = Paths.get(style);
        root.getStylesheets().add(getClass().
            getResource("/styles.css").toExternalForm());
        window = primaryStage;
        window.setTitle("Mobi");
        //Window has fixed dimensions.
        Scene scene = new Scene(root, 1300, 700);
        scene.setFill(null);
        window.setResizable(false); //Do not allow the user to resize the window
        window.initStyle(StageStyle.TRANSPARENT);
        window.setScene(scene);
        window.show();
    }

    /** 
     *  Spawns the menu thread.
     *  
     *  <p>This thread is the underlying power of the application.
     */
    private void createMenuThread() {
        menuThreadGUI = new MenuThreadGUI();
        Thread thread = new Thread(menuThreadGUI);
        thread.start();
    }
}

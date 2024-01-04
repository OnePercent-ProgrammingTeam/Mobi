package graphics;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import javafx.scene.control.Button;
import gr.aueb.dmst.onepercent.programming.MenuThread;
//import javafx.scene.control.TreeView;
/** Class: GUI is the core class that runs the Graphics of
 * the application. It uses the Intro and MainPage classes
 * @see Intro
 * @see MainPage
 */
public class GUI extends Application {

    static Stage window;
    static MenuThread menuThread;

    /** Method: start(Stage) is the method that runs the application.
     * @param window is the stage of the application.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
       
        createMenuThread();

        window = stage;
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        // WINDOW CREATION
        window.setTitle("Mobi");
        StackPane introLayout = new StackPane();
        introLayout.setAlignment(Pos.TOP_CENTER);
       
        Intro introPage = new Intro();
        MainPage mainPage = new MainPage();
        ListPane list = new ListPane();
        
        
        // SCENES CREATION
        Scene introScene = introPage.createIntroScene(introLayout);
        
        BorderPane borderPane = new BorderPane();
        Scene mainScene = mainPage.createMainScene(borderPane);
        
        // BUTTON CREATION
        Button startButton = introPage.createStartButton(introLayout);
        startButton.setOnAction(e -> window.setScene(mainScene));
        
        //TEXT CREATION
        Text[] textNodes = introPage.createText();
        introPage.formatText(textNodes, introLayout);

        // IMAGE CREATION
        String path = "C:/Users" +
                        "/scobi" +
                        "/OneDrive" +
                        "/Έγγραφα" +
                        "/UNIVERSITY" +
                        "/AUEB" +
                        "/Semester 3" +
                        "/Programming II" +
                        "/Assignment" +
                        "/hellofx" +
                        "/src" +
                        "/main" +
                        "/resources" + "/org/openjfx" +
                        "/containerwhales.png";
        introPage.setImage(path, introLayout);


    

        // Create list of containers in the center of the main page
        VBox vbox = list.createList();
        borderPane.setCenter(vbox);

        // Create the tree menu on the left of the main page
        Tree treeobj = new Tree();
        
        VBox menu = treeobj.createTree(list);
        borderPane.setLeft(menu);

        window.setScene(introScene); 
        window.show();
    }

    /** Method: setRoot(String) is the method that sets the root of the
     * application.
     * @param fxml is the fxml file that is going to be loaded.
     * @throws IOException
     */
    static void setRoot(String fxml) throws IOException {
        //introScene.setRoot(loadFXML(fxml));
    }

    /** Method: loadFXML(String) is the method that loads the fxml file.
     * @param fxml is the fxml file that is going to be loaded.
     * @return the loaded fxml file.
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public void closeProgram() {
        Boolean answer = ConfirmBox.display("Exit", "Sure you want to exit?");
        
        if (answer && window != null)  // Check for null before calling close()
             window.close();
    }

    public static void main(String[] args) {
        //System.out.println(javafx.scene.text.Font.getFamilies());
        launch(args);
    }

    private void createMenuThread() {
        menuThread = new MenuThread();
        Thread thread = new Thread(menuThread);
        thread.start();
    }

}

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

/** Class: GUI is the core class that runs the Graphics of
 * the application. It uses the Intro and MainPage classes.
 * @see Intro
 * @see MainPage
 */
public class GUI extends Application {

    static Stage window;
    static MenuThread menuThread;
    static Scene imagesScene;
    Scene introScene;
    Scene mainScene;
    static final ListPane LIST = new ListPane();

    /** Method: start(Stage) is the method that runs the application.
     * @param window is the stage of the application.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
       
        createMenuThread();

        window = stage;
        window.setTitle("Mobi");
        closeProgramBox(window);
        
        /* Create the intro scene.*/
        createIntroScene();
        
        /* Create the main page.*/
        MainPage mainPage = new MainPage();
        BorderPane mainBorderPane = new BorderPane();
        mainScene = mainPage.createMainScene(mainBorderPane);
        createBasicScene(mainPage, mainBorderPane);
        
        /* Create list of containers in the center of the main page.*/
        VBox vbox = LIST.createList();
        mainBorderPane.setCenter(vbox);
        
        /* Create the info page.*/
        BorderPane imagesBorderPane = new BorderPane();
        imagesScene = mainPage.createMainScene(imagesBorderPane);
        createBasicScene(mainPage, imagesBorderPane);
        
        window.setScene(introScene); 
        window.show();
    }

    /** Method: createMenuThread() is the method that creates the thread
     *  that runs the menu. This thread is the underlying power of the
     *  application.
     */
    private void createMenuThread() {
        menuThread = new MenuThread();
        Thread thread = new Thread(menuThread);
        thread.start();
    }

    private void closeProgramBox(Stage window) {
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
    }

    /** Method: closeProgram() is the method that closes the application
     *  when the user presses the X button.
     */
    public void closeProgram() {
        Boolean answer = ConfirmBox.display("Exit", "Sure you want to exit?");
        
        if (answer && window != null)  // Check for null before calling close()
             window.close();
    }

    private void createIntroScene() {
        StackPane introLayout = new StackPane();
        introLayout.setAlignment(Pos.TOP_CENTER);
        Intro introPage = new Intro();
        introScene = introPage.createIntroScene(introLayout);
        /* Create the button that starts the app (changes the page from
         * the intro to the main).*/
        Button startButton = introPage.createStartButton(introLayout);
        startButton.setOnAction(e -> window.setScene(mainScene));
        /* Create the texts of the intro page.*/
        Text[] textNodes = introPage.createText();
        introPage.formatText(textNodes, introLayout);
        /* Set image in the intro page.*/
        String path = "src\\main\\resources\\containerwhales.png";
        introPage.setImage(path, introLayout);
    }

    private void createBasicScene(MainPage mainPage, BorderPane borderPane) {
        
        borderPane.setStyle("-fx-background-color: #E8E9EB;");
        /*  Create the tree menu on the left of the main page. */
        Tree treeobj = new Tree();
        VBox menu = treeobj.createTree(LIST);
        borderPane.setLeft(menu);
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
    
    
    public static void main(String[] args) {
        //System.out.println(javafx.scene.text.Font.getFamilies());
        launch(args);
    }



}

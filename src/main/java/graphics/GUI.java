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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.nio.file.Path;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.nio.file.Paths;



import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;


import gr.aueb.dmst.onepercent.programming.MenuThreadGUI;

/** Class: GUI is the core class that runs the Graphics of
 * the application. It uses the Intro and MainPage classes.
 * @see Intro
 * @see MainPage
 */
public class GUI extends Application {

    static Stage window;
    static MenuThreadGUI menuThreadGUI;
    static Scene imagesScene;
    Scene introScene;
    static Scene mainScene;
    static Scene analyticsScene;
    static Scene helpScene;
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
        
        /* Create the image page.*/
        ImagePage imagePage = new ImagePage();
        BorderPane imagesBorderPane = new BorderPane();
        imagesScene = mainPage.createMainScene(imagesBorderPane);
        createBasicScene(mainPage, imagesBorderPane);
        /* Create list of containers in the center of the main page.*/
        VBox vboxImages = imagePage.createList();
        imagesBorderPane.setCenter(vboxImages);

        HelpPage helpPage = new HelpPage();
        BorderPane helpBorderPane = new BorderPane();
        helpBorderPane.setStyle("-fx-background-color: #E8E9EB;");
        helpScene = helpPage.createHelpScene(helpBorderPane);
        createBasicScene(mainPage, helpBorderPane);
        Stage helpStage = new Stage();
        helpStage.setTitle("Tips for running our app successfully.");
    
        VBox helpPageVbox = new VBox();
        helpPageVbox.setStyle("-fx-background-color: #E8E9EB;");

        TextFlow textFlow = new TextFlow();
        String style = "-fx-font-size: 20px; -fx-font-family: 'Arial';";

        Text text1 = new Text("About Mobi - Docker Container Management Dashboard");
        text1.setStyle("-fx-font-size: 30px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);

        String string = "Welcome to Mobi, your intuitive Docker container management dashboard.\n" +
                        "This application simplifies the process of interacting with Docker " + 
                        "containers and images,\n" +
                    "providing you with a user-friendly interface to streamline your workflow.\n\n";

        text1 = new Text(string);
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(50);

        text1 = new Text("Navigation\n");
        text1.setStyle("-fx-font-size: 30px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);

        text1 = new Text("Containers\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Start: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        Text text2 = new Text("Initiates the selected container.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        

        text1 = new Text("Stop: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Halts the selected container.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Get Information: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Displays detailed information about the chosen container.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(20);

        text1 = new Text("Images\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(20);

        text1 = new Text("Search: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Enables searching Docker Hub for images. " + 
        "Top 3 results are displayed below the list\nof locally installed images.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Pull: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Downloads a image to your local environment. You have to type its " +
         "exact name.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Analytics\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(20);

        text1 = new Text("CPU Usage: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Visualizes the CPU usage of a running " +
            "container through plotted diagrams.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Help\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);

        text1 = new Text("About: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Provides details about the application and its usage. Includes system\n" +
                "requirements and a user manual.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Exit Mobi: ");
        text1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold;");
        text2 = new Text("Prompts a confirmation window before exiting the application.");
        text2.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().addAll(text1, text2);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(10);

        text1 = new Text("Getting Started");
        text1.setStyle("-fx-font-size: 30px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);

        text1 = new Text("Containers Page\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);

        text1 = new Text("Navigate to the \"Containers\" section to manage your local containers." +
        "\nChoose a container from the list and use the buttons to start, stop," + 
        "or gather information.");
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);

        text1 = new Text("Images Page\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);
        
        text1 = new Text("Explore the \"Images\" section to view and manage your " +
        "local and Docker Hub images.\nUse the search bar to find Docker Hub images and pull" + 
        " them to your local environment.");
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);

        text1 = new Text("Analytics Page\n");
        text1.setStyle("-fx-font-size: 25px; -fx-font-family: 'Arial'; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);

        text1 = new Text("Visit the \"Analytics\" section to view CPU usage diagrams " +
                         "for running containers.");
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);

        text1 = new Text("System Requirements\n");
        text1.setStyle("-fx-font-size: 30px; -fx-font-family: 'Monotype '; -fx-font-weight: bold;");
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(60);
//--------------------------------------
        text1 = new Text("Conditions to run our Program.\n");
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        text1 = new Text("Do all the following steps:\n");
        text1.setStyle(style);
        textFlow.getChildren().add(text1);
        text1 = new Text("1) Open Docker Desktop\n");
        text1.setStyle(style);
        textFlow.getChildren().add(text1);
        text1 = new Text("2) Navigate to Settings -> General\n");
        text1.setStyle(style);
        textFlow.getChildren().add(text1);
        
        
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);

        
        String path = "src\\main\\resources\\images\\helpPage1.png";
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(600);
        imageView.setFitWidth(1100);
        helpPageVbox.getChildren().add(imageView);
        helpPageVbox.setSpacing(30);

 
       
        text1 = new Text("3) Enable Expose daemon on ");
        text1.setStyle(style);
        textFlow = new TextFlow();
        text2 = new Text("tcp://localhost:2375 ");
        text2.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';" + 
            " -fx-font-weight: bold; -fx-fill: blue;");
        Text text3 = new Text("without TLS\n");
        text3.setStyle(style);
        textFlow.getChildren().addAll(text1, text2, text3);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);
 
        path = "src\\main\\resources\\images\\helpPage2.png";
        imagePath = Paths.get(path);
        image = new Image(imagePath.toUri().toString());
        ImageView imageView2 = new ImageView(image);
        imageView2.setFitHeight(600);
        imageView2.setFitWidth(1100);
        helpPageVbox.getChildren().add(imageView2);
        helpPageVbox.setSpacing(30);
  
        text1 = new Text("4) Then, press the Button Apply and Restart\n");
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(30);
        
        path = "src\\main\\resources\\images\\helpPage3.png";
        imagePath = Paths.get(path);
        image = new Image(imagePath.toUri().toString());
        ImageView imageView3 = new ImageView(image);
        imageView3.setFitHeight(600);
        imageView3.setFitWidth(1100);
        helpPageVbox.getChildren().add(imageView3);
        helpPageVbox.setSpacing(30);


        text1 = new Text("Congratulations, you're ready to use Mobi! Enjoy:)\n");
        text1.setStyle(style);
        textFlow = new TextFlow();
        textFlow.getChildren().add(text1);
        textFlow.setStyle("-fx-background-color: #E8E9EB;");
        helpPageVbox.getChildren().add(textFlow);
        helpPageVbox.setSpacing(50);    
    
  
        helpPageVbox.setSpacing(50);
        
        
        helpPageVbox.setPadding(new javafx.geometry.Insets(50, 100, 0, 100));
        
        
        ScrollPane helpScrollPane = new ScrollPane(helpPageVbox);
        
        helpScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        helpScrollPane.setStyle("-fx-background-color: #E8E9EB;");
        
        helpBorderPane.setCenter(helpScrollPane);
        //helpBorderPane.setRight(helpScrollPane);
        helpBorderPane.setPrefSize(2000, 2000);

        window.setScene(introScene); 
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
        String path = "src\\main\\resources\\images\\containerwhales.png";
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        
        ImageView imageView = new ImageView(image);

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

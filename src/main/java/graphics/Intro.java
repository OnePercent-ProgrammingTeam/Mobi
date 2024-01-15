package graphics;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.Button;


/** Class: Intro is the class that creates the intro page of the GUI. */
public class Intro {

    //protected static StackPane introayout;

    /** Method: createText() creates the text for the intro page of the GUI
     *  The text contains the title of the application, a greeting message,
     *  the operating system on which the application is running and a list of the
     *  functionalities of the application.
     *  @return textNodes: the array of textNodes that are created
     */
    public Text[] createText() {
        Text title = new Text("Mobi");
        Text greeting = new Text("Welcome to Mobi - your Docker Companion! ");
        Text system = new Text("You are running Mobi on: " + 
                                System.getProperty("os.name").toLowerCase());
        Text textNode1 = new Text("Mobi offers a suite of powerful tools allowing you to:");
        Text textNode2 = new Text("1.Manage Containers");
        Text textNode3 = new Text("2.Manage Images");
        Text textNode4 = new Text("3.Container Analytics");       
        
        /*NOTE FOR PROGRAMMERS: the order of the textNodes in the array is the order 
        they will be displayed in the GUI*/
        Text[] textNodes = {title, greeting, system, textNode1, textNode2, textNode3, textNode4};

        return textNodes;
    }
    
    /** Method: formatText(Text[], StackPane) formats the text in a modern and good-looking manner.
     * @param textNodes: the array of textNodes to be formatted.
     * @param root: the StackPane (layout) in which the textNodes will be placed.
     */
    public void formatText(Text[] textNodes, StackPane introLayout) {
        if (textNodes.length > 0) {
            textNodes[0].setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, 50)); // book old ..
            textNodes[0].setStyle("-fx-fill: white;");
            introLayout.getChildren().add(textNodes[0]);
            StackPane.setAlignment(textNodes[0], Pos.TOP_CENTER);
            StackPane.setMargin(textNodes[0], new javafx.geometry.Insets(40, 0, 0, 0));
            for (int i = 1; i < textNodes.length; i++) {
                textNodes[i].setFont(Font.font("Arial", 20));
                textNodes[i].setStyle("-fx-fill: white;");
                
                StackPane.setAlignment(textNodes[i], Pos.CENTER);
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
    
                /* Arrange the text in the center of the intro page as wanted.*/
                if (i == 1) {
                    bottom = 100;
                }
                if (i == 2) {
                    top = i * 40;
                    left = 60;
                } else if (i == 4) {
                    top = i * 40; 
                    right = 30;
                } else {
                    top = i * 40;
                    left = 0;
                }
                
                StackPane
                .setMargin(textNodes[i], new javafx.geometry.Insets(top, right, bottom, left));
                
                introLayout.getChildren().add(textNodes[i]);
            }
            
        }
    }

    public void format(Text[] textNodes, StackPane introLayout) {
        VBox vbox = new VBox();
        if (textNodes.length > 0) {
            textNodes[0].setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, 50)); // book old ..
            textNodes[0].setStyle("-fx-fill: white;");
            introLayout.getChildren().add(textNodes[0]);
            for (int i = 1; i < textNodes.length; i++) {
                textNodes[i].setFont(Font.font("Arial", 20));
                textNodes[i].setStyle("-fx-fill: white;");
                vbox.getChildren().add(textNodes[i]);
                vbox.setSpacing(20);
                
            }
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new javafx.geometry.Insets(100, 0, 0, 0));
        }
        introLayout.getChildren().add(vbox);
    }



    /** Method: setImage(String, StackPane) formats the image - whale 
     *  logo that is dislayed in the intro page of the GUI, next to 
     *  the title "Mobi". 
     *  @param path: the path of the image
     *  @param introLayout: the StackPane (layout) in which the image will be placed
     */
    public void setImage(String path, StackPane introLayout) {
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        StackPane.setAlignment(imageView, Pos.TOP_CENTER);
        StackPane.setMargin(imageView, new javafx.geometry.Insets(70, 0, 0, 0));
        introLayout.getChildren().add(imageView);
        introLayout.setStyle("-fx-background-color: linear-gradient(to left, #037ADF, #2A2A72);");
    }

    /** Method: createStartButton(StackPane) creates the start button
     *  that is displayed in the intro page of the GUI. When pressed,
     *  it takes the user to the main page of the GUI. The action 
     *  functionality takes place in the GUI class.
     *  @param introLayout: the StackPane (layout) in which the button will be placed
     *  @return startButton: the button that is created
     */
    public Button createStartButton(StackPane introLayout) {
        Button startButton = new Button("Start");
        
        startButton
            .setStyle("-fx-background-color: #3a3a9d;" + 
                      "-fx-text-fill: #e2e2f4;" +
                      "-fx-font-size: 20px;" + 
                      "-fx-font-weight: bold;");
        
        StackPane.setAlignment(startButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(startButton, new javafx.geometry.Insets(0, 30, 30, 0));
        introLayout.getChildren().add(startButton);
        return startButton;
    }

    /** Method: createIntroScene(StackPane) creates the intro scene
     *  of the GUI. It is the first scene that the user sees when
     *  the application is launched.
     *  @param introLayout: the StackPane (layout) in which the scene will be placed
     *  @return introScene: the scene that is created
     */
    public Scene createIntroScene(StackPane introLayout) {
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        Scene introScene = new Scene(introLayout, bounds.getWidth(), bounds.getHeight());
        return introScene;
    }

    /**
     * Default constructor
     */
    public Intro() {
    } 
}


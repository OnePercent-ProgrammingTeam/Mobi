package graphics;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.Button;



public class Intro {

    //protected static StackPane introayout;

    /** Method: createText() creates the text for the intro page of the GUI */
    public Text[] createText() {
        Text title = new Text("Mobi");
        Text greeting = new Text("Welcome to Mobi - your Docker Companion! ");
        Text system = new Text("You are running Mobi on: " + 
                                System.getProperty("os.name").toLowerCase());
        Text textNode1 = new Text("Mobi offers a suite of powerful tools allowing you to:");
        Text textNode2 = new Text("1.Manage Containers");
        Text textNode3 = new Text("2.Manage Images");
        Text textNode4 = new Text("3.Container Analytics");       
        
        /*Note for programmers: the order of the textNodes in the array is the order 
        they will be displayed in the GUI*/
        Text[] textNodes = {title, greeting, system, textNode1, textNode2, textNode3, textNode4};

        return textNodes;
    }
    
    /** Method: formatText(Text[], StackPane) formats the text in a modern and good-looking manner.
     * @param textNodes: the array of textNodes to be formatted
     * @param root: the StackPane (layout) in which the textNodes will be placed
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

    /** Method: setImage(String, StackPane)  */
    public void setImage(String path, StackPane introLayout) {
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        
        ImageView imageView = new ImageView(image);
        StackPane.setAlignment(imageView, Pos.TOP_CENTER);
        StackPane.setMargin(imageView, new javafx.geometry.Insets(40, 0, 0, 160));
        introLayout.getChildren().add(imageView);
        introLayout.setStyle("-fx-background-color: linear-gradient(to left, #037ADF, #2A2A72);");
    }

    public Button createStartButton(StackPane introLayout) {
        Button startButton = new Button("Start");
        
        startButton
            .setStyle("-fx-background-color: #2A2A72;" + 
                      "-fx-text-fill: white;" + 
                      "-fx-font-size: 20px;" +  
                      "-fx-font-weight: bold;");
        
        StackPane.setAlignment(startButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(startButton, new javafx.geometry.Insets(0, 30, 30, 0));
        introLayout.getChildren().add(startButton);
        return startButton;
    }

    public Scene createIntroScene(StackPane introLayout) {
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        Scene introScene = new Scene(introLayout, bounds.getWidth(), bounds.getHeight());
        return introScene;
    }
} 



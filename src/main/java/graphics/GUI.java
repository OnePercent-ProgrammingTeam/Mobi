package org.openjfx;
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

/**
 * JavaFX App
 */
public class GUI extends Application {

    @Override
    public void start(Stage window) throws IOException {
       
        // WINDOW CREATION
        window.setTitle("Mobi");
        StackPane introLayout = new StackPane();
        introLayout.setAlignment(Pos.TOP_CENTER);
       
        Intro introPage = new Intro();
        MainPage mainPage = new MainPage();
        
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
        String path = "C:/Users/scobi/OneDrive/Έγγραφα/UNIVERSITY/AUEB/Semester 3/Programming II/Assignment/hellofx/src/main/resources/org/openjfx/whale.png";
        introPage.setImage(path, introLayout);

        // MENU CREATION
        VBox menu = mainPage.createMenu();
        borderPane.setLeft(menu);
        
        window.setScene(introScene); 
        window.show();
    }

    static void setRoot(String fxml) throws IOException {
        //introScene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        //System.out.println(javafx.scene.text.Font.getFamilies());
        launch(args);
    }

}
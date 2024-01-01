package org.openjfx;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Screen;


public class MainPage {
    
    public Button[] createButtons() {
        Button containers = new Button("Containers");
        Button images = new Button("Images");
        Button analytics = new Button("Analytics");

        containers.setStyle("-fx-background-color: #2A2A72; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        images.setStyle("-fx-background-color: #2A2A72; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        analytics.setStyle("-fx-background-color: #2A2A72; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
    
        Button[] buttons = {containers, images, analytics};

        return buttons;
    }

    public VBox createMenu() {
        
        Button[] buttons = createButtons();
        
        Button containers = buttons[0];
        Button images = buttons[1];
        Button analytics = buttons[2];
        
        VBox menu = new VBox(30);
        menu.getChildren().addAll(containers, images, analytics);

        menu.setStyle("-fx-background-color: #2A2A72;");
        return menu;
    }

    public Scene createMainScene(BorderPane borderPane) {
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        Scene mainScene = new Scene(borderPane, bounds.getWidth(), bounds.getHeight());
        return mainScene;
    }

}

package graphics;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

public class HelpPage {
    
    /** Method: createMainScene(BorderPane) creates the main scene of the GUI.
     *  @param borderPane: the BorderPane (layout) in which the scene will be placed
     *  @return mainScene: the scene that is created
     */
    public Scene createHelpScene(BorderPane borderPane) {
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        Scene helpScene = new Scene(borderPane, bounds.getWidth(), bounds.getHeight());
        return helpScene;
    }

    public void createHelpPageMenu() {
        
    }

}


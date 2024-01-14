package graphics;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

/**
 * Class: HelpPage is the class that creates the help page of the GUI.
 * The help page contains information about the usage of the GUI and the 
 * requirements for it to work properly.
 */
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

    /**
     * Creating of help page menu in the app
     * Not yet built
     */
    public void createHelpPageMenu() {
        
    }

    /**
     * Defualt Constructor
     */
    public HelpPage() {

    }

}


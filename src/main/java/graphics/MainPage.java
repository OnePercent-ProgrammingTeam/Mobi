package graphics;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Screen;

/** Class: MainPage is the class that creates the main page of the GUI. */
public class MainPage {
    
    /** Method: createButtons() creates the buttons that lead to the functionalities
     *  of the application.
     *  @return buttons: the array of buttons that are created
     */
    public Button[] createButtons() {
        Button containers = new Button("Containers");
        Button images = new Button("Images");
        Button analytics = new Button("Analytics");
        Button help = new Button("Help");
        Button close = new Button("Close Program");

        String style = "-fx-background-color: #2A2A72;" +
                       "-fx-text-fill: white;" +
                       "-fx-font-size: 20px;" + 
                       "-fx-font-weight: bold;";
        containers.setStyle(style);
        images.setStyle(style);
        analytics.setStyle(style);
        
        help.setStyle(style);

        close.setStyle(style);
        close.setOnAction(e -> closeProgram());
        
        Button[] buttons = {containers, images, analytics, help, close};

        return buttons;
    }

    /** Method: createMenu() creates the menu of the main page of the GUI.
     *  @return menu: the VBox (layout) that is created
     */
    public VBox createMenu(Button[] buttons) {
        
        Button containers = buttons[0];
        Button images = buttons[1];
        Button analytics = buttons[2];
        Button help = buttons[3];
        Button close = buttons[4];

        
        VBox.setMargin(containers, new javafx.geometry.Insets(70, 0, 0, 75));
        VBox.setMargin(images, new javafx.geometry.Insets(0, 0, 0, 75));
        VBox.setMargin(analytics, new javafx.geometry.Insets(0, 0, 0, 75));
        VBox.setMargin(help, new javafx.geometry.Insets(0, 0, 0, 75));
        VBox.setMargin(close, new javafx.geometry.Insets(300, 0, 0, 75));

        VBox menu = new VBox(30);
        menu.getChildren().addAll(containers, images, analytics, help, close);

        menu.setStyle("-fx-background-color: #2A2A72;");
        menu.setPrefWidth(300);
        return menu;
    }

    /** Method: createMainScene(BorderPane) creates the main scene of the GUI.
     *  @param borderPane: the BorderPane (layout) in which the scene will be placed
     *  @return mainScene: the scene that is created
     */
    public Scene createMainScene(BorderPane borderPane) {
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        Scene mainScene = new Scene(borderPane, bounds.getWidth(), bounds.getHeight());
        return mainScene;
    }

    public void closeProgram() {
        Boolean answer = ConfirmBox.display("Exit", "Sure you want to exit?");
        
        if (answer && GUI.window != null)  // Check for null before calling close()
             GUI.window.close();
    }
    



}

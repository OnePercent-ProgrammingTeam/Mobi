package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import java.nio.file.Path;
import java.nio.file.Paths;

import exceptions.UserExistsException;
import exceptions.UserNotFoundException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/** Class: Intro is the class that creates the intro page of the GUI. */
public class Intro {

    Button startButton;

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
     * @param introLayout: the StackPane (introLayout) in which the textNodes will be placed.
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

//teh same method with the above?
     
    /** Method: format(Text[], StackPane) formats the text in a modern and good-looking manner.
     * @param textNodes: the array of textNodes to be formatted.
     * @param introLayout: the StackPane (layout) in which the textNodes will be placed.
     */
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
        startButton = new Button("Start");
        
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

    /** Method: createLogButton() creates the log button
     *  that is displayed in the intro page of the GUI. When pressed,
     *  it opens a window that make the user to log in. The action 
     *  functionality takes place in the GUI class.
     *  @return logButton: the button that is created
     */
    public Button createLogButton() {
        Button logButton = new Button("Log in");
        logButton
            .setStyle("-fx-background-color: #2A2A72;" + 
                      "-fx-text-fill: white;" + 
                      "-fx-font-size: 15px;" +  
                      "-fx-font-weight: bold;");
        return logButton; 
    }

    /** Method: createSignButton() creates the sign button
     *  that is displayed in the intro page of the GUI. When pressed,
     *  it opens a window that make the user to sign up. The action 
     *  functionality takes place in the GUI class.
     *  @return signButton: the button that is created
     */
    public Button createSignButton() {
        Button signButton = new Button("Sign up");
        signButton
            .setStyle("-fx-background-color: #2A2A72;" + 
                      "-fx-text-fill: white;" + 
                      "-fx-font-size: 15px;" +  
                      "-fx-font-weight: bold;");
        return signButton; 
    }

    Label labelsign;
    Label labellog;

    /** Method: openLoginWindow() opens the log in window
     *  that is displayed in the intro page of the GUI. 
     */
    private void openLoginWindow() {
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setTitle("Log In");

        VBox loginLayout = new VBox(20);
        GridPane grid = getUserWindows("Log in", true);
        grid.setAlignment(Pos.CENTER);

        StackPane stacklog = new StackPane();
        labellog = new Label("");
        labellog.setStyle("-fx-text-fill: red;");
        stacklog.getChildren().add(labellog);


        loginLayout.getChildren()
            .addAll(grid, stacklog); // Add your sign-up form components here
        loginLayout.setAlignment(Pos.CENTER);

        Scene loginScene = new Scene(loginLayout, 500, 300);
        loginStage.setScene(loginScene);
        loginStage.showAndWait();
    }

    /** Method: openSignupWindow() opens the sign up window
     *  that is displayed in the intro page of the GUI. 
     */
    private void openSignupWindow() {
        Stage signupStage = new Stage();
        signupStage.initModality(Modality.APPLICATION_MODAL);
        signupStage.setTitle("Sign Up");

        VBox signupLayout = new VBox(20);
        GridPane grid = getUserWindows("Sign up", false);
        grid.setAlignment(Pos.CENTER);

        StackPane stacksign = new StackPane();
        labelsign = new Label("");
        labelsign.setStyle("-fx-text-fill: red;");
        stacksign.getChildren().add(labelsign);


        signupLayout.getChildren()
            .addAll(grid, stacksign); // Add your sign-up form components here
        signupLayout.setAlignment(Pos.CENTER);


        Scene signupScene = new Scene(signupLayout, 500, 300);
        signupStage.setScene(signupScene);
        signupStage.showAndWait();
    }

    /** Method: createButtons() creates the log and sign buttons
     *  that is displayed in the intro page of the GUI in the form of HBox.
     * @param introLayout the StackPane (layout) in which the buttons will be placed
     * @param signButton The button for the sign up
     * @param logButton The button for the log in
     */
    public void createButtons(StackPane introLayout, Button signButton, Button logButton) {
        HBox hbox = new HBox(30);
        hbox.getChildren().addAll(logButton, signButton);
        hbox.setMinWidth(500);
        // Add functionality to the button
        signButton.setOnAction(event -> {
            openSignupWindow();
        });

         // Add functionality to the button
        logButton.setOnAction(event -> {
            openLoginWindow();
        });

        StackPane.setAlignment(hbox, Pos.TOP_RIGHT);
        StackPane.setMargin(hbox, new javafx.geometry.Insets(10, 20, 0, 0));

        introLayout.getChildren().add(hbox);            

    }

    DataUsers userTable = new DataUsers();
    boolean key;

    /** Method: getUserWindows() creates the GridPane 
     *  that is displayed after the press of the buttons log in or sign up
     * @param string The name of the button
     * @param flag The flag shows if the start button is visible or not
     * @return grid The GridPane of the windows
     */
    public GridPane getUserWindows(String string, boolean flag) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.setVgap(8);
        grid.setHgap(10);

        //name label
        Label name = new Label("      User name");
        GridPane.setConstraints(name, 0, 0);

        //name input
        TextField input = new TextField();
        input.setPromptText("name");
        GridPane.setConstraints(input, 1, 0);

        //password label
        Label password = new Label("User password");
        GridPane.setConstraints(password, 0, 1);

        //name input
        TextField input2 = new TextField();
        input2.setPromptText("password");
        GridPane.setConstraints(input2, 1, 1);
        Button log = new Button(string);
        if (flag) {
            log.setOnAction(event -> {
                System.out.println("log in name " + input.getText() + " pass " + input2.getText());

                key = userTable.getUserExistanceInDatabase(input.getText(), input2.getText());

                try {
                    if (key) {
                        windowClose(log);
                        startButton.setDisable(false);
                    } else {
                        throw new UserNotFoundException(input.getText());
                    }
                } catch (UserNotFoundException e) {
                    labellog.setText(e.getMessage());
                } finally {
                    //userTable.getAllUsers();
                }   
                
            });
        } else {
            log.setOnAction(event -> {
                System.out.println("sign name " + input.getText() + " pass " + input2.getText());

                key = userTable.getUserExistanceInDatabase(input.getText(), input2.getText());

                try {
                    if (!key) {
                        System.out.println("inside the if in !key");
                        windowClose(log);
                        startButton.setDisable(false);
                        userTable.insertUsers(input.getText(), input2.getText());
                    } else {
                        throw new UserExistsException(input.getText());
                    }   
                } catch (UserExistsException e) {
                    labelsign.setText(e.getMessage());
                } finally {
                    //userTable.getAllUsers();
                }   
            });
        }
       
        log.setPrefSize(80, 30); // Set the preferred width and height
        GridPane.setConstraints(log, 1, 4);
        grid.getChildren().addAll(name, input, password, input2, log);

        return grid;
    }

    /** Method: windowClose() closes the window after the press of 
     * the buttons log in or sign up inside the window
     * @param log The specific button that i want
     */
    private void windowClose(Button log) {
        Scene scenelog = log.getScene();
        Stage stagelog = (Stage) scenelog.getWindow();
        stagelog.close();
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


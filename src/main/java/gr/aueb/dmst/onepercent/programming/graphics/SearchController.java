package gr.aueb.dmst.onepercent.programming.graphics;

import exceptions.EmptyFieldError;
import exceptions.PullImageException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;


import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;


/**
 * FXML Controller class.
 * Class that controls the Docker Hub page of the GUI. It is used to search for images
 * in the registry and pull them. This class provides a connection with Docker Hub
 * and the database. 
 * Regarding to Docker Hub connectivity, users can search and pull images that are uploaded
 * in Docker Hub. They are prompt to  press the search or the pull button. 
 * If they press the search button, they can specify the number of results they want to see. Then, 
 * they are prompt to enter the name of the image they want to search for. 
 * If they press the pull button, they are prompt to enter the name of the image they want to pull.
 * By exception handling, the user is informed about the status of the pull request (success,
 * failure due to internal server error, failure due to image not found)
 * Regarding to the database connectivity, the previous searches of the user are saved in the 
 * database and displayed in the GUI. User can select one of them so as to search for it again.
 * Last 5 searches are displayed in the GUI.
 * @see exceptions.PullImageException
 */
public class SearchController {
    /** ok */
    public SearchController() { }

    @FXML
    private TextField autoCompleteTextField;

    @FXML 
    private HBox prevSearchesBox;

    @FXML
    private VBox vboxContainer;

    @FXML
    private VBox resultSet;

    @FXML
    private TextField resultCount;

    @FXML
    private HBox searchParameters;

    @FXML
    private Label countErrorText;

    DataBase dataBase = DataBase.getInstance();
    ArrayList<Button> suggestions;
   
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        preparePreviousSearches();
        spinner = new MFXProgressSpinner();
        spinner.setRadius(30);
        spinner.color1Property().setValue(new Color(0.5, 0, 0.95, 1));
        spinner.color2Property().setValue(new Color(0.5, 0, 0.95, 1));
        spinner.color3Property().setValue(new Color(0.5, 0, 0.95, 1));
        spinner.color4Property().setValue(new Color(0.5, 0, 0.95, 1));
        String path = "src\\main\\resources\\images\\searchPage\\searchDefault.png";
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(190);
        imageView.setPreserveRatio(true);
        resultSet.getChildren().add(imageView);

    }

    private void preparePreviousSearches() {
        ArrayList<String> arr = dataBase.getImageForSearch();
        suggestions = new ArrayList<Button>();

        if (arr.size() == 0) {
            prevSearchesBox.setVisible(false);
            return;
        }

        for (int i = 0; i < Math.min(5, arr.size()); i++) {
            Button b = createButton(arr.get(i));
            suggestions.add(b);
        }

        Collections.reverse(suggestions);
        prevSearchesBox.getChildren().addAll(suggestions);
    }

    /**
     * Handles the key pressed event.
     * @param event the event.
     */
    @FXML
    void handleKeyPressed(KeyEvent event) {
        try {
            if (event.getCode().toString().equals("ENTER") && isReadyForSearch()) {
                //loadSpinner.setVisible(true);
                handleEnterFunctionality();
            }
        } catch (EmptyFieldError e) {
            clearResults();
            Text exceptionText = new Text(e.getDefaultMessage());
            exceptionText.setStyle("-fx-fill: " + e.getColorCode() + "; -fx-font-size: 16px;");
            resultSet.getChildren().add(exceptionText);
        }
    }

    @FXML
    void searchByButton(ActionEvent event) {
        handleEnterFunctionality();
    }

    MFXProgressSpinner spinner;

    private void handleEnterFunctionality() {
       
        countErrorText.setVisible(false);
        if (resultCount.getText().equals("") ||
            Integer.parseInt(resultCount.getText()) > 100) {
            countErrorText.setVisible(true);
            return;
        }
        clearResults();
        spinner.setVisible(true);
        resultSet.getChildren().add(spinner);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            executeSearch(e);
            updatePrevSearches();
        });
        pause.play();
        
    }

    MenuThreadGUI menuThreadGUI = new MenuThreadGUI();

    /**
     * Executes the search for the image.
     */
    
    MonitorHttpGUI monitorHttpGui = new MonitorHttpGUI();

    private void executeSearch(ActionEvent e) {
        //functionality
        MonitorHttpGUI.imageName = autoCompleteTextField.getText();
        MonitorHttpGUI.searchResultCount = Integer.parseInt(resultCount.getText());
        System.out.println("Before execution");
        menuThreadGUI.handleUserInputGUI(3);
        System.out.println("After execution");
        
        StringBuilder stringBuilder = MonitorThreadGUI.getInstance().getContainerMonitorHttp().
            getSearchResult(autoCompleteTextField.getText());
        System.out.println("string builder: " + stringBuilder);

        Platform.runLater(() -> {
            resultSet.getChildren().remove(spinner);
        });

        System.out.println(monitorHttpGui.getResponse1().toString());
        int statusCode = (monitorHttpGui.getHttpResponse() != null) ?
            monitorHttpGui.getHttpResponse().getStatusLine().getStatusCode() : 404;
        System.out.println("status code outside switch: " + statusCode);
        PopupController popupController = new PopupController();
        switch (statusCode) {
            case 200:
                System.out.println("status code: " + statusCode);
                break;  
            case 500:
                System.out.println("status code: " + statusCode);
                showSearchError();
                popupController.showPopup(e);
                popupController.setErrorMessage("An Internal Server Error has occured!");
                return;  
            default:
                showSearchError();
                popupController = new PopupController();
                popupController
                .setErrorMessage("Seems like the image you are looking for does not exist!");
                popupController.showPopup(e);
                return;
        }    
        
         
        String[] imageInfo = stringBuilder.toString().
            replace("\"", "").split("\n");
        
        for (int i = 0; i < imageInfo.length; ) {

            String title = imageInfo[i];
            i++;
            String description = imageInfo[i];
            i++;
            String starCount = imageInfo[i];
            i++;

            String imIconPathString = "src\\main\\resources\\images\\dockerHubPage\\imageIcon.png";
            Path imIconPath = Paths.get(imIconPathString);
            Image imageIconImg = new Image(imIconPath.toUri().toString());

            ImageView imageIcon = new ImageView(imageIconImg);
            imageIcon.setFitHeight(50);
            imageIcon.setFitWidth(50);

            HBox imageBox = new HBox();
            imageBox.setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-radius: 20; " +
                "-fx-background-radius: 20;");
            imageBox.setAlignment(Pos.CENTER_LEFT);
            imageBox.setPadding(new Insets(20, 20, 20, 20));
            imageBox.setMinWidth(910);
            imageBox.setMinHeight(100);
            imageBox.setMaxWidth(910);
            imageBox.setMaxHeight(100);
            imageBox.setSpacing(20);

            VBox titleDescBox = new VBox();
            titleDescBox.setMinWidth(550);
            titleDescBox.setMaxWidth(550);
            titleDescBox.setSpacing(10);

            Text titleText = new Text(title);
            Text descText = new Text(description);
            titleText.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            descText.setStyle("-fx-font-size: 12px; -fx-fill: #555");

            TextFlow descTextFlow = new TextFlow(descText);
            descTextFlow.setMaxWidth(550);
            titleDescBox.getChildren().addAll(titleText, descTextFlow);

            Button pullButton = new Button("Pull");
            pullButton.setStyle("-fx-text-fill: #6200ee; " +
                "-fx-background-color: transparent; fx-font-size: 15px;");
            pullButton.setMinWidth(100);
            pullButton.setMaxWidth(100);

            pullButton.setOnAction((ActionEvent event) -> {
                executePull(pullButton, title, event);
            });
            
            String path = "src\\main\\resources\\images\\dockerHubPage\\star.png";
            Path imagePath = Paths.get(path);
            Image image = new Image(imagePath.toUri().toString());
            
            ImageView starIcon = new ImageView(image);
            starIcon.setFitHeight(15);
            starIcon.setFitWidth(15);

            Text starCountText = new Text(starCount);
            starCountText.setStyle("-fx-text-fill: #555;");
            HBox.setMargin(starCountText, new Insets(0, 0, 0, -18));
            imageBox.getChildren().
                addAll(imageIcon, titleDescBox, pullButton, starIcon, starCountText);
            
            resultSet.getChildren().add(imageBox);
        }
    }
    ManagerHttpGUI managerHttpGUI = new ManagerHttpGUI();
    /**
     * Executes the pull for the image.
     * @throws PullImageException if the image is not found or there is an internal server error.
     */
    private void executePull(Button pullButton, String imName, ActionEvent e) {
        //functionality
        
        ManagerHttpGUI.imageName = imName;
        menuThreadGUI.handleUserInputGUI(4);
        
        //final String ERROR_CODE_STYLE = "-fx-font-size: 50px; -fx-fill: #ff0000;font-weight: bold;
        //final String TEXT_STYLE = "-fx-font-size: 24px;";
        int statusCode = Integer.parseInt(managerHttpGUI.getResponse1().toString());
        
        //String message;
        PopupController popupController = new PopupController();
        switch (statusCode) {
            case 200:
                //Text statusCodeText = new Text("Status Code");
                //statusCodeText.setStyle("-fx-font-size: 18px;");
            
                //Text resultCode = new Text(managerHttpGUI.getResponse1().toString());
                //resultCode.setStyle("-fx-font-size: 50px; -fx-fill: #3dc985; font-weight: bold;");

                //Text resultText = new Text("Success! Image pulled successfully!");
                //resultText.setStyle(TEXT_STYLE);
                //resultSet.getChildren().remove(spinner);
                pullButton.setText("Pulled");
                pullButton.setStyle("-fx-text-fill: #555; " +
                    "-fx-background-color: transparent; fx-font-size: 15px;");
                pullButton.setDisable(true);
                /*Platform.runLater(() -> {
                    resultSet.getChildren().addAll(statusCodeText, resultCode, resultText);
                    System.out.println("result added");
                });*/
                
                break;
            case 500:
                popupController.showPopup(e);
                popupController.setErrorMessage("An Internal Server Error has occured!");
                break;
            default:
                popupController = new PopupController();
                popupController.setErrorMessage("We are sorry! Something unexpected has occured!");
                popupController.showPopup(e);
        }
        
    }

    /**
     * Updates the previous searches of the user. It provides suggestions,
     * based on the previous searches of the user. This is done by saving the
     * previous searches in the database.
     */
    private void updatePrevSearches() {
        boolean alreadyExists = false;
        for (Button b : suggestions) {
            if (b.getText().equals(autoCompleteTextField.getText())) {
                alreadyExists = true;
                break;
            }
        }
        if (alreadyExists) return;

        if (suggestions.size() == 0) {
            prevSearchesBox.setVisible(true);
            Button b = createButton(autoCompleteTextField.getText());
            suggestions.add(b);
            prevSearchesBox.getChildren().add(b);
        } else if (suggestions.size() < 5) {
            String oldLast = suggestions.get(suggestions.size() - 1).getText();
            switchSuggestions(suggestions.size() - 1);
            Button b = createButton(oldLast);
            suggestions.add(b);
            prevSearchesBox.getChildren().add(b);
        } else if (suggestions.size() == 5) {
            switchSuggestions(4);
        }
    }

    /**
     * Creates a button.
     * @param text the text of the button.
     * @return the button.
     */
    private Button createButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #bb66fc; " + 
            "-fx-border-radius: 8; -fx-text-fill: #ffffff;"); 
        b.setOnAction(e -> {
            autoCompleteTextField.setText(b.getText());
        });
        return b;
    }

    /**
     * Switches the suggestions based on the index of the last suggestion so
     * as to display the new ones and do not blend with the previous ones.
     * @param lastIndex the index of the last suggestion.
     */
    private void switchSuggestions(int lastIndex) {
        for (int i = lastIndex; i >= 1; i--) {
            suggestions.get(i).setText(suggestions.get(i - 1).getText());
        }
        suggestions.get(0).setText(autoCompleteTextField.getText());
    }

    private boolean isReadyForSearch() throws EmptyFieldError {
        if (autoCompleteTextField.getText().equals("") ||
            autoCompleteTextField.getText().equals(" ")) {
            throw new EmptyFieldError();
        }
        return true;
    }

    /**
     * Clears the results so as to display the new ones and do not blend with the previous ones.
     */
    private void clearResults() {
        resultSet.getChildren().clear();
    }

    private void showSearchError() {
        clearResults();
        String path = "src\\main\\resources\\images\\searchPage\\searchError.png";
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(true);
        resultSet.getChildren().add(imageView);
    }
}

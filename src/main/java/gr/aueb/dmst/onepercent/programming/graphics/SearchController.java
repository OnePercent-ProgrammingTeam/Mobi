package gr.aueb.dmst.onepercent.programming.graphics;

import exceptions.EmptyFieldError;
import exceptions.PullImageException;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;


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
    private Button searchButton;

    @FXML
    private Button pullButton;

    @FXML
    private VBox resultSet;

    @FXML
    private TextField resultCount;

    @FXML
    private HBox searchParameters;

    DataBase dataBase = DataBase.getInstance();
    ArrayList<Button> suggestions;
    boolean isForSeach = true;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        selectSearch();
        preparePreviousSearches();
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
                handleEnterFunctionality();
            }
        } catch (EmptyFieldError e) {
            clearResults();
            Text exceptionText = new Text(e.getDefaultMessage());
            exceptionText.setStyle("-fx-fill: " + e.getColorCode() + "; -fx-font-size: 16px;");
            resultSet.getChildren().add(exceptionText);
        }
    }

    private void handleEnterFunctionality() {
        if (isForSeach) {
            clearResults();
            executeSearch();
            updatePrevSearches();
        } 
        
        if (!isForSeach) {
            clearResults();
            //exception handling, formatting the appearance of the result
            try {
                executePull();
            } catch (PullImageException e) {
                Text statusCodeText = new Text("Status Code");
                statusCodeText.setStyle("-fx-font-size: 18px;");
                
                Text resultCode = new Text(String.valueOf(e.getStatusCode()));
                resultCode.setStyle(e.getStatusCodeStyle());
                
                Text resultText = new Text(e.getMessage());
                resultText.setStyle(e.getStyle());
                
                resultSet.getChildren().addAll(statusCodeText, resultCode, resultText);
            }
            
        }
    }

    /**
     * Selects the search button.
     */
    @FXML
    void selectSearch() {
        searchButton.setStyle(selectButtonsDefaultStyle() + "-fx-border-color: #6200ee;");
        pullButton.setStyle(selectButtonsDefaultStyle() + "-fx-border-color: #ffffff;");
        isForSeach = true;
        prevSearchesBox.setVisible(true);
        searchParameters.setVisible(true);
    }

    /**
     * Selects the pull button.
     */
    @FXML
    void selectPull() {
        pullButton.setStyle(selectButtonsDefaultStyle() + "-fx-border-color: #6200ee;");
        searchButton.setStyle(selectButtonsDefaultStyle());
        isForSeach = false;
        prevSearchesBox.setVisible(false);
        searchParameters.setVisible(false);
    }

    /**
     * Sets the default style of the buttons.
     * @return the default style of the buttons.
     */
    private String selectButtonsDefaultStyle() {
        return "-fx-background-color: #ffffff; -fx-border-color: #ffffff;" +
            "-fx-border-radius: 8; -fx-text-fill: #6200ee;" +
            "-fx-border-width: 2px; -fx-background-radius: 8;";
    }

    MenuThreadGUI menuThreadGUI = new MenuThreadGUI();

    /**
     * Executes the search for the image.
     */
    private void executeSearch() {
        //functionality
        MonitorHttpGUI.imageName = autoCompleteTextField.getText();
        MonitorHttpGUI.searchResultCount = Integer.parseInt(resultCount.getText());
        menuThreadGUI.handleUserInputGUI(3);

        MonitorHttpGUI monitorHttpGui = new MonitorHttpGUI();
        StringBuilder stringBuilder = monitorHttpGui.
            getSearchResult(autoCompleteTextField.getText());
        
        String[] imageInfo = stringBuilder.toString().
            replace("\"", "").split("\n");
        for (int i = 0; i < imageInfo.length; ) {
            String title = imageInfo[i];
            i++;
            String description = imageInfo[i];
            i++;
            String starCount = imageInfo[i];
            i++;
            HBox imageBox = new HBox();
            imageBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #6200ee;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 2px; -fx-background-radius: 8;");
            imageBox.setPadding(new Insets(10, 10, 10, 10));
            HBox.setMargin(imageBox, new Insets(20, 0, 0, 0));
            imageBox.setMaxHeight(70);
            imageBox.setSpacing(5);

            VBox titleDescBox = new VBox();
            titleDescBox.setMinWidth(600);
            titleDescBox.setSpacing(10);

            Text titleText = new Text(title);
            Text descText = new Text(description);
            titleText.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            descText.setStyle("-fx-font-size: 15px; -fx-text-fill: #111111");

            TextFlow descTextFlow = new TextFlow(descText);
            descTextFlow.setMaxWidth(580);
            titleDescBox.getChildren().addAll(titleText, descTextFlow);
            
            String path = "src\\main\\resources\\images\\star.png";
            Path imagePath = Paths.get(path);
            Image image = new Image(imagePath.toUri().toString());
            
            ImageView starIcon = new ImageView(image);
            starIcon.setFitHeight(15);
            starIcon.setFitWidth(15);

            Text starCountText = new Text(starCount);
            imageBox.getChildren().addAll(titleDescBox, starIcon, starCountText);
            
            resultSet.getChildren().add(imageBox);
        }
    }

    /**
     * Executes the pull for the image.
     * @throws PullImageException if the image is not found or there is an internal server error.
     */
    private void executePull() throws PullImageException {
        //functionality
        ManagerHttpGUI managerHttpGUI = new ManagerHttpGUI();
        ManagerHttpGUI.imageName = autoCompleteTextField.getText();
        menuThreadGUI.handleUserInputGUI(4);
        
        final String ERROR_CODE_STYLE = "-fx-font-size: 50px; -fx-fill: #ff0000;font-weight: bold;";
        final String TEXT_STYLE = "-fx-font-size: 24px;";

        int statusCode = Integer.parseInt(managerHttpGUI.getResponse1().toString());
        String message;
        switch (statusCode) {
            case 200:
                Text statusCodeText = new Text("Status Code");
                statusCodeText.setStyle("-fx-font-size: 18px;");
            
                Text resultCode = new Text(managerHttpGUI.getResponse1().toString());
                resultCode.setStyle("-fx-font-size: 50px; -fx-fill: #3dc985; font-weight: bold;");

                Text resultText = new Text("Success! Image pulled successfully!");
                resultText.setStyle(TEXT_STYLE);
               
                resultSet.getChildren().addAll(statusCodeText, resultCode, resultText);
                break;
            case 404:
                message = "Error! Image not found!";
                throw new PullImageException(message, TEXT_STYLE, statusCode, ERROR_CODE_STYLE);
            case 500:
                message = "Error! Internal server error!";
                throw new PullImageException(message, TEXT_STYLE, statusCode, ERROR_CODE_STYLE);
            default:
                message = "Oops... Something went wrong!";
                throw new PullImageException(message, TEXT_STYLE, statusCode, ERROR_CODE_STYLE);
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
}

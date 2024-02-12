package gr.aueb.dmst.onepercent.programming.graphics;

import gr.aueb.dmst.onepercent.programming.data.Database;

import gr.aueb.dmst.onepercent.programming.exceptions.EmptyFieldError;
import gr.aueb.dmst.onepercent.programming.exceptions.PullImageException;

import gr.aueb.dmst.onepercent.programming.gui.ManagerGUI;
import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;

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

/**
 * Controller class for the Docker Hub page of the GUI. 
 * 
 * <p>It is used to search for imagesvin the registry and pull them, providing a connection to
 * Docker Hub. Implemented through 
 * <a href="https://docs.docker.com/docker-hub/api/latest/">Docker Hub API</a>
 */
public class SearchController {
    /*XXX: After searching for an images and getting the results, the app does not respond.
    Might be caused by threading issues or HTTP response. Fix it ASAP.*/

    /*TO DO(Anyone): Create a graphical way to handle the error, when image pull fails. */
    
    /** Instance of database to store search related info. */
    Database dataBase = Database.getInstance();
    
    /** A list with the suggestions, based on previous searches. */
    ArrayList<Button> suggestions;

    /** A menu thread instance. */
    MenuThreadGUI menu_thread = new MenuThreadGUI();

    /** A monitor instance. */
    MonitorGUI monitor = new MonitorGUI();

    /** A manager instance. */
    ManagerGUI manager = new ManagerGUI();

    /** Loading spinner. */
    MFXProgressSpinner spinner;

    /** Autocompleted search text field. */
    @FXML
    private TextField autoCompleteTextField;

    /** The previous searches-suggestions. */
    @FXML 
    private HBox prevSearchesBox;

    /**A vertical box with containers. */
    @FXML
    private VBox vboxContainer;

    /**The result set. */
    @FXML
    private VBox resultSet;

    /** The result count. */
    @FXML
    private TextField resultCount;

    /** The parameters of the search.  */
    @FXML
    private HBox searchParameters;

    /** The error text. */
    @FXML
    private Label countErrorText;

    /** Default constructor. */
    public SearchController() { }

    /** Initializes the controller class. */
    @FXML
    public void initialize() {
        preparePreviousSearches();
        //customize the loading spinner, that is shown while waiting for search results.
        spinner = new MFXProgressSpinner();
        spinner.setRadius(30); 
        spinner.color1Property().setValue(new Color(0.5, 0, 0.95, 1));
        spinner.color2Property().setValue(new Color(0.5, 0, 0.95, 1));
        spinner.color3Property().setValue(new Color(0.5, 0, 0.95, 1));
        spinner.color4Property().setValue(new Color(0.5, 0, 0.95, 1));
        //All the search results have a default icon.
        String path = "src\\main\\resources\\images\\searchPage\\searchDefault.png";
        Path imagePath = Paths.get(path);
        Image image = new Image(imagePath.toUri().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(190);
        imageView.setPreserveRatio(true);
        resultSet.getChildren().add(imageView);
    }

    /** 
     * Prepares the previous images searched by the user.
     */
    private void preparePreviousSearches() {
        ArrayList<String> arr = dataBase.getSearchSuggestions();
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
     * @param event The event.
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

    /**
     * Starts searching when enter is pressed.
     * @param event The event.
     */
    @FXML
    void searchByButton(ActionEvent event) {
        handleEnterFunctionality();
    }

    /**
     * Handles the background task executed when enter pressed.
     */
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

    //XXX: Possible threading issues here.
    /**
     * Executes the search of the image.
     * @param action the action event.
     */
    private void executeSearch(ActionEvent action) {
        //functionality
        MonitorGUI.imageName = autoCompleteTextField.getText();
        MonitorGUI.searchResultCount = Integer.parseInt(resultCount.getText());
        menu_thread.handleUserInput(3);
        StringBuilder stringBuilder = MonitorThreadGUI.getInstance().getMonitorInstance()
            .getSearchResult(autoCompleteTextField.getText());
        //Secure thread concurrency.
        Platform.runLater(() -> {
            resultSet.getChildren().remove(spinner);
        });
        //Api did not have a 404 status code, so we had to define it:
        int statusCode = (monitor.getHttpResponse() != null) ?
            monitor.getHttpResponse().getStatusLine().getStatusCode() : 404;
        PopupController popupController = new PopupController();
        switch (statusCode) {
            case 200:
                break;  
            case 500:
                showSearchError();
                popupController.showPopup(action);
                popupController.setErrorMessage("An Internal Server Error has occured!");
                return;  
            default: //when image not found.
                showSearchError();
                popupController = new PopupController();
                popupController
                .setErrorMessage("Seems like the image you are looking for does not exist!");
                popupController.showPopup(action);
                return;
        }
        //Customize the appearance of the search result.
        String[] imageInfo = stringBuilder.toString().
            replace("\"", "").split("\n");
        for (int i = 0; i < imageInfo.length; ) {
            String title = imageInfo[i];
            i++;
            String description = imageInfo[i];
            i++;
            String starCount = imageInfo[i];
            i++; 
            //add default icon to search results.
            String imIconPathString = "src\\main\\resources\\images\\dockerHubPage\\imageIcon.png";
            Path imIconPath = Paths.get(imIconPathString);
            Image imageIconImg = new Image(imIconPath.toUri().toString());
            ImageView imageIcon = new ImageView(imageIconImg);
            //set the size of it.
            imageIcon.setFitHeight(50);
            imageIcon.setFitWidth(50);
            //add everything in a hbox to be displayed horizontally
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
            //the vbox contains the title and the description of the search result.
            VBox titleDescBox = new VBox();
            //customize it.
            titleDescBox.setMinWidth(550);
            titleDescBox.setMaxWidth(550);
            titleDescBox.setSpacing(10);
            //create the title and the description text, based on the results.
            Text titleText = new Text(title);
            Text descText = new Text(description);
            /*customize them, add them in a text flow and then in the vbox in orded to 
            be shown vertically*/
            titleText.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            descText.setStyle("-fx-font-size: 12px; -fx-fill: #555");
            TextFlow descTextFlow = new TextFlow(descText);
            descTextFlow.setMaxWidth(550);
            titleDescBox.getChildren().addAll(titleText, descTextFlow);
            /*create a pull button for each result so as to let user pull the searched image in
            he wants to*/
            Button pullButton = new Button("Pull");
            pullButton.setStyle("-fx-text-fill: #6200ee; " +
                "-fx-background-color: transparent; fx-font-size: 15px;");
            pullButton.setMinWidth(100);
            pullButton.setMaxWidth(100);
            //Add functionality to the button created.
            pullButton.setOnAction((ActionEvent event) -> {
                executePull(pullButton, title, event);
            });
            //Set an image of star for the star count.
            String path = "src\\main\\resources\\images\\dockerHubPage\\star.png";
            Path imagePath = Paths.get(path);
            Image image = new Image(imagePath.toUri().toString());
            ImageView starIcon = new ImageView(image);
            starIcon.setFitHeight(15);
            starIcon.setFitWidth(15);
            Text starCountText = new Text(starCount);
            starCountText.setStyle("-fx-text-fill: #555;");
            HBox.setMargin(starCountText, new Insets(0, 0, 0, -18));
            //add all the components to the box of the result.
            imageBox.getChildren().
                addAll(imageIcon, titleDescBox, pullButton, starIcon, starCountText); 
            resultSet.getChildren().add(imageBox);
        }
    }
  
    /**
     * Executes the pull for the image.
     * @throws PullImageException if the image is not found or there is an internal server error.
     */
    private void executePull(Button pullButton, String imName, ActionEvent e) {
        //functionality.
        ManagerGUI.imageName = imName;
        menu_thread.handleUserInput(4);
        int statusCode = Integer.parseInt(manager.getResponseBuilder().toString());
        PopupController popupController = new PopupController();
        switch (statusCode) {
            /* XXX: We have to find out why the system collapses after searching an image...
            might be caused by threading issues or http responses/ buffers etc that 
            are not cleared. */ 
            case 200:
                pullButton.setText("Pulled");
                pullButton.setStyle("-fx-text-fill: #555; " +
                    "-fx-background-color: transparent; fx-font-size: 15px;");
                pullButton.setDisable(true);
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
     * Updates the previous searches of the user. 
     * <p>It provides suggestions,
     * based on the previous searches of the user. 
     * This is done by saving the previous searches in the database.
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

    /** Shows a UI component indicating search error. */
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

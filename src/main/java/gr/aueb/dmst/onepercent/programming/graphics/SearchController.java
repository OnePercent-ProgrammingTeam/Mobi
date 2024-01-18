package gr.aueb.dmst.onepercent.programming.graphics;

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

import gr.aueb.dmst.onepercent.programming.core.DataBase;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;

public class SearchController {

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

    DataBase database = new DataBase();
    ArrayList<Button> suggestions;
    boolean isForSeach = true;

    @FXML
    public void initialize() {
        selectSearch();

        ArrayList<String> arr = database.getImageForSearch();
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

    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (isForSeach && isReadyForSearch()) {
                clearResults();
                executeSearch();
                updatePrevSearches();
            } else if (!isForSeach) {
                clearResults();
                executePull();
            }
        }
    }

    @FXML
    void selectSearch() {
        searchButton.setStyle(selectButtonsDefaultStyle() + "-fx-border-color: #2a2a72;");
        pullButton.setStyle(selectButtonsDefaultStyle() + "-fx-border-color: #ffffff;");
        isForSeach = true;
    }

    @FXML
    void selectPull() {
        pullButton.setStyle(selectButtonsDefaultStyle() + "-fx-border-color: #2a2a72;");
        searchButton.setStyle(selectButtonsDefaultStyle());
        isForSeach = false;
    }

    private String selectButtonsDefaultStyle() {
        return "-fx-background-color: #ffffff; -fx-border-color: #ffffff;" +
            "-fx-border-radius: 8; -fx-text-fill: #2a2a72;" +
            "-fx-border-width: 2px; -fx-background-radius: 8;";
    }

    MenuThreadGUI menuThreadGUI = new MenuThreadGUI();

    private void executeSearch() {
        //functionality
        MonitorHttpGUI.imageName = autoCompleteTextField.getText();
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
            imageBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #2a2a72;" +
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

    private void executePull() {
        //functionality
        ManagerHttpGUI managerHttpGUI = new ManagerHttpGUI();
        ManagerHttpGUI.imageName = autoCompleteTextField.getText();
        menuThreadGUI.handleUserInputGUI(4);
        System.out.println(managerHttpGUI.getResponse1().toString());
        Text text = new Text(managerHttpGUI.getResponse1().toString());
        vboxContainer.getChildren().add(text);
    }

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

    private Button createButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #6495ed; " + 
            "-fx-border-radius: 8; -fx-text-fill: #ffffff;"); 
        b.setOnAction(e -> {
            autoCompleteTextField.setText(b.getText());
        });
        return b;
    }

    private void switchSuggestions(int lastIndex) {
        for (int i = lastIndex; i >= 1; i--) {
            suggestions.get(i).setText(suggestions.get(i - 1).getText());
        }
        suggestions.get(0).setText(autoCompleteTextField.getText());
    }

    private boolean isReadyForSearch() {
        if (autoCompleteTextField.getText().equals("") ||
            autoCompleteTextField.getText().equals(" ")) {
            return false;
        }
        return true;
    }

    private void clearResults() {
        resultSet.getChildren().clear();
    }
}

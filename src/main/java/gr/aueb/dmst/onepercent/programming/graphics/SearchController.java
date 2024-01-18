package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.core.DataBase;
import gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;

public class SearchController {

    @FXML
    private TextField autoCompleteTextField;

    @FXML 
    private HBox prevSearchesBox;

    @FXML
    private VBox vboxContainer;

    DataBase database = new DataBase();
    ArrayList<Button> suggestions;

    @FXML
    public void initialize() {
        ArrayList<String> arr = database.getImageForSearch();
        suggestions = new ArrayList<Button>();

        if (arr.size() == 0) {
            prevSearchesBox.setVisible(false);
            return;
        }

        for (int i = 0; i < Math.min(5, arr.size()); i++) {
            Button b = new Button(arr.get(i));
            suggestions.add(b);
            b.setStyle("-fx-background-color: #6495ed; " + 
                "-fx-border-radius: 8; -fx-text-fill: #ffffff;"); 
            b.setOnAction(e -> {
                autoCompleteTextField.setText(b.getText());
            });
        }

        prevSearchesBox.getChildren().addAll(suggestions);
    }

    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeSearch();
            updatePrevSearches();
        }
    }

    private void executeSearch() {
        //functionality
        MonitorHttpGUI.imageName = autoCompleteTextField.getText();
        MenuThreadGUI menuThreadGUI = new MenuThreadGUI();
        menuThreadGUI.handleUserInputGUI(3);

        MonitorHttpGUI monitorHttpGui = new MonitorHttpGUI();
        StringBuilder stringBuilder = monitorHttpGui.
            getSearchResult(autoCompleteTextField.getText());
        Text text = new Text(stringBuilder.toString());
        vboxContainer.getChildren().add(text);
    }

    private void updatePrevSearches() {
        if (suggestions.size() == 0) {
            prevSearchesBox.setVisible(true);
            Button b = new Button(autoCompleteTextField.getText());
            suggestions.add(b);
            b.setStyle("-fx-background-color: #6495ed; " + 
                "-fx-border-radius: 8; -fx-text-fill: #ffffff;"); 
            b.setOnAction(e -> {
                autoCompleteTextField.setText(b.getText());
            });
            prevSearchesBox.getChildren().add(b);
        } else if (suggestions.size() < 5) {
            String oldLast = suggestions.get(suggestions.size() - 1).getText();
            for (int i = suggestions.size() - 1; i >= 1; i--) {
                suggestions.get(i).setText(suggestions.get(i - 1).getText());
            }
            suggestions.get(0).setText(autoCompleteTextField.getText());
            Button b = new Button(oldLast);
            suggestions.add(b);
            b.setStyle("-fx-background-color: #6495ed; " + 
                "-fx-border-radius: 8; -fx-text-fill: #ffffff;"); 
            b.setOnAction(e -> {
                autoCompleteTextField.setText(b.getText());
            });
            prevSearchesBox.getChildren().add(b);
        } else if (suggestions.size() == 5) {
            for (int i = 4; i >= 1; i--) {
                suggestions.get(i).setText(suggestions.get(i - 1).getText());
            }
            suggestions.get(0).setText(autoCompleteTextField.getText());
        }
    }

}

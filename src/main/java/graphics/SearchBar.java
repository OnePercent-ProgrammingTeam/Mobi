package graphics;

import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.DataBase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SearchBar {
    
    TextField searchField = new TextField();
    
    ListView<String> autoCompleteListView = new ListView<>();

    public VBox createBar() {
        DataBase database = new DataBase();
        ArrayList<String> names = database.getImageForSearch();
        ObservableList<String> suggestions = FXCollections.observableArrayList(names);

        searchField.setMaxWidth(150);
        searchField.setMaxHeight(5);

        autoCompleteListView.setMaxWidth(150);
        //autoCompleteListView.setMaxWidth(150);
        searchField.setStyle("-fx-background-color: #FFFFFF;" +
                            "-fx-text-fill: black;");
        searchField.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));

        autoCompleteListView.setStyle("-fx-control-inner-background: #FFFFFF;" + 
                                        // Background color of the entire ListView
                                        "-fx-selection-bar: #E8E9EB;" +              
                                        // Color of the selected cell  #DCDCDC
                                        "-fx-background-color: #FFFFFF;" +
                                        // Background color of unselected cells
                                        //"-fx-font-family: 'Tahoma'; " +
                                        "-fx-font-weight: bold; " +
                                        "-fx-font-size: 15; ");
                                        
        
                                        
        
        autoCompleteListView.setMaxHeight(250); // Set the maximum height of the list

        autoCompleteListView.setVisible(false);

        autoCompleteListView.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-text-fill: black;");
                } else {
                    setText(item);

                    if (autoCompleteListView.getSelectionModel().getSelectedItem() != null &&
                            item.equals(autoCompleteListView
                                                        .getSelectionModel().getSelectedItem())) {
                        setStyle("-fx-text-fill: #2E5894; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String userInput = newValue.toLowerCase();
            ObservableList<String> filteredSuggestions = FXCollections.observableArrayList();

            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase().contains(userInput)) {
                    filteredSuggestions.add(suggestion);
                }
            }

            Platform.runLater(() -> {
                autoCompleteListView.setItems(filteredSuggestions);
                autoCompleteListView.setVisible(!filteredSuggestions.isEmpty());
            });
        });


        // Handle arrow key navigation and Enter key press in the search field
        searchField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    navigateListView(-1); // Up arrow
                    break;
                case DOWN:
                    navigateListView(1); // Down arrow
                    break;
                case ENTER:
                    handleListViewSelection();
                    break;
                default:
                    break;
            }
        });


        // Handle arrow key navigation and Enter key press in the list view
        autoCompleteListView.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    navigateListView(-1); // Up arrow
                    break;
                case DOWN:
                    navigateListView(1); // Down arrow
                    break;
                case ENTER:
                    handleListViewSelection();
                    break;
                default:
                    break;
            }
        });


        // Handle list view item selection
        autoCompleteListView.setOnMouseClicked(event -> {
            String selectedSuggestion = autoCompleteListView.getSelectionModel().getSelectedItem();
            if (selectedSuggestion != null) {
                searchField.setText(selectedSuggestion);

                autoCompleteListView.setVisible(false);

                // Move the cursor to the end of the updated text
                searchField.positionCaret(searchField.getLength());

                
                // Allow the user to continue typing after the choice
                searchField.requestFocus();
                
            }
        });

        VBox layout = new VBox(searchField, autoCompleteListView);
        //layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        return layout;

    }

    private void handleListViewSelection() {
        String selectedSuggestion = autoCompleteListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            searchField.setText(selectedSuggestion);
            autoCompleteListView.setVisible(false);

            searchField.positionCaret(selectedSuggestion.length());
        }
    }

    private void navigateListView(int direction) {
        int currentIndex = autoCompleteListView.getSelectionModel().getSelectedIndex();
        int newIndex = currentIndex + direction;

        int size = autoCompleteListView.getItems().size();

        /* 
        if (newIndex >= 0 && newIndex < autoCompleteListView.getItems().size()) {
            autoCompleteListView.getSelectionModel().select(newIndex);
        }
        */
        if (size > 0) {
            newIndex = (newIndex + size) % size; // Wrap around if going beyond bounds
            autoCompleteListView.getSelectionModel().select(newIndex);
    
            // Scroll to the selected index
            autoCompleteListView.scrollTo(newIndex);
        }
    }
   
    
}

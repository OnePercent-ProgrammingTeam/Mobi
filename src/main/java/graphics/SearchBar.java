package graphics;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SearchBar {
    
    TextField searchField = new TextField();
    ListView<String> autoCompleteListView = new ListView<>();

    public VBox makeBar() {
        ObservableList<String> suggestions = FXCollections.observableArrayList(
                "Apple", "Banana", "Cherry", "Date", "Grape", "Lemon", "Orange", "Peach"
        );

        
        autoCompleteListView.setMaxHeight(250); // Set the maximum height of the list

        autoCompleteListView.setVisible(false);

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

        if (newIndex >= 0 && newIndex < autoCompleteListView.getItems().size()) {
            autoCompleteListView.getSelectionModel().select(newIndex);
        }
    }
   
    
}

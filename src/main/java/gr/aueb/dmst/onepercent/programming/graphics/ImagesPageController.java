package gr.aueb.dmst.onepercent.programming.graphics;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.core.SystemController;
import gr.aueb.dmst.onepercent.programming.gui.ManagerGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorGUI;

import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * A UI controller that manages the user interface and functionalities of the images 
 * page in the application. This page features a table displaying information about locally 
 * installed images, including their names and IDs. Each row in the table includes a button 
 * to remove the corresponding image.
 */
public class ImagesPageController {
    
    /** The table with the locally installed images. */
    @FXML
    private TableView<DataModel> imagesTable;
    /** The column with the names of the images. */
    @FXML
    private TableColumn<DataModel, String> imagesNameCol;
    /** The column with the ids of the images. */
    @FXML
    private TableColumn<DataModel, String> imagesIdCol;
    /** The column with the buttons to remove images. */
    @FXML
    private TableColumn<DataModel, Button> removeCol;
    /** The collection with the customized data models (embedded class). */
    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    /** Default Constructor. */
    public ImagesPageController() { }
    
    /**
     * Initializes the Images page with it's UI components and adds functionalities.
     */
    @FXML
    public void initialize() {
        //Set cell values in order to be feasible to add buttons to the table.
        imagesNameCol.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        imagesIdCol.setCellValueFactory(new PropertyValueFactory<>("imageId"));
        MonitorGUI monitor = new MonitorGUI();
        monitor.listImages();
        ArrayList<String> imagesIdsList = new ArrayList<String>();
        imagesIdsList = monitor.getFormattedImageIdsList();
        ArrayList<String> imagesNamesList = new ArrayList<String>();
        imagesNamesList = monitor.getFormattedImageNamesList();
        //Create customized data models (embedded class).
        for (int i = 0; i < imagesNamesList.size(); i++) {
            data.add(new DataModel(
                    imagesNamesList.get(i),
                    imagesIdsList.get(i),
                    new Button()));
        }
        removeCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button button = new Button();
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else { //style the remove buttons of the table.
                    String path = "src/main/resources/images/imagesPage/remove-icon.png";
                    Path pathToFile = Paths.get(path);
                    button.setStyle("-fx-background-color: transparent; " +
                        "-fx-background-image: url('" + pathToFile.toUri().toString() + "'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: center center;" +
                        "-fx-background-size: 20px 20px;");
                    setAlignment(javafx.geometry.Pos.CENTER);
                    setGraphic(button);
                }
            }
            {   //Add functionality to the button to remove images when clicked when clicked.
                button.setOnMouseClicked(event -> {
                    Platform.runLater(() -> { //speed up the process 
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        ManagerGUI managerHttpGUI = new ManagerGUI();
                        ManagerGUI.imageName = dataModel.getImageName();
                        SystemController superHttp = new SystemController();
                        superHttp.imageName = ManagerGUI.imageName;
                        managerHttpGUI.removeImage();
                        data.remove(dataModel);
                        int removedIndex = getTableRow().getIndex();
                        imagesTable.getItems().remove(removedIndex);
                    });
                }); 
            }
        });
        // Style the columns
        imagesTable.setItems(data);
        imagesNameCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");
        imagesIdCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;"); 
        removeCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");
    }

   /**
    * An embedded class representing a customized data model, needed for the table,
    * containing the container's name, id, status, time of creation, a button to start/ stop it
    * and a button to remove it.
    */
    public static class DataModel {
        /** The name of the image. */
        private final String imageName;
        /** The id of the image. */
        private final String imageId;
        /** The button to remove the image. */
        private final Button removeButton;
        
        /**
         * Constructor to initialize the fields.
         * @param imageName The name of the image.
         * @param imageId The id of the image.
         * @param removeButton The button to remove the image.
         */
        public DataModel(String imageName, String imageId, Button removeButton) {
            this.imageName = imageName;
            this.imageId = imageId;
            this.removeButton = removeButton;
        }
    
        /**
         * Getter for image name.
         * @return The image name.
         */
        public String getImageName() {
            return imageName;
        }

        /**
         * Getter for image id.
         * @return The image id.
         */
        public String getImageId() {
            return imageId;
        }

        /**
         * Getter for the button, used to remove images.
         * @return The remove button.
         */
        public Button getRemoveButton() {
            return removeButton;
        }
    }
}

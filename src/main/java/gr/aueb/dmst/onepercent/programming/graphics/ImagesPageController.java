package gr.aueb.dmst.onepercent.programming.graphics;

import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;

/**
 * ok
 */

public class ImagesPageController {
    /** ok */
    public ImagesPageController() { }
    
    @FXML
    private TableView<DataModel> imagesTable;

    @FXML
    private TableColumn<DataModel, String> imagesNameCol;

    @FXML
    private TableColumn<DataModel, String> imagesIdCol;

    @FXML
    private TableColumn<DataModel, Button> removeCol;

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    /**
     * ok
     */
    @FXML
    public void initialize() {
        // Link columns to corresponding properties in DataModel
        imagesNameCol.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        imagesIdCol.setCellValueFactory(new PropertyValueFactory<>("imageId"));
        
        System.out.println("HELLO WORLD");
        MonitorHttpGUI monitorHttp = new MonitorHttpGUI();
        monitorHttp.getImagesListGUI();
        ArrayList<String> imagesIdsList = new ArrayList<String>();
        imagesIdsList = monitorHttp.getFormattedImageIdsList();
        ArrayList<String> imagesNamesList = new ArrayList<String>();
        imagesNamesList = monitorHttp.getFormattedImageNamesList();;




        // Populate data from ArrayLists
        for (int i = 0; i < imagesNamesList.size(); i++) {
            
            data.add(new DataModel(
                    imagesNamesList.get(i),
                    imagesIdsList.get(i),
                    new Button()));
        }

        // Set the items for the TableView
        imagesTable.setItems(data);
        imagesNameCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");
        
        imagesIdCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");    


        imagesNameCol.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        imagesIdCol.setCellValueFactory(new PropertyValueFactory<>("imageId"));
        
        // Set up the removeCol column with a custom cell factory
        
    }

    /**
     * ok
     */
    public static class DataModel {
        private final String imageName;
        private final String imageId;
        private final Button removeButton;
        
        /**
         * ok
         * @param imageName ok
         * @param imageId ok
         * @param removeButton ok
         */
        public DataModel(String imageName, String imageId, Button removeButton) {
            this.imageName = imageName;
            this.imageId = imageId;
            this.removeButton = removeButton;
        }
    
        /**
         * ok
         * @return ok
         */
        public String getImageName() {
            return imageName;
        }

        /**
         * ok
         * @return ok
         */
        public String getImageId() {
            return imageId;
        }

        /**
         * ok
         * @return ok
         */
        public Button getRemoveButton() {
            return removeButton;
        }
    }
}

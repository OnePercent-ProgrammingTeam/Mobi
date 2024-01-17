package gr.aueb.dmst.onepercent.programming.graphics;

import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ImagesPageController {
    
    @FXML
    private TableView<DataModel> imagesTable;

    @FXML
    private TableColumn<DataModel, String> imagesNameCol;

    @FXML
    private TableColumn<DataModel, String> imagesIdCol;

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

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
                    imagesIdsList.get(i)));
        }

        // Set the items for the TableView
        imagesTable.setItems(data);
        imagesNameCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");
        
        imagesIdCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");    
    }

    public static class DataModel {
        private final String imageName;
        private final String imageId;
        
        public DataModel(String imageName, String imageId) {
            this.imageName = imageName;
            this.imageId = imageId;
        }

        public String getImageName() {
            return imageName;
        }

        public String getImageId() {
            return imageId;
        }
    }
}

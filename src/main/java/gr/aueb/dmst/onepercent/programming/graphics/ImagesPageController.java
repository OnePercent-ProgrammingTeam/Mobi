package gr.aueb.dmst.onepercent.programming.graphics;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.core.SuperHttp;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

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

        removeCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button button = new Button();

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {     
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

            {
                button.setOnMouseClicked(event -> {
                    
                    Platform.runLater(() -> { //speed up the process
                        
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                       
                        ManagerHttpGUI managerHttpGUI = new ManagerHttpGUI();
                        ManagerHttpGUI.imageName = dataModel.getImageName();
                        SuperHttp superHttp = new SuperHttp();
                        superHttp.imageName = ManagerHttpGUI.imageName;
                        managerHttpGUI.removeImage();
                        data.remove(dataModel);
                        int removedIndex = getTableRow().getIndex();
                        imagesTable.getItems().remove(removedIndex);

                  
                    });
                }); 
            }
    
  
        });
        
    
       
        
        // Set the items for the TableView
        imagesTable.setItems(data);
        imagesNameCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");
        
        imagesIdCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;"); 
        removeCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee; -fx-min-width: 200px;");
        
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

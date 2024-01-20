package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;

public class ContainersPageController {

    @FXML
    private TableView<DataModel> containerTable;

    @FXML
    private TableColumn<DataModel, String> containerNameCol;

    @FXML
    private TableColumn<DataModel, String> containerIdCol;

    @FXML
    private TableColumn<DataModel, String> containerStatusCol;

    @FXML
    private TableColumn<DataModel, String> timeCreatedCol;

    @FXML
    private TableColumn<DataModel, Button> actionCol;

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Link columns to corresponding properties in DataModel
        containerNameCol.setCellValueFactory(new PropertyValueFactory<>("containerName"));
        containerIdCol.setCellValueFactory(new PropertyValueFactory<>("containerId"));
        containerStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        timeCreatedCol.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));
        //actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));


        //check
        actionCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button stopButton = new Button("Stop");

            {
                // Define the button's behavior
                stopButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        // Add your custom action when the button is clicked
                        System.out.println("Stop button clicked for container: " 
                            + dataModel.getContainerName());
                        // You can add more logic here as needed
                    }
                });
            }

            // @Override
            // protected void updateItem(Button item, boolean empty) {
            //     super.updateItem(item, empty);

            //     if (empty) {
            //         setGraphic(null);
            //     } else {
            //         ImageView imageView = new ImageView(new Image("/images/startButton.png"));
            //         imageView.setFitWidth(16); // Set the width as needed
            //         imageView.setFitHeight(16); // Set the height as needed

            //         // Set the image as the graphic of the button
            //         item.setGraphic(imageView);

            //         setGraphic(item);
            //     }
            // }
        });
        
        MonitorAPI monitor = new MonitorAPI();
        MonitorAPI.createDockerClient();
        monitor.initializeContainerModels(true);
        ArrayList<String> containerNameList = monitor.getNameList();
        ArrayList<String> containerIdList = monitor.getIdList();
        ArrayList<String> statusList = monitor.getStatusList();
        ArrayList<String> timeCreatedList = monitor.getTimeCreatedList();


        // Populate data from ArrayLists
        for (int i = 0; i < containerNameList.size(); i++) {
            data.add(new DataModel(
                    containerNameList.get(i),
                    containerIdList.get(i),
                    statusList.get(i),
                    timeCreatedList.get(i)
            ));
        }

        // Set the items for the TableView
        containerTable.setItems(data);
        containerNameCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee");
        containerIdCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee");
        containerStatusCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee");
        timeCreatedCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee");
        actionCol.setStyle("-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee");
    }

    public static class DataModel {
        private final String containerName;
        private final String containerId;
        private final String status;
        private final String timeCreated;

        public DataModel(String containerName, 
            String containerId, String status, String timeCreated) {
            this.containerName = containerName;
            this.containerId = containerId;
            this.status = status;
            this.timeCreated = timeCreated;
        }

        public String getContainerName() {
            return containerName;
        }

        public String getContainerId() {
            return containerId;
        }

        public String getStatus() {
            return status;
        }

        public String getTimeCreated() {
            return timeCreated;
        }
        
    }
}

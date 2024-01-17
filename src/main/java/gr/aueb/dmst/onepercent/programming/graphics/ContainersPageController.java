package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Link columns to corresponding properties in DataModel
        containerNameCol.setCellValueFactory(new PropertyValueFactory<>("containerName"));
        containerIdCol.setCellValueFactory(new PropertyValueFactory<>("containerId"));
        containerStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        timeCreatedCol.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));
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

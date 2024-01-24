package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.TableCell;

import java.util.ArrayList;

import org.controlsfx.control.ToggleSwitch;

import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;



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
    private TableColumn<DataModel, ToggleSwitch> actionCol;

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
                    timeCreatedList.get(i),
                    new ToggleSwitch()
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
 
        //check
        actionCol.setCellFactory(column -> new TableCell<DataModel, ToggleSwitch>() {
            private final ToggleSwitch toggle = new ToggleSwitch();

            @Override
            protected void updateItem(ToggleSwitch item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {
                    DataModel dataModel = getTableView().getItems().get(getIndex());

                    // Set button text based on container status
                    if (monitor.getContainerStatus(dataModel.getContainerId())) {
                        toggle.setSelected(true);
                        
                    } else {
                        toggle.setSelected(false);
                    }
                    setGraphic(toggle);
                }
            }

            {
                toggle.setOnMouseClicked(event -> {
                    
                    DataModel dataModel = getTableView().getItems().get(getIndex());
                    
                    ManagerHttpGUI.containerId = dataModel.getContainerId();

                    //monitor.initializeContainerModels(true);
                    if (toggle.isSelected()) {
                        MainGUI.menuThreadGUI.handleUserInputGUI(1);
                        dataModel.setStatus("Running");
                        getTableView().refresh();
                        System.out.println("toggle on");
                    } else {
                        MainGUI.menuThreadGUI.handleUserInputGUI(2);
                        System.out.println("toggle off");
                    }
                }); 
            }
    
  
        });
        
        
    }

    public static class DataModel {
        private final String containerName;
        private final String containerId;
        private String status;
        private final String timeCreated;
        private final ToggleSwitch action;

        public DataModel(String containerName, 
            String containerId, String status, String timeCreated, ToggleSwitch action) {
            this.containerName = containerName;
            this.containerId = containerId;
            this.status = status;
            this.timeCreated = timeCreated;
            this.action = action;
        }

        public String getContainerName() {
            return containerName;
        }

        public String getContainerId() {
            return containerId;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public String getTimeCreated() {
            return timeCreated;
        }

        public ToggleSwitch getAction() {
            return action;
        }
        
    }
}

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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import gr.aueb.dmst.onepercent.programming.core.ManagerHttp;
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

        
        String startpath = "src\\main\\resources\\images\\startButton.png";
        Path startimagePath = Paths.get(startpath);

        String stoppath = "src\\main\\resources\\images\\stopButton.png";
        Path stopimagePath = Paths.get(stoppath);
        
        

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
                    new Button("Stop")
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
        actionCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button stopButton = new Button();
            {
                stopButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        
                        ManagerHttp.containerId = dataModel.getContainerId();

                        //monitor.initializeContainerModels(true);
                        if (!monitor.getContainerStatus(dataModel.getContainerId())) {

                            stopButton.setStyle(stopButton.getStyle() 
                                                + "-fx-background-image: url('" 
                                                + stopimagePath.toUri().toString() + "'); ");

                            MainGUI.menuThreadGUI.handleUserInputGUI(1);
                        } else {

                            stopButton.setStyle(stopButton.getStyle() 
                                                + "-fx-background-image: url('" 
                                                + startimagePath.toUri().toString() + "'); ");


                            MainGUI.menuThreadGUI.handleUserInputGUI(2);
                        }
                    }
                });
                
            }
    
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {
                    DataModel dataModel = getTableView().getItems().get(getIndex());

                    // Set button text based on container status
                    if (monitor.getContainerStatus(dataModel.getContainerId())) {
                        //stopButton.setText("Stop");
                        stopButton.setPrefSize(25, 25);
                        stopButton.setStyle("-fx-background-image: url('" 
                                            + stopimagePath.toUri().toString() + "'); " 
                                            + "-fx-background-size: cover; " 
                                            + "-fx-background-repeat: no-repeat; "
                                            + "-fx-background-position: center; "
                                            + "-fx-padding: 0;"
                                            + "-fx-background-color: transparent;");
                    } else {
                        //stopButton.setText("Start");
                        stopButton.setPrefSize(25, 25);
                        stopButton.setStyle("-fx-background-image: url('" 
                                            + startimagePath.toUri().toString() + "'); " 
                                            + "-fx-background-size: cover; " 
                                            + "-fx-background-repeat: no-repeat; "
                                            + "-fx-background-position: center; "
                                            + "-fx-padding: 0;"
                                            + "-fx-background-color: transparent;");
                    }

                    setGraphic(stopButton);
                }
            }
        });
        
        
    }

    public static class DataModel {
        private final String containerName;
        private final String containerId;
        private final String status;
        private final String timeCreated;
        private final Button action;

        public DataModel(String containerName, 
            String containerId, String status, String timeCreated, Button action) {
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

        public String getStatus() {
            return status;
        }

        public String getTimeCreated() {
            return timeCreated;
        }

        public Button getAction() {
            return action;
        }
        
    }
}

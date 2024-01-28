package gr.aueb.dmst.onepercent.programming.graphics;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.chart.PieChart;

import javafx.scene.control.TableCell;

import javafx.scene.control.Button;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.controlsfx.control.ToggleSwitch;


import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.core.SuperHttp;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;


/**
 * ok
 */
public class ContainersPageController {

    /**ok */
    public ContainersPageController() { }

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

    @FXML
    private TableColumn<DataModel, Button> removeCol;

    @FXML
    private PieChart pieChart;

    @FXML
    private Text runningText;

    @FXML
    private Text exitedText;

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    /**
     * ok
     */
    @FXML
    public void initialize() {
       
        MonitorAPI monitor = new MonitorAPI();
        MonitorAPI.createDockerClient();
        setUpPieChart();
       // Link columns to corresponding properties in DataModel
        containerNameCol.setCellValueFactory(new PropertyValueFactory<>("containerName"));
        containerIdCol.setCellValueFactory(new PropertyValueFactory<>("containerId"));
        //containerStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        timeCreatedCol.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));    

       
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
                    (statusList.get(i).contains("Exited")) ? "Exited" : "Running", //custom status
                    timeCreatedList.get(i),
                    new ToggleSwitch(), new Button()
            ));
        }

        // Set the items for the TableView
        String columnStyle = "-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee";
        containerTable.setItems(data);
        containerNameCol.setStyle(columnStyle);
        containerIdCol.setStyle(columnStyle);
        containerStatusCol.setStyle(columnStyle);
        containerStatusCol.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        timeCreatedCol.setStyle(columnStyle);
        actionCol.setStyle(columnStyle);
        removeCol.setStyle(columnStyle);
        
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

                    //Set button text based on container status
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
                    
                    Platform.runLater(() -> { //speed up the process
                         
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        
                        ManagerHttpGUI.containerId = dataModel.getContainerId();
                        SuperHttp superHttp = new SuperHttp();
                        superHttp.setContainerId(ManagerHttpGUI.containerId);
                        //monitor.initializeContainerModels(true);
                        if (toggle.isSelected()) {
                            MainGUI.menuThreadGUI.handleUserInputGUI(1);
                            running++;
                            updatePieChart();
                            dataModel.setStatus("Running");
                        } else {
                            MainGUI.menuThreadGUI.handleUserInputGUI(2);
                            dataModel.setStatus("Exited");
                            running--;
                            updatePieChart();
                    
                        }
                    });
                }); 
            }
    
  
        });
        removeCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button removeButton = new Button();

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {     
                    String path = "src/main/resources/images/containersPage/remove-icon.png";
                    Path pathToFile = Paths.get(path);
                   
                    removeButton.setStyle("-fx-background-color: transparent; " +
                        "-fx-background-image: url('" + pathToFile.toUri().toString() + "'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: center center;" +
                        "-fx-background-size: 20px 20px;");
                    setAlignment(javafx.geometry.Pos.CENTER);
                    setGraphic(removeButton);
                }
            }

            {
                removeButton.setOnMouseClicked(event -> {
                    
                    Platform.runLater(() -> { //speed up the process
                        
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                       
                        ManagerHttpGUI managerHttpGUI = new ManagerHttpGUI();
                        ManagerHttpGUI.containerId = dataModel.getContainerId();
                        SuperHttp superHttp = new SuperHttp();
                        superHttp.containerId = ManagerHttpGUI.containerId;
                        managerHttpGUI.removeContainer();
                        data.remove(dataModel);
                        int removedIndex = getTableRow().getIndex();
                        containerTable.getItems().remove(removedIndex);

                  
                    });
                }); 
            }
    
  
        });
        
    }

    int all; //number of all containers
    int running; //number of running containers
    ObservableList<PieChart.Data> pieChartData; //data for pie chart
    private void setUpPieChart() {
        all = MonitorAPI.dc.listContainersCmd().withShowAll(true).exec().size();
        running = MonitorAPI.dc.listContainersCmd().withShowAll(false).exec().size();
        int stopped = all - running;
        /*initialize the labels of the chart pie */
        runningText.setText("Running: " + running);
        exitedText.setText("Exited: " + stopped);
        
        // Set pie chart data
        pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Running", running),
            new PieChart.Data("Stopped", stopped)
        );

        pieChartData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(data.getName(), data.pieValueProperty())
        ));
        pieChart.setLabelsVisible(false);
        pieChart.getData().addAll(pieChartData);
    }

    private void updatePieChart() {
        pieChartData.get(0).setPieValue(running);
        pieChartData.get(1).setPieValue(all - running);
        /*make sure pie chart doesn't change color when updated */
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: #6200ee");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: #bb66fc");
        /*update the labels of the chart pie*/
        runningText.setText("Running: " + running);
        exitedText.setText("Exited: " + (all - running));
    }

    /**
     * ok
     */
    public static class DataModel {
        private final String containerName;
        private final String containerId;
        private SimpleStringProperty status; 
        //SimpleStringProperty is used to observe changes in the property's value.
        private final String timeCreated;
        private final ToggleSwitch action;
        private final Button remove;

        /**
         * con
         * @param containerName ok
         * @param containerId ok
         * @param status ok
         * @param timeCreated ok
         * @param action ok
         * @param remove ok
         */
        public DataModel(String containerName, 
            String containerId, String status, String timeCreated,
            ToggleSwitch action, Button remove) {
            this.containerName = containerName;
            this.containerId = containerId;
            this.status = new SimpleStringProperty(status); 
            this.timeCreated = timeCreated;
            this.action = action;
            this.remove = remove;
        }

        /**
         * ok
         * @return ok
         */
        public String getContainerName() {
            return containerName;
        }

        /**
         * ok
         * @return ok
         */
        public String getContainerId() {
            return containerId;
        }

        /**
         * ok
         * @param status ok
         */
        public void setStatus(String status) {
            this.status.set(status);
        }

        /**
         * ok
         * @return ok
         */
        public StringProperty getStatus() {
            return status;
        }

        /**
         * ok
         * @return ok
         */
        public String getTimeCreated() {
            return timeCreated;
        }

        /**
         * ok
         * @return ok
         */
        public ToggleSwitch getAction() {
            return action;
        }

        public Button getRemoveButton() {
            return remove;
        }
    }
}

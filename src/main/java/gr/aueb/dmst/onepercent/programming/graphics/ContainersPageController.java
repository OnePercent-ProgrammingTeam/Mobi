package gr.aueb.dmst.onepercent.programming.graphics;

import gr.aueb.dmst.onepercent.programming.core.DockerInformationRetriever;
import gr.aueb.dmst.onepercent.programming.core.SystemController;
import gr.aueb.dmst.onepercent.programming.gui.ManagerGUI;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

import javafx.application.Platform;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.chart.PieChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.TableCell;

import org.controlsfx.control.ToggleSwitch;

/**
 * A UI controller that manages the user interface and functionalities of the containers page 
 * in the application. 
 * 
 * <p>This page features a table displaying information about containers, including
 * their names, IDs, status, and time of creation. Each row in the table also includes buttons to 
 * start/stop the containers and to remove them. Additionally, a pie chart next to the table 
 * dynamically updates to show the number of running and exited containers in real-time.
 * This class is part of the gr.aueb.dmst.onepercent.programming.graphics package.
 */
public class ContainersPageController {
    
    /** Number of all containers. */
    int all;
    
    /** Number of running containers. */
    int running;

    /** Data of pie chart. */
    ObservableList<PieChart.Data> pieChartData; //Data for pie chart
    
    /** The table containing the information about the containers. */
    @FXML
    private TableView<DataModel> containerTable;
    
    /** The column with the container names from the table. */
    @FXML
    private TableColumn<DataModel, String> containerNameCol;
    
    /** The column with the container ids from the table. */
    @FXML
    private TableColumn<DataModel, String> containerIdCol;
    
    /** The column with the container status from the table. */
    @FXML
    private TableColumn<DataModel, String> containerStatusCol;
    
    /** The column with the container time of creation from the table. */
    @FXML
    private TableColumn<DataModel, String> timeCreatedCol;
    
    /** The column with the toggle switches to start/stop the containers from the table. */
    @FXML
    private TableColumn<DataModel, ToggleSwitch> actionCol;
    
    /** The column with the buttons to remove containers from the table. */
    @FXML
    private TableColumn<DataModel, Button> removeCol;
    
    /** The pie chart showing running and exited containers. */
    @FXML
    private PieChart pieChart;
    
    /** The label of the pie chart for the running containers. */
    @FXML
    private Text runningText;
    
    /** The label of the pie chart for the exited containers. */
    @FXML
    private Text exitedText;
    
    /** The list with the customized data models (embedded class). */
    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    /** Default Constructor. */
    public ContainersPageController() { }

    /**
     * Set up the Container page with all it's UI components.
     */
    @FXML
    public void initialize() {
        DockerInformationRetriever monitor = new DockerInformationRetriever();
        DockerInformationRetriever.createDockerClient();
        setUpPieChart();
        //Create sell values so as to be feasible to add buttons inside the table.
        containerNameCol.setCellValueFactory(new PropertyValueFactory<>("containerName"));
        containerIdCol.setCellValueFactory(new PropertyValueFactory<>("containerId"));
        timeCreatedCol.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));    
        //Initialoize and retrieve info about all the locally installed containers.
        monitor.initializeContainerList(true);
        ArrayList<String> containerNameList = monitor.getContainerNames();
        ArrayList<String> containerIdList = monitor.getIdList();
        ArrayList<String> statusList = monitor.getStatusList();
        ArrayList<String> timeCreatedList = monitor.getTimeCreatedList();
        //Populate data from ArrayLists and build the customized Data models (embedded class).
        for (int i = 0; i < containerNameList.size(); i++) {
            data.add(new DataModel(
                    containerNameList.get(i),
                    containerIdList.get(i),
                    (statusList.get(i).contains("Exited")) ? "Exited" : "Running", //custom status
                    timeCreatedList.get(i),
                    new ToggleSwitch(), new Button()
            ));
        }
        containerTable.setItems(data); //Set the data to the table and customize the appearance.
        String columnStyle = "-fx-text-fill: #111111; -fx-font-family: Malgun Gothic;" +
            "-fx-font-size: 15px; -fx-background-color: #eee";
        containerNameCol.setStyle(columnStyle);
        containerIdCol.setStyle(columnStyle);
        containerStatusCol.setStyle(columnStyle);
        containerStatusCol.setCellValueFactory(cellData -> cellData.getValue().getStatus());
        timeCreatedCol.setStyle(columnStyle);
        actionCol.setStyle(columnStyle);
        removeCol.setStyle(columnStyle);
        /* Add toggle switch buttons to the cells of the action column of the table in order
         *to start / stop containers.
         */
        actionCol.setCellFactory(column -> new TableCell<DataModel, ToggleSwitch>() {
            private final ToggleSwitch toggle = new ToggleSwitch();
            @Override
            protected void updateItem(ToggleSwitch item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {
                    DataModel dataModel = getTableView().getItems().get(getIndex());
                    /* 
                     * Set button text based on container status.If it is running,
                     * then the toggle should be switched on, otherwise switched off.
                     */
                    if (monitor.isContainerRunning(dataModel.getContainerId())) {
                        toggle.setSelected(true);
                    } else {
                        toggle.setSelected(false);
                    }
                    setGraphic(toggle);
                }
            }
            {   /* Add functionality to the toggle switch so as when switched-on to start containers
                 and when switched-off to stop containers. */
                toggle.setOnMouseClicked(event -> {
                    Platform.runLater(() -> { //Secure concurrency.
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        ManagerGUI.containerId = dataModel.getContainerId();
                        SystemController superHttp = new SystemController();
                        superHttp.setContainerId(ManagerGUI.containerId);
                        /*if toggle switch is switched-on, start the container and update the pie
                         *chart showing running and exited containers. If switched-off, stop the
                         *container
                         */
                        if (toggle.isSelected()) {
                            MainGUI.menuThreadGUI.handleUserInput(1);
                            running++;
                            updatePieChart();
                            dataModel.setStatus("Running");
                        } else {
                            MainGUI.menuThreadGUI.handleUserInput(2);
                            dataModel.setStatus("Exited");
                            running--;
                            updatePieChart();
                        }
                    });
                }); 
            }
        });
        //Set a sell factory in order so as it is possible to put buttons in table.
        removeCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button removeButton = new Button();
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else { //Style the remove button.
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
            { //Add functionality  to the remove button.
                removeButton.setOnMouseClicked(event -> {
                    //Secure thread concurrency, related to UI components.
                    Platform.runLater(() -> {
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        ManagerGUI manager = new ManagerGUI();
                        ManagerGUI.containerId = dataModel.getContainerId();
                        SystemController superHttp = new SystemController();
                        superHttp.containerId = ManagerGUI.containerId;
                        manager.removeContainer();
                        data.remove(dataModel);
                        int removedIndex = getTableRow().getIndex();
                        containerTable.getItems().remove(removedIndex);
                    });
                }); 
            }
        }); 
    }
    
    private void setUpPieChart() {
        //Retrieve the quantity of locally installed containers, including running and exited.
        all = DockerInformationRetriever.dockerClient.listContainersCmd()
                                                     .withShowAll(true).exec().size();
        running = DockerInformationRetriever.dockerClient.listContainersCmd()
                                                     .withShowAll(false).exec().size();
        int stopped = all - running;
        /* Initialize the labels of the chart pie.*/
        runningText.setText("Running: " + running);
        exitedText.setText("Exited: " + stopped);
        /* Set the data needed for the pie chart. */
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

    /**
     * Updates the pie chart with the running containers.
     */
    private void updatePieChart() {
        pieChartData.get(0).setPieValue(running);
        pieChartData.get(1).setPieValue(all - running);
        /*Make sure pie chart doesn't change color when updated. */
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: #6200ee");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: #bb66fc");
        /*Update the labels of the chart pie. */
        runningText.setText("Running: " + running);
        exitedText.setText("Exited: " + (all - running));
    }

    /**
    * An embedded class representing a customized data model, needed for the table,
    * containing the container's name, id, status, time of creation, a button to start/ stop it
    * and a button to remove it.
    */
    public static class DataModel {
        
        /** The container name. */
        private final String containerName;
        /** The container id. */
        private final String containerId;
        /** The container status. */
        private SimpleStringProperty status; 
        /** The time a container was created. */
        private final String timeCreated;
        /** The toggle switch, used to stop/start containers. */
        private final ToggleSwitch action;
        /** The button, used to remove containers. */
        private final Button remove;

        /**
         * Constructor to initialize the fields.
         * @param containerName The container name.
         * @param containerId The container id.
         * @param status The container status.
         * @param timeCreated The time a container was created.
         * @param action The toggle switch, used to stop/start containers.
         * @param remove The button, used to remove containers.
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
         * Getter for the container name.
         * @return The container name.
         */
        public String getContainerName() {
            return containerName;
        }

        /**
         * Getter for the container ID.
         * @return The container ID.
         */
        public String getContainerId() {
            return containerId;
        }

        /**
         * Getter for the status of the container.
         * @return The status of the container.
         */
        public StringProperty getStatus() {
            return status;
        }

        /**
         * Getter for the time the container was created.
         * @return The time the container was created.
         */
        public String getTimeCreated() {
            return timeCreated;
        }

        /**
         * Getter for toggle switch that is used for starting/stopping the containers.
         * @return The toggle switch.
         */
        public ToggleSwitch getAction() {
            return action;
        }

        /**
         * Getter for remove button.
         * @return The remove button.
         */
        public Button getRemoveButton() {
            return remove;
        }

        /**
         * Setter for the status of the container.
         * @param status The status of the container.
         */
        public void setStatus(String status) {
            this.status.set(status);
        }
    }
}

package gr.aueb.dmst.onepercent.programming.graphics;

import gr.aueb.dmst.onepercent.programming.core.DockerInformationRetriever;
import gr.aueb.dmst.onepercent.programming.core.Monitor;
import gr.aueb.dmst.onepercent.programming.gui.MonitorGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * A UI controller of Analytics Page in the GUI of the application.
 * 
 * <p>It provides real-time monitoring of CPU and memory usage for selected containers, 
 * along with information about running containers and swarm details.
 */
public class AnalyticsPageController {
    
    /** The series-datapoints of CPU usage diagram. */
    XYChart.Series<String, Double> cpuSeries;
    /** The series-datapoints of memory usage diagram. */ 
    XYChart.Series<String, Double> memorySeries; 
    /** The response return from the HTTP request that was sent. */
    CloseableHttpResponse response;
    /** A MonitorHttpGUI instance for accessing the monitoring methods. */
    MonitorGUI monitor = new MonitorGUI();
    /** The button that was selected. */
    Button selectedButton;
    /** Indicates whether a button has been selected or not. */
    boolean hasSelected = false;

    /** The gateway address text used in rounded square blocks for container inspection. */
    @FXML
    private Text gatewayText;
    /** The ip address text used in rounded square blocks for container inspection. */
    @FXML
    private Text ipText;
    /** The mac address text used in rounded square blocks for container inspection. */
    @FXML
    private Text macText;
    /** The network id text used in rounded square blocks for container inspection. */
    @FXML
    private Text networkText;
    /** The chart that plots CPU usage. */
    @FXML
    private LineChart<String, Double> cpuChart;
    /** The chart that plots memory usage. */
    @FXML
    private LineChart<String, Double> memoryChart;
    /** The table that contains the running containers. */
    @FXML
    private TableView<DataModel> runningContainersTable;
    /** The column of the table with the name of the containers. */
    @FXML
    private TableColumn<DataModel, String> containerNameCol;
    /** The column of the table with the select buttons. */
    @FXML
    private TableColumn<DataModel, Button> selectionCol;
    /** The swarm name text used at the bottom of the page with swarm information. */
    @FXML
    private Text swarmName;
    /** The swarm id text used at the bottom of the page with swarm information. */
    @FXML
    private Text swarmId;
    /** The date of swarm creation text used at the bottom of the page with swarm information. */
    @FXML
    private Text swarmBorn;
    /** The date of swarm update text used at the bottom of the page with swarm information. */
    @FXML
    private Text swarmUpdated;
    /** The swarm subnet size text used at the bottom of the page with swarm information. */
    @FXML
    private Text swarmSubnetSize;
    /** The swarm version text used at the bottom of the page with swarm information. */
    @FXML
    private Text swarmVersion;
    /** A collection with data models, as we define them in the embedded class. */
    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    /** Default Constructor. */
    public AnalyticsPageController() { }


    /**
     * Initialize the page's UI components and define functionalities that should run.
     */
    @FXML
    public void initialize() {
        /** Set up the charts. */
        cpuSeries = new XYChart.Series<>();
        cpuSeries.setName("CPU USAGE");
        memorySeries = new XYChart.Series<>();
        memorySeries.setName("MEMORY USAGE");
        /* Set a cell factory in order to be able to add buttons to the cells of the table. */
        containerNameCol.setCellValueFactory(new PropertyValueFactory<>("containerName"));
        selectionCol.setCellValueFactory(new PropertyValueFactory<>("selection"));
        /* Create a instance of DockerInformationRetriever and a docker client. */
        DockerInformationRetriever retriever = new DockerInformationRetriever();
        DockerInformationRetriever.createDockerClient();
        /* 
         * Initialize the list with locally installed containers, containing only the running.
         * This is specified by using 'false' boolean value as a parameter.
         */
        retriever.initializeContainerList(false);
        ArrayList<String> containersNamesList = retriever.getContainerNames();
        ArrayList<String> containersIdList = retriever.getIdList();
        /* Create as many selection buttons as the running locally installed containers.*/
        for (int i = 0; i < containersNamesList.size(); i++) {
            data.add(new DataModel(
                    containersNamesList.get(i),
                    containersIdList.get(i),
                    new Button()));
        }
       /* 
        * Create a table cell for each running container in the appropriate column
        * of the table and then so as to put the buttons in. Then, add fuctionality to
        * the buttons. 
        */
        selectionCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button selectButton = new Button("Select");
            //When button is selected, change it's style and make the graphic visible
            //When button is unselected, change it'style and vanish the datapoints from it.
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {
                    selectButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-font-family: Malgun Gothic; -fx-font-size: 13px; " +
                        "-fx-background-radius: 25; -fx-text-fill: #6200ee;");
                    setAlignment(javafx.geometry.Pos.CENTER);
                    setGraphic(selectButton);
                }
            }
            {  //Add functionallity to the buttons.
                selectButton.setOnAction(event -> {
                    /* ensures that the UI updates occur on the correct thread to avoid concurrency
                     *issues.
                     */
                    Platform.runLater(() -> {
                        ObservableList<DataModel> items = getTableView().getItems();
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        /*
                         * Define what happens when user selects/unselects a button.
                         * If user tries to select two containers to plot their diagrams
                         * simultaneously, then make a pop appear indicating that it is an
                         * inappropriate action. The diagrams can appear for only one container
                         * a time.
                         */
                        if (selectButton.getText().equals("Select")) {
                            if (!hasSelected) {
                                hasSelected = true;
                            } else {
                                PopupController popupController = new PopupController();
                                popupController.showPopup(event);
                                return;
                            }
                            //Set the button to 'Selected', change it;s style and update the chart.
                            selectButton.setText("Selected");
                            selectButton.setStyle("-fx-background-color: transparent;" +
                                "-fx-font-family: Malgun Gothic; -fx-font-size: 13px; " +
                                "-fx-background-radius: 25; -fx-text-fill: #ddd;");
                            Monitor.containerId = dataModel.getContainerId();
                            updateContainerInfo();
                            response = monitor.getContainerStats("Graph");
                            startUpdatingChart(); 
                        } else {
                            hasSelected = false;
                            selectButton.setText("Select");
                            selectButton.setStyle("-fx-background-color: transparent;" +
                                "-fx-font-family: Malgun Gothic; -fx-font-size: 13px; " +
                                "-fx-background-radius: 25; -fx-text-fill: #6200ee;");
                            stopUpdatingChart();
                        }
                    });
                }); 
            }
        });
        // Set the items for the TableView
        runningContainersTable.setItems(data);
    }

    ScheduledExecutorService executorService;
    /**
     * Starts updating the chart. 
     */
    private void startUpdatingChart() {
        cpuSeries.getData().clear();
        memorySeries.getData().clear();
        cpuChart.getData().add(cpuSeries);
        memoryChart.getData().add(memorySeries);
        executorService = Executors.newSingleThreadScheduledExecutor();
        //The chart is updated every 5 seconds.
        executorService.scheduleAtFixedRate(() -> {
            updateChartData();
        }, 0, 5, TimeUnit.SECONDS);
        
    }

    /**
     * Stops updating and clears the chart when the container is unselected.
     */
    private void stopUpdatingChart() {
        /* when user unselects the button, 
         * the charts are cleared and data points are vanished from both
         * CPU and memory charts.
         */
        cpuSeries
            .getData()
            .removeAll(Collections.singleton(cpuChart.getData().setAll()));
        memorySeries
            .getData()
            .removeAll(Collections.singleton(memoryChart.getData().setAll()));
        executorService.shutdown();
        try {
            response.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Updates the charts with new datapoints.
     */
    private void updateChartData() {

        //Add new data points to the series.
        long currentTimestamp = System.currentTimeMillis();
        //Format the timestamp to display hours, minutes and seconds.
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = dateFormat.format(new Date(currentTimestamp));
        //Securing thread-concurrency issues.
        Platform.runLater(() -> {
            double[] stats = statsPlot();
            cpuSeries.getData().add(new XYChart.Data<>(formattedTime, stats[0]));
            memorySeries.getData().add(new XYChart.Data<>(formattedTime, stats[1]));
            /* Remove old datapoints to keep the chart from getting too long, 
             * always show the latest updates
             */
            if (cpuSeries.getData().size() > 10) {
                cpuSeries.getData().remove(0);
            }
            if (memorySeries.getData().size() > 10) {
                memorySeries.getData().remove(0);
            }
        });
    }

   /**
    * Returns CPU and Memory usage, after being estimated.
    * @return A array of doubles, containing two elements: CPU usage and memory usage.
    */
    public double[] statsPlot() {
        //Gather real-time data.
        BufferedReader reader;
        double cpu_usage = 0;
        double memory_usage = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine = reader.readLine(); // Read a new line from the response
            if (inputLine != null) {
                cpu_usage = monitor.getCPUusage(new StringBuilder(inputLine));
                memory_usage = monitor.getMemoryUsage(new StringBuilder(inputLine));
            } else {
                /* No more lines to read, close the reader and cancel the timer
                 * This is executed only if there are no more responses .
                 */
                reader.close();
            }                                                             
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        double[] stats = {cpu_usage, memory_usage};
        return stats;
    }

    /**
     * Updates the rounded squared boxes, containing information about containers.
     */
    private void updateContainerInfo() {
        Platform.runLater(() -> {
            MainGUI.menuThreadGUI.handleUserInput(6);
            String[] containerInfo = MonitorThreadGUI.getInstance()
                .getMonitorInstance().getContainerInfo();
            ipText.setText(containerInfo[0]);
            macText.setText(containerInfo[1]);
            networkText.setText(containerInfo[2].substring(0, 10) + "...");
            gatewayText.setText(containerInfo[3]);
        });
    }

    /* Methods used to let user copy the information appearing in the rounded boxes by one click. */
    
    /** Copies IP Address. */
    @FXML
    void copyIp() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(ipText.getText());
        clipboard.setContents(stringSelection, null);
    }

    /** Copies Mac Address. */
    @FXML
    void copyMac() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(macText.getText());
        clipboard.setContents(stringSelection, null);
    }

    /** Copies Network ID. */
    @FXML
    void copyNetwork() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(monitor.getContainerInfo()[2]);
        clipboard.setContents(stringSelection, null);
    }

    /** Copies Gateway Address. */
    @FXML
    void copyGateway() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(gatewayText.getText());
        clipboard.setContents(stringSelection, null);
    }


    /** Retrieves and shows swarm information in the bottom table of the page. */
    @FXML
    void getSwarmInfo() {
        monitor.inspectSwarm();
        String[] swarmInfo = monitor.getSwarmInfo();
        swarmName.setText(swarmInfo[0]);
        swarmId.setText(swarmInfo[1].substring(0, 10) + "...");
        swarmVersion.setText(swarmInfo[2]);
        swarmBorn.setText(swarmInfo[3]);
        swarmUpdated.setText(swarmInfo[4]);
        swarmSubnetSize.setText(swarmInfo[5]);
    }

    /** Loads Help Page, when the text pointing to it is clicked. */
    @FXML
    void loadHelpPage() {
        LoginPageController.MAIN_PAGE_CONTROLLER.loadPage("HelpPage.fxml");  
    }

   /**
    * An embedded class representing a customized data model, needed for the diagram and table,
    * containg the running containers and the button to plot their resource consumption.
    */
    public static class DataModel {
        /** Container's name. */
        private final String containerName;
        /** Container's id. */
        private final String containerId;
        /** Button for selecting/unselecting the specific container. */
        private final Button selection;
        
        /**
         * Constructor to initialize fields.
         * @param containerName The name of the container.
         * @param containerId The id of the container.
         * @param selection The button for selecting/unselecting the specific container.
         */
        public DataModel(String containerName, String containerId, Button selection) {
            this.containerName = containerName;
            this.containerId = containerId;
            this.selection = selection;
        }

        /**
         * Getter for container name.
         * @return container name.
         */
        public String getContainerName() {
            return containerName;
        }

        /**
         * Getter for container id.
         * @return container id.
         */
        public String getContainerId() {
            return containerId;
        }

        /**
         * Getter for container button.
         * @return selection button for container
         */
        public Button getSelection() {
            return selection;
        }
    }
}

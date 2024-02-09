package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;

import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javafx.scene.text.Text;


/**
 * ok
 */

public class AnalyticsPageController {

    /** ok */
    public AnalyticsPageController() { }
    @FXML
    private Text gatewayText;

    @FXML
    private Text ipText;

    @FXML
    private Text macText;

    @FXML
    private Text networkText;

    @FXML
    private LineChart<String, Double> cpuChart;

    @FXML
    private LineChart<String, Double> memoryChart;

    @FXML
    private TableView<DataModel> runningContainersTable;

    @FXML
    private TableColumn<DataModel, String> containerNameCol;

    @FXML
    private TableColumn<DataModel, Button> selectionCol;

    @FXML
    private Text swarmName;

    
    @FXML
    private Text swarmId;

    
    @FXML
    private Text swarmBorn;

    
    @FXML
    private Text swarmUpdated;

    
    @FXML
    private Text swarmSubnetSize;

    
    @FXML
    private Text swarmVersion;

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    // Other injected components go here
    XYChart.Series<String, Double> cpuSeries; 
    XYChart.Series<String, Double> memorySeries; 

    CloseableHttpResponse response;

    MonitorHttpGUI monitorHttpGUI = new MonitorHttpGUI();

    Button selectedButton;
    boolean hasSelected = false;


    /**
     * ok
     */
    @FXML
    public void initialize() {
        // Set up the line chart
        cpuSeries = new XYChart.Series<>();
        cpuSeries.setName("CPU USAGE");

        memorySeries = new XYChart.Series<>();
        memorySeries.setName("MEMORY USAGE");

        // Table
        // Link columns to corresponding properties in DataModel
        containerNameCol.setCellValueFactory(new PropertyValueFactory<>("containerName"));
        selectionCol.setCellValueFactory(new PropertyValueFactory<>("selection"));

        MonitorAPI monitor = new MonitorAPI();
        MonitorAPI.createDockerClient();

        monitor.initializeContainerModels(false);
        ArrayList<String> containersNamesList = monitor.getNameList();
        ArrayList<String> containersIdList = monitor.getIdList();

        // Populate data from ArrayLists
        for (int i = 0; i < containersNamesList.size(); i++) {
            data.add(new DataModel(
                    containersNamesList.get(i),
                    containersIdList.get(i),
                    new Button()));
        }

       
        selectionCol.setCellFactory(column -> new TableCell<DataModel, Button>() {
            private final Button selectButton = new Button("Select");

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

            {
                selectButton.setOnAction(event -> {
                    
                    Platform.runLater(() -> { //speed up the process

                        ObservableList<DataModel> items = getTableView().getItems();


                        DataModel dataModel = getTableView().getItems().get(getIndex());
                       
                        if (selectButton.getText().equals("Select")) {
                            
                            if (!hasSelected) {
                                hasSelected = true;
                            } else {
                                PopupController popupController = new PopupController();
                                popupController.showPopup(event);
                                return;
                            }
                            
                            selectButton.setText("Selected");
                            selectButton.setStyle("-fx-background-color: transparent;" +
                                "-fx-font-family: Malgun Gothic; -fx-font-size: 13px; " +
                                "-fx-background-radius: 25; -fx-text-fill: #ddd;");
                            MonitorHttp.containerId = 
                                dataModel.getContainerId();
                            updateContainerInfo();
                            response = monitorHttpGUI.getContainerStats("Graph");
                           
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


    /**
     * ok
     */
    public static class DataModel {
        private final String containerName;
        private final String containerId;
        private final Button selection;
        

        /**
         * ok
         * @param containerName ok
         * @param containerId ok
         * @param selection ok
         */
        public DataModel(String containerName, String containerId, Button selection) {
            this.containerName = containerName;
            this.containerId = containerId;
            this.selection = selection;
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
         * @return ok
         */
        public Button getSelection() {
            return selection;
        }
    }

    /**
     * ok
     */
    public class HttpRequestTask extends Task<String> {     


        /** ok */
        HttpRequestTask() {
            
        }


        @Override    
        protected String call() throws Exception {         
            // Perform your HTTP GET request here        
            return null;
        } 
    }

    ScheduledExecutorService executorService;

    private void startUpdatingChart() {
        cpuSeries.getData().clear();
        memorySeries.getData().clear();

        cpuChart.getData().add(cpuSeries);
        memoryChart.getData().add(memorySeries);
        System.out.println("Updating chart");

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            updateChartData();
        }, 0, 5, TimeUnit.SECONDS);
        
    }

    private void stopUpdatingChart() {
        /*when user unselects the button, 
        the charts are cleared, data points are vanished
        Stack Overflow solution: 
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


    private void updateChartData() {

        // Add new data points to the series
        // For example:
        long currentTimestamp = System.currentTimeMillis();
        // Format the timestamp to display hours, minutes, and seconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = dateFormat.format(new Date(currentTimestamp));
        

        Platform.runLater(() -> {
            //XYChart.Series<String, Number> series = lineChart.getData().get(0);
            double[] stats = statsPlot();
            cpuSeries.getData().add(new XYChart.Data<>(formattedTime, stats[0]));
            memorySeries.getData().add(new XYChart.Data<>(formattedTime, stats[1]));
            //Math.random() * 100

            // Remove old data points to keep the chart from getting too long
            if (cpuSeries.getData().size() > 10) {
                cpuSeries.getData().remove(0);
            }
            if (memorySeries.getData().size() > 10) {
                memorySeries.getData().remove(0);
            }
        });
    }


    /**
     * ok
     * @return ok
     */
    public double[] statsPlot() {
        // Simulate real-time data update every second
        BufferedReader reader;
        double cpu_usage = 0;
        double memory_usage = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine = reader.readLine(); // Read a new line from the response
            if (inputLine != null) {
                cpu_usage = monitorHttpGUI.getCPUusage(new StringBuilder(inputLine));
                memory_usage = monitorHttpGUI.getMemoryUsage(new StringBuilder(inputLine));
            } else {
                // No more lines to read, close the reader and cancel the timer
                //This is executed only if there are not more responses 
                reader.close();
            }                                                             

        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        double[] stats = {cpu_usage, memory_usage};
        return stats;
    }


    private void updateContainerInfo() {
        Platform.runLater(() -> {
            MainGUI.menuThreadGUI.handleUserInputGUI(6);
            String[] containerInfo = MonitorThreadGUI.getInstance()
                .getContainerMonitorHttp().getContainerInfo();
            ipText.setText(containerInfo[0]);
            macText.setText(containerInfo[1]);
            networkText.setText(containerInfo[2].substring(0, 10) + "...");
            gatewayText.setText(containerInfo[3]);
        });
       
        // System.out.println("Updating container info");
        // MonitorThreadGUI monitorThreadGUI = MonitorThreadGUI.getInstance();
        
    }

    @FXML
    void copyIp() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(ipText.getText());
        clipboard.setContents(stringSelection, null);
    }

    @FXML
    void copyMac() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(macText.getText());
        clipboard.setContents(stringSelection, null);
    }

    @FXML
    void copyNetwork() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(monitorHttpGUI.getContainerInfo()[2]);
        clipboard.setContents(stringSelection, null);
    }

    @FXML
    void copyGateway() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(gatewayText.getText());
        clipboard.setContents(stringSelection, null);
    }


    @FXML
    void getSwarmInfo() {
        monitorHttpGUI.inspectSwarm();
        String[] swarmInfo = monitorHttpGUI.getSwarmInfo();
        swarmName.setText(swarmInfo[0]);
        swarmId.setText(swarmInfo[1].substring(0, 10) + "...");
        swarmVersion.setText(swarmInfo[2]);
        swarmBorn.setText(swarmInfo[3]);
        swarmUpdated.setText(swarmInfo[4]);
        swarmSubnetSize.setText(swarmInfo[5]);
    }

    @FXML
    void loadHelpPage() {
        LoginPageController.MAIN_PAGE_CONTROLLER.loadPage("HelpPage.fxml");  
    }
}


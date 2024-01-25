package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;

import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;
import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
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

public class AnalyticsPageController {
    @FXML
    private LineChart<String, Double> lineChart;

    @FXML
    private TableView<DataModel> runningContainersTable;

    @FXML
    private TableColumn<DataModel, String> containerNameCol;

    @FXML
    private TableColumn<DataModel, Button> selectionCol;

    private ObservableList<DataModel> data = FXCollections.observableArrayList();

    // Other injected components go here
    XYChart.Series<String, Double> series; 

    CloseableHttpResponse response;

    MonitorHttpGUI monitorHttpGUI = new MonitorHttpGUI();

    @FXML
    public void initialize() {
        // Set up the line chart
        series = new XYChart.Series<>();
        series.setName("CPU USAGE");
        // Axis<Double> yAxis = (Axis<Double>) lineChart.getYAxis();
        // yAxis.setAutoRanging(false); // Disable auto-ranging
        // yAxis.boundsInLocalProperty().(0.0);     // Set lower bound
        // yAxis.setUpperBound(10.0);    // Set upper bound
        lineChart.getData().add(series);

        //lineChart.setCreateSymbols(false);
        /*  Set the line color using CSS style
        series.getNode().setStyle("-fx-stroke: #00ff00; " 
                                + "-fx-chart-symbol: square; "
                                + "-fx-symbol-size: 20px; "
                                + "-fx-chart-series-symbol-color: #ff0000;");
        */

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
                    setGraphic(selectButton);
                }
            }

            {
                selectButton.setOnAction(event -> {
                    
                    Platform.runLater(() -> { //speed up the process

                         
                        DataModel dataModel = getTableView().getItems().get(getIndex());
                        
                        ManagerHttpGUI.containerId = dataModel.getContainerId();
                        //monitor.initializeContainerModels(true);
                        if (selectButton.getText().equals("Select")) {
                            selectButton.setText("Selected");
                            selectButton.setStyle("-fx-background-color: transparent;" +
                                "-fx-font-family: Malgun Gothic; -fx-font-size: 13px; " +
                                "-fx-background-radius: 25; -fx-text-fill: #ddd;");
                            System.out.println(dataModel.getContainerId());
                            MonitorHttp.containerId = 
                                "11b7ed24d1b9317d3c38bd4a4b26962fe2b71d2fb023f5d0af2dc66db26fed08";
                            response = monitorHttpGUI.getContainerStats("Graph");
                            startUpdatingChart();
                        } else {
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

    public static class DataModel {
        private final String containerName;
        private final String containerId;
        private final Button selection;
        
        public DataModel(String containerName, String containerId, Button selection) {
            this.containerName = containerName;
            this.containerId = containerId;
            this.selection = selection;
        }

        public String getContainerName() {
            return containerName;
        }

        public String getContainerId() {
            return containerId;
        }

        public Button getSelection() {
            return selection;
        }
    }

    ScheduledExecutorService executorService;

    private void startUpdatingChart() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            updateChartData();
        }, 0, 5, TimeUnit.SECONDS);
        
    }

    private void stopUpdatingChart() {
        executorService.shutdown();
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
            series.getData().add(new XYChart.Data<>(formattedTime, statsPlot()));
            //Math.random() * 100

            // Remove old data points to keep the chart from getting too long
            if (series.getData().size() > 10) {
                series.getData().remove(0);
            }
        });


    }



    public double statsPlot() {
        // Simulate real-time data update every second
        BufferedReader reader;
        double usage = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine = reader.readLine(); // Read a new line from the response
            if (inputLine != null) {
                usage = monitorHttpGUI.getFormattedStats(new StringBuilder(inputLine));
                
            } else {
                // No more lines to read, close the reader and cancel the timer
                //This is executed only if there are not more responses 
                reader.close();
            }                                                             

        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return usage;
    }

}


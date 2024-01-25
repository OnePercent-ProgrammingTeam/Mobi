package gr.aueb.dmst.onepercent.programming.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;
import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class AnalyticsPageController {
    @FXML
    private LineChart<String, Double> lineChart;

    // Other injected components go here
    XYChart.Series<String, Double> series; 

    CloseableHttpResponse response;

    MonitorHttpGUI monitorHttpGUI = new MonitorHttpGUI();

    @FXML
    public void initialize() {
        // Set up the line chart
        series = new XYChart.Series<>();
        series.setName("CPU USAGE");
        lineChart.getData().add(series);

        //lineChart.setCreateSymbols(false);
        /*  Set the line color using CSS style
        series.getNode().setStyle("-fx-stroke: #00ff00; " 
                                + "-fx-chart-symbol: square; "
                                + "-fx-symbol-size: 20px; "
                                + "-fx-chart-series-symbol-color: #ff0000;");
        */

        //my container
        MonitorHttp.containerId = 
                    "7bc75769bff7ae780bff74c743a0ecc9128e7c1f3253964f4111ce9fb2605a1c";
        response = monitorHttpGUI.getContainerStats("Graph");
        startUpdatingChart();
        
    }


    private void startUpdatingChart() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            updateChartData();
        }, 0, 1, TimeUnit.SECONDS);
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


import org.apache.http.client.methods.CloseableHttpResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jfree.data.time.Millisecond;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ContainerVisualization extends JFrame {
    
    private XYSeries usageSeries;

    public ContainerVisualization(String title) {
        super(title);

        // Create series/ axis for the CPU usage
        usageSeries = new XYSeries("CPU Usage");

        XYSeriesCollection dataset = new XYSeriesCollection(usageSeries);

        // Create a chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Container Stats",
                "Time",
                "Usage",
                dataset
        );

        // Customize chart setting backround color 
        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot(); // Get the plot from the chart, casting it to XYPlot because it may be a subclass
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis(); // Get the domain axis from the plot, casting it to DateAxis because it may be a subclass
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1)); // Set the tick unit to be 1 second (it is a real time chart, so we want to update every second)

        // Create Panel, panel is 
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }

    // Update the stats with the new data point
    private void updateStats(long timestamp, double usage) {
        // Add new data point to the series
        long timestampInMilliseconds = new Date(timestamp).getTime(); // Convert the timestamp to milliseconds, type long
        usageSeries.addOrUpdate(timestampInMilliseconds, usage);
        /*addOrUpdate() method will add a new data point if it doesn't exist, or update the existing one if it does
          the parameters it gets are the x and y values of the data point (should be double)*/
    }

    public void startUpdatingStats(CloseableHttpResponse response, ContainerVisualization ex) throws UnsupportedOperationException, IOException {
        // Simulate real-time data update every second
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    
        Timer timer = new Timer(true); // Create a new timer
        timer.scheduleAtFixedRate(new TimerTask() { // Schedule a task to run every second
            
            boolean flag = false;
    
            @Override
            public void run() {
                try {
                    onRunGraph( flag, reader, ex, timer);
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000); // Update every second (1000 milliseconds = 1 second) 
    }
    
    public void onRunGraph (boolean flag, BufferedReader reader, ContainerVisualization ex, Timer timer) throws IOException{
        String inputLine = reader.readLine(); // Read a new line from the response
                    if (inputLine != null) {
                        double d = ContainerMonitorHttp.getFormattedStats(new StringBuffer(inputLine));
                        long currentTimestamp = System.currentTimeMillis(); // Get the current timestamp
                        updateStats(currentTimestamp, d);
                        if (!flag) {
                            ex.setVisible(true);
                            flag = true;
                        }
                    } else {
                        // No more lines to read, close the reader and cancel the timer
                        reader.close();
                        timer.cancel();
                        System.out.println("FINISHED");
                    }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Create a new thread to run the GUI
            ContainerVisualization example = new ContainerVisualization("Container Stats Plotter"); // Create a new ContainerVisualization object
            example.setSize(800, 600);  // Set the size of the window
            example.setLocationRelativeTo(null); // Center the window
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Set the close operation
            CloseableHttpResponse res = ContainerMonitorHttp.getContainerStats();
           
            try {
                example.startUpdatingStats(res, example); // Start updating the stats
                 // Make the window visible to the end user
                
            } catch (UnsupportedOperationException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // Start updating the stats
            //example.setVisible(true); // Make the window visible to the end user 
        });
    }
}

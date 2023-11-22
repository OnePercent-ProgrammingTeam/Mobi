package gr.aueb.dmst.onepercent.programming;
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
    
    /** Represents a sequence of zero or more data items in the form (x, y) */
    private XYSeries usageSeries;

    /** Constructor 
     * @param title the title of the window
     * @variable dataset a collection of XYSeries objects that can be used as a dataset
    */
    public ContainerVisualization(String title) {
        super(title);
        usageSeries = new XYSeries("CPU Usage");
        XYSeriesCollection dataset = new XYSeriesCollection(usageSeries);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Container Stats",
                "Time",
                "Usage",
                dataset
        ); 
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot(); // Get the plot from the chart, casting it to XYPlot because it may be a subclass
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis(); // Get the domain axis from the plot, casting it to DateAxis because it may be a subclass
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1)); // Set the tick unit to be 1 second (it is a real time chart, so we want to update every second)

        // The GUI component to make the chart visible to the end user
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }

    /** Update the stats with the new data point*/
    private void updateStats(long timestamp, double usage) {
        // Add new data point to the series
        long timestampInMilliseconds = new Date(timestamp).getTime(); // Convert the timestamp to milliseconds, type long
        usageSeries.addOrUpdate(timestampInMilliseconds, usage);
        /*addOrUpdate() method will add a new data point if it doesn't exist, or update the existing one if it does
          the parameters it gets are the x and y values of the data point (should be double)*/
    }

    /** Start updating the stats in order to plot real time data-the consume of cpu resources every second */
    public void startUpdatingStats(CloseableHttpResponse response, ContainerVisualization ex) throws UnsupportedOperationException, IOException {
        // Simulate real-time data update every second
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    
        Timer timer = new Timer(true); // Create a new timer
        timer.scheduleAtFixedRate(new TimerTask() { // Schedule a task to run every second
             boolean flag = false;
            
             @Override
            public void run() { // The task to run, this will be executed every second
                try {
                    onRunGraph( flag, reader, ex, timer);
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000); // Update every second (1000 milliseconds = 1 second) 
    }
    
    /** Run the graph
     * @param flag a boolean variable
     * @param reader a BufferedReader object
     * @param ex a ContainerVisualization object
     * @param timer a Timer object
     * @throws IOException if an error occurs while reading the input
     */
    public void onRunGraph (boolean flag, BufferedReader reader, ContainerVisualization ex, Timer timer) throws IOException{
        String inputLine = reader.readLine(); // Read a new line from the response
                    if (inputLine != null) {
                        double d = ContainerMonitorHttp.getFormattedStats(new StringBuffer(inputLine));
                        long currentTimestamp = System.currentTimeMillis(); // Get the current timestamp
                        updateStats(currentTimestamp, d);
                        if (!flag) {
                            ex.setVisible(true); // Make the window visible to the end user, we use flag because we want to make the window visible only once
                            flag = true;
                        }
                    } else {
                        // No more lines to read, close the reader and cancel the timer
                        reader.close();
                        timer.cancel();
                        System.out.println("FINISHED");
                    }
    }
    
    /** Main method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Create a new thread to run the GUI
            ContainerVisualization example = new ContainerVisualization("Container Stats Plotter"); // Create a new ContainerVisualization object
            example.setSize(800, 600);  // Set the size of the window
            example.setLocationRelativeTo(null); // Center the window
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Set the close operation, so that the application exits when the window is closed
            ContainerMonitorHttp containerMonitor = new ContainerMonitorHttp();
            CloseableHttpResponse res = containerMonitor.getContainerStats();
           
            try {
                example.startUpdatingStats(res, example); // Start updating the stats
            } catch (UnsupportedOperationException | IOException e) {
                e.printStackTrace();
            } // Start updating the stats
        });
    }
}

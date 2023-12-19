package gr.aueb.dmst.onepercent.programming;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Class: Graph class is responsible for the creation of the graph that shows the cpu usage of the container
 *  in real time. It uses the JFreeChart library to create the graph.
 *  The graph is created in a new window.
 */
public class Graph extends JFrame {
    
    /** Field XYSeries represents a sequence of zero or more data items in the form (x, y) */
    private XYSeries usageSeries;

    /** Field: monitorHttp is a MonitorHttp object */
    static MonitorHttp monitorHttp = new MonitorHttp();

    /** Constructor:  
     * @param title the title of the window
     * @variable dataset a collection of XYSeries objects that can be used as a dataset
    */
    public Graph(String title) {
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); // Create a date format
        dateAxis.setDateFormatOverride(dateFormat); // Set the date format to be the one we just created
        // The GUI component to make the chart visible to the end user
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }

    /** Method: updateStats(double) updates the stats with the new data point
     * @param usage the cpu usage of the container
    */
    private void updateStats(double usage) {
        // Add new data point to the series
        long currentTimestamp = System.currentTimeMillis(); // Get the current timestamp
        long timestampInMilliseconds = new Date(currentTimestamp).getTime(); // Convert the timestamp to milliseconds, type long
        usageSeries.addOrUpdate(timestampInMilliseconds, usage);
        
        /*addOrUpdate() method will add a new data point if it doesn't exist, or update the existing one if it does,
          the parameters it gets are the x and y values of the data point (should be double)*/
    }

    /** Method: statsPlot(CloseableHttpResponse) starts updating the stats in order
     *  to plot real time data-the consume of cpu resources every second
     *  @param response a CloseableHttpResponse object 
     * */
    public void statsPlot(CloseableHttpResponse response, Graph ex) throws UnsupportedOperationException, IOException {
        // Simulate real-time data update every second
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    
        Timer timer = new Timer(false); // Create a new timer
        timer.scheduleAtFixedRate(new TimerTask() { // Schedule a task to run every second
            @Override
            public void run() { // The task to run, this will be executed every second
                try {
                    onRunGraph(reader, ex, timer);
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000); // Update every second (1000 milliseconds = 1 second) 
    }
    
    /** Method: onRunGraph(BufferedReader, Graph, Timer) runs the graph
     * @param reader a BufferedReader object
     * @param ex a Graph object
     * @param timer a Timer object
     * @throws IOException if an error occurs while reading the input
     */
    public void onRunGraph(BufferedReader reader, Graph ex, Timer timer) throws IOException {
        String inputLine = reader.readLine(); // Read a new line from the response
        if (inputLine != null) {
            double usage = monitorHttp.getFormattedStats(new StringBuffer(inputLine)); 
            updateStats(usage);
        } else {
            // No more lines to read, close the reader and cancel the timer
            //This is executed only if there are not more responses 
            reader.close();
            timer.cancel();
        }
    }
    
    /** Method: executeDiagram() executes the diagram */
    public static void executeDiagram() {

        Graph cv = new Graph("Container Stats Plotter"); // Create a new Graph object
        cv.setSize(800, 600);  // Set the size of the window
        cv.setLocationRelativeTo(null); // Center the window
        cv.setDefaultCloseOperation(Graph.DO_NOTHING_ON_CLOSE); // Set the close operation, so that the application exits when the window is closed
        CloseableHttpResponse res = monitorHttp.getContainerStats("Graph");
           
        cv.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(cv);
            }
        });

        cv.setVisible(true); // Make the window visible to the end user
        
        try {
            cv.statsPlot(res, cv); // Start updating the stats
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }  
    }
    
    /** Method: closeWindow(JFrame) closes the window
     * @param frame a JFrame object
     */
    private static void closeWindow(JFrame frame) {
        frame.setVisible(false);
    }
}

package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI;
import gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

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



/** Class: Graph class is responsible for the creation of the graph 
 * that shows the cpu usage of the container in real time. 
 * It uses the JFreeChart library to create the graph. The graph 
 * is created in a new window.
 */

public class Graph extends JFrame {

    private static Graph graph;

    private Graph() { }

    /**
     * Method: getInstance() gets the singleton instance of the Graph class.
     *
     * @return the singleton instance of the Graph class
     */
    public static Graph getInstance() {
        if (graph == null) {
            graph = new Graph();
        }
        return graph;
    }


    
    /** Field XYSeries represents a sequence of zero or more data items in the form (x, y). */
    private XYSeries usageSeries;

    private static boolean flag;

    /**
     * Field: end represents whether the graph should end or continue updating.
     */
    public static boolean end;

    /** Field: monitorHttp is a MonitorHttp object. */
    static MonitorHttpCLI monitorHttpCLI = new MonitorHttpCLI();

    /**
     * Constructor:
     *
     * @param title the title of the window
     * Dataset is a collection of XYSeries objects that can be used as a dataset
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
        // Get the plot from the chart, casting it to XYPlot because it may be a subclass
        XYPlot plot = (XYPlot) chart.getPlot(); 
        // Get the domain axis from the plot, casting it to DateAxis because it may be a subclass
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis(); 
        /*Set the tick unit to be 1 second (it is a real time chart, 
        so we want to update every second).*/
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1)); 

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); // Create a date format
        // Set the date format to be the one we just created
        dateAxis.setDateFormatOverride(dateFormat); 
        // The GUI component to make the chart visible to the end user
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }


    /** Method: updateStats(double) updates the stats with the new data point.
     * @param usage the cpu usage of the container
    */
    private void updateStats(double usage) {
        // Add new data point to the series
        long currentTimestamp = System.currentTimeMillis(); // Get the current timestamp
        // Convert the timestamp to milliseconds, type long
        long timestampInMilliseconds = new Date(currentTimestamp).getTime(); 
        usageSeries.addOrUpdate(timestampInMilliseconds, usage);

        /*addOrUpdate() method will add a new data point if it doesn't exist, 
        or update the existing one if it does, the parameters it gets are the x and y
        values of the data point (should be double).*/
    
    }


    

    /** Method: statsPlot(CloseableHttpResponse) starts updating the stats in order
     *  to plot real time data-the consume of cpu resources every second.
     *  @param response a CloseableHttpResponse object 
     *  @param ex a Graph object
     *  @throws IOException if an error occurs while reading the input
     */
    public void statsPlot(CloseableHttpResponse response, Graph ex) 
        throws UnsupportedOperationException, IOException {
        // Simulate real-time data update every second
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()
                                                                                 .getContent()));
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
            double usage = monitorHttpCLI.getFormattedStats(new StringBuilder(inputLine));
            updateStats(usage);

            if (flag == false) {
              /*  LocalDate date = LocalDate.now(); 
                LocalTime time = LocalTime.now();
                DataBase database = new DataBase();

                //Date and Time in one
                String datetime = date.toString() + " " + time.toString().substring(0, 8);
                int last_id = database.insertMetricsToDatabase(datetime);
                database.insertMeasureToDatabase(last_id, usage);
                database.getSomeMetrics(last_id); //helpful
                database.getSomeMeasure(last_id); //helpful*/
            } else {
                //reader.close();
                timer.cancel();
                end = true;
            }
            
            
        } else {
            // No more lines to read, close the reader and cancel the timer
            //This is executed only if there are not more responses 
            reader.close();
            timer.cancel();
        }
        
    }

    
    /** Method: executeDiagram() executes the diagram. */
    public static void executeDiagram() {
        Graph cv = new Graph("Container Stats Plotter"); // Create a new Graph object
        cv.setSize(800, 600);  // Set the size of the window
        cv.setLocationRelativeTo(null); // Center the window
        // Set the close operation, so that the application exits when the window is closed
        cv.setDefaultCloseOperation(Graph.DO_NOTHING_ON_CLOSE); 
        CloseableHttpResponse res = monitorHttpCLI.getContainerStats("Graph");
        flag = false;  
        end = false;
        cv.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(cv);
                flag = true; 
            }
        });

        cv.setVisible(true); // Make the window visible to the end user
        
        try {
            cv.statsPlot(res, cv); // Start updating the stats
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }  
    }

    /**
     * Create an instance of MonitorHttpGUI for monitoring purposes.
     */
    MonitorHttpGUI monitorHttpGUI = new MonitorHttpGUI();

    /** Method: executeDiagramGUI() executes the diagram. 
     * This method does not show any messages to command line. 
     */
    public void executeDiagramGUI() {
        Graph cv = new Graph("Container Stats Plotter"); // Create a new Graph object
        cv.setSize(800, 600);  // Set the size of the window
        cv.setLocationRelativeTo(null); // Center the window
        // Set the close operation, so that the application exits when the window is closed
        cv.setDefaultCloseOperation(Graph.DO_NOTHING_ON_CLOSE); 
        CloseableHttpResponse res = monitorHttpGUI.getContainerStats("GOT TO DELETE!!!");
        flag = false;  
        end = false;
        cv.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(cv);
                flag = true; 
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

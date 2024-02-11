package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI;
import gr.aueb.dmst.onepercent.programming.data.DataBaseThread;
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

/**
 * A graphical user interface for visualizing container statistics.
 * 
 * <p>The Graph class extends the JFrame class to create and manage a graphical
 * window for displaying real-time container statistics. It provides methods for
 * initializing and displaying graphs, scheduling real-time data updates, and
 * handling window closing events gracefully.
 * 
 * <p>Containers' CPU or memory usage data can be visualized based on user preference,
 * and the class supports both command-line interface (CLI) and graphical user interface
 * (GUI) versions of the application. It interacts with 
 * {@link gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI} and 
 * {@link gr.aueb.dmst.onepercent.programming.gui.MonitorHttpGUI} instances to fetch
 * container statistics and updates the graph accordingly.
 * 
 * <p>This class also implements singleton design pattern to ensure only one instance
 * of the Graph class is created throughout the application.
 * 
 * <p>Note: This class is used to visualize container statistics in real-time and is
 * an integral part of the Mobi application for Docker container management.
 * 
 * @see MonitorHttpCLI
 * @see MonitorHttpGUI
 * @see DataBaseThread
 */

public class Graph extends JFrame {

    /** Indicates if the graph is for memory or cpu usage. */
    public static boolean isForMemory;
    /** Instance of MonitorHttpCLI for providing the real time data. */
    static MonitorHttpCLI monitor_cli = new MonitorHttpCLI();
    /** Instance of MonitorHttpGUI for monitoring purposes. */
    MonitorHttpGUI monitor_gui = new MonitorHttpGUI();

    /**  Graph singleton object. */
    private static Graph graph;
    /** Represents a sequence of zero or more data items in the form (x, y). */
    private XYSeries usageSeries;

    /** Constructor. */
    private Graph() { }

   /** Returns the instance of the Graph class. */
    public static Graph getInstance() {
        if (graph == null) {
            graph = new Graph();
        }
        return graph;
    }

    /**
     * Constructor to create a graph by initializing the fields,
     * related to the graph.
     * @param title the title of the graph
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
        /* 
         * Get the plot from the chart, casting it to XYPlot because it may be a subclass 
         * (other typer of diagrams).
         */
        XYPlot plot = (XYPlot) chart.getPlot(); 
        /*  Initialize the date axis (axis for the x values). */
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis(); 
        /*
         * Set the tick unit to be 1 second (it is a real time chart, 
         * so we want to update every second).
         */
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1)); 
        /* Create the dateform as we want it to be apperead on X axis. */
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        /* Set the date format to be the one we just created. */
        dateAxis.setDateFormatOverride(dateFormat); 
        /* CreateGUI component to make the chart visible to the end user. */
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        setContentPane(chartPanel);
    }

    /**
     * Initializes and displays a graph for visualizing container statistics for CLI version.
     * 
     * <p>This method creates a new Graph object with the specified title and sets its size
     * and location. It configures the window to not exit the application when closed and
     * adds a window listener to handle the window closing event gracefully. The graph is
     * then made visible to the user.
     * 
     * <p>Also, the method retrieves container statistics data using the MonitorHttpCLI 
     * instance and schedules real-time data updates for the graph.
     * 
     * <p>Note that this method is used in the CLI version of the application.
     */
    public static void displayGraph() {
        /* Customization of the graph. */
        Graph graph = new Graph("Container Stats Plotter");
        graph.setSize(800, 600);
        graph.setLocationRelativeTo(null);
        /* Do not exit the application when window is closed. */
        graph.setDefaultCloseOperation(Graph.DO_NOTHING_ON_CLOSE); 
        /* Get the container statistics data using the MonitorHttpCLI instance. */
        CloseableHttpResponse response = monitor_cli.getContainerStats("Graph");
        /* Set the state of the DataBaseThread to success, used for status section in history. */
        DataBaseThread dataBaseThread = DataBaseThread.getInstance();
        dataBaseThread.setState("success");
        /* Add a window listener to handle the window closing event gracefully. */
        graph.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(graph);
            }
        });
        /* Make the window visible to the end user. */
        graph.setVisible(true);   
        /* Schedule real-time data updates for the graph. */     
        try {
            graph.scheduleRealTimeDataUpdate(response, graph);
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }  
    }

    /**
     * Schedules a task to update real-time data periodically.
     * 
     * <p>This method schedules a task to run every second to update the real-time data
     * based on the provided reader and exception handler. The task reads data from the
     * BufferedReader and handles any UnsupportedOperationException that may occur.
     * 
     * <p>The method uses a Timer to schedule the task, which executes the {@link #onRunGraph}
     * method at fixed intervals (every second) to update the real-time data.
     * 
     * @param reader the BufferedReader from which to read real-time data
     * @throws IOException if an I/O error occurs while reading the data
     * @see #onRunGraph(BufferedReader, UnsupportedOperationException, Timer)
     */
    public void scheduleRealTimeDataUpdate(CloseableHttpResponse response, Graph ex) 
        throws UnsupportedOperationException, IOException {
        /* Create a new BufferedReader to read the response. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()
                                                                                 .getContent()));
        /* The timer is not a daemon thread, so it will not prevent the application from exiting. */
        Timer timer = new Timer(false);
        /* Schedule a task to run every second (1000 milliseconds = 1 second). */
        timer.scheduleAtFixedRate(new TimerTask() {
            /* The functionality has to be executed every second, so it is a thread. */
            @Override
            public void run() { 
                try {
                    onRunGraph(reader, timer);
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    /** 
     * Reads a new line from the response and updates the stats.
     * 
     * <p>It is called by the TimerTask to update the real-time data periodically.
     * Based on user's choice, it reads the CPU or memory usage from the response and
     * updates the stats accordingly as Memory or CPU usage diagrams can be created.
     * 
     * @param reader the BufferedReader from which to read real-time data
     * @param timer the Timer that schedules the task
     * @throws IOException if an I/O error occurs while reading the data
     */
    public void onRunGraph(BufferedReader reader, Timer timer) throws IOException {
        String inputLine = reader.readLine(); // Read a new line from the response
        if (inputLine != null) {
            double cpu_usage = monitor_cli.getCPUusage(new StringBuilder(inputLine));
            double memory_usage = monitor_cli.getMemoryUsage(new StringBuilder(inputLine));
            double usage = isForMemory ? memory_usage : cpu_usage;
            updateStats(usage);
        } else {
            // If there are no more lines to read, close the reader and cancel the timer.
            reader.close();
            timer.cancel();
        }
    }

    /** 
     * Updates the stats with the new data point.
     * @param usage the cpu usage of the container.
     */
    private void updateStats(double usage) {
        /* Get the current timestamp for the X-axis and convert it to appear as needed. */
        long currentTimestamp = System.currentTimeMillis();
        long timestampInMilliseconds = new Date(currentTimestamp).getTime(); 
        /* Add the new data point to the series. */
        usageSeries.addOrUpdate(timestampInMilliseconds, usage);    
    }

    /**
     * Initializes and displays a graph for visualizing container statistics for GUI version.
     * 
     * <p>This method creates a new Graph object with the specified title and sets its size
     * and location. It configures the window to not exit the application when closed and
     * adds a window listener to handle the window closing event gracefully. The graph is
     * then made visible to the user.
     * 
     * <p>Also, the method retrieves container statistics data using the MonitorHttpGUI
     * instance and schedules real-time data updates for the graph.
     * 
     * <p>Note that this method is used in the GUI version of the application.
     */
    public void displayGraphGUI() {
        Graph cv = new Graph("Container Stats Plotter");
        cv.setSize(800, 600);
        cv.setLocationRelativeTo(null);
        cv.setDefaultCloseOperation(Graph.DO_NOTHING_ON_CLOSE); 
        CloseableHttpResponse res = monitor_gui.getContainerStats("stats");
        cv.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(cv);
            }
        });
        cv.setVisible(true);
        try {
            cv.scheduleRealTimeDataUpdate(res, cv);
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }  
    }
    
    /**
     * Closes the specified window.
     * @param frame the window to close
     */
    private static void closeWindow(JFrame frame) {
        frame.setVisible(false);
    }
}

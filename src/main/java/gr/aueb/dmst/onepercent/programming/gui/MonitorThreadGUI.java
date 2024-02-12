package gr.aueb.dmst.onepercent.programming.gui;

import gr.aueb.dmst.onepercent.programming.core.Graph;
import gr.aueb.dmst.onepercent.programming.core.SuperThread;

/**
 * A monitor thread responsible for retrieving information, related to Docker system  and Docker
 * objects.
 * 
 * <p>This thread is used in the GUI version of the application and extends 
 * {@link gr.aueb.dmst.onepercent.programming.core.SuperThread}.
 * It contains the logic for processing user actions and invoking appropriate methods 
 * to monitor Docker objects and the docker system such as inspecting containers,
 * listing images, and monitoring the swarm.
 */
public class MonitorThreadGUI extends SuperThread {
    
    /** A monitor instance. */
    MonitorGUI monitor = new MonitorGUI();
    /** A Singleton monitor thread instance. */
    private static MonitorThreadGUI monitor_thread;

    /** Default Constructor. */
    private MonitorThreadGUI() { }

    /**
     * Getter for the monitor instance.
     * @return The monitor instance.
     */
    public MonitorGUI getMonitorInstance() {
        return monitor;
    }

    /**
     * Gets the instance of MonitorThreadGUI using the singleton pattern.
     *
     * @return The MonitorThreadGUI instance.
     */
    public static MonitorThreadGUI getInstance() {
        if (monitor_thread == null) {
            monitor_thread = new MonitorThreadGUI();
        }
        return monitor_thread;
    }
    
    /** Handles the user actions and executes the appropriate tasks. */
    @Override
    public void run() { 
        switch (this.userInput) {
            case 3:
                monitor.searchImage();
                break;
            case 5:
                Graph graph = Graph.getInstance();
                graph.displayGraphGUI();
                break;
            case 6:
                monitor.inspectContainer();
                break;
            case 11:
                break;
            case 12: 
                monitor.inspectImage();
                break;
        }
    }
}

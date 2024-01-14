package gr.aueb.dmst.onepercent.programming;

/**
 * MonitorThreadCLI is a thread class specifically designed 
 * for command-line interface (CLI) interactions
 * in the monitoring system. It extends SuperThread and handles various user inputs by invoking
 * corresponding methods from MonitorHttpCLI, Graph, CSV, and other related classes.
 */
public class MonitorThreadCLI extends SuperThread {
    
    //Singleton
    private static MonitorThreadCLI monitorThreadCLI;

    private MonitorThreadCLI() { }

    /**
     * Gets the instance of MonitorThreadCLI using the singleton pattern.
     *
     * @return The MonitorThreadCLI instance.
     */
    public static MonitorThreadCLI getInstance() {
        if (monitorThreadCLI == null) {
            monitorThreadCLI = new MonitorThreadCLI();
        }
        return monitorThreadCLI;
    }
    
    @Override
    public void run() { 
        var containerMonitorHttp = new MonitorHttpCLI();
        switch (this.userInput) {
            case 3:
                containerMonitorHttp.searchImage();
                break;
            case 5:
                Graph.executeDiagram();
                break;
            case 6:
                containerMonitorHttp.inspectContainer();
                break;
            case 7:
                CSV csv = new CSV();
                csv.startSavingData();   
                break;
            case 8:
                //only for command line
                SuperAPI.createDockerClient();
                MonitorAPI containerMonitor = new MonitorAPI();
                containerMonitor.initializeContainerModels(true);
                containerMonitor.getContainerList();
                break;
        }
        
    }

}

package gr.aueb.dmst.onepercent.programming.cli;

import gr.aueb.dmst.onepercent.programming.core.Graph;
import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.core.SuperAPI;
import gr.aueb.dmst.onepercent.programming.core.SuperThread;

import java.util.Scanner;
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
                System.out.println("Type \"C\" for CPU usage or \"M\" for Memory usage diagram.");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                Graph.isForMemory = input.equalsIgnoreCase("M");
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
            case 11:
                containerMonitorHttp.inspectSwarm();
                break;
        }
        dataBaseThread.setCommand(this.userInput);
        Thread dataThread = new Thread(dataBaseThread);
        dataThread.start();
    }

}

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
        var monitor = new MonitorHttpCLI();
        Scanner scanner = new Scanner(System.in);
        String input; //user input 
        switch (this.userInput) {
            case 3:
                monitor.searchImage();
                break;
            case 5:
                System.out.print("Type \"C\" for CPU usage or \"M\" for Memory usage diagram: ");
                input = scanner.nextLine();
                Graph.isForMemory = input.equalsIgnoreCase("M");
                Graph.executeDiagram();
                break;
            case 6:
                monitor.inspectContainer();
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
                monitor.inspectSwarm();
                System.out.println(monitor.formatSwarmInfo());
                break;
            case 13:
                monitor.listImages();
                //monitor.printImagesList();
                monitor.estimateImages();
                int images = scanner.nextInt();
                monitor.printImagesList(images);
                break;
            case 14: 
                System.out.print("Type \"D\" for Docker version or \"S\" for System overview: ");
                input = scanner.nextLine();
                while (!input.equalsIgnoreCase("D") && !input.equalsIgnoreCase("S")) {
                    System.out.println("Invalid input. Please try again.");
                    input = scanner.nextLine();
                }
                if (input.equalsIgnoreCase("D")) {
                    monitor.dockerVersion();
                } else {
                    monitor.systemInfo();
                }
        }
        dataBaseThread.setCommand(this.userInput);
        Thread dataThread = new Thread(dataBaseThread);
        dataThread.start();
    }

}

package gr.aueb.dmst.onepercent.programming.cli;

import gr.aueb.dmst.onepercent.programming.core.Graph;
import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.core.SuperAPI;
import gr.aueb.dmst.onepercent.programming.core.SuperThread;

import java.util.Scanner;
/**
 * A monitor thread responsible for retrieving information, related to Docker system  and Docker
 * objects.
 * 
 * <p>This thread is used in the CLI version of the application and extends 
 * {@link gr.aueb.dmst.onepercent.programming.core.SuperThread}.
 * It contains the logic for processing user input and invoking appropriate methods 
 * to monitor Docker objects and the docker system such as inspecting containers,
 * listing images, and monitoring the swarm.
 */
public class MonitorThreadCLI extends SuperThread {

    /** The singleton instance of MonitorThreadCLI. */
    private static MonitorThreadCLI monitorThreadCLI;

    /** Default constructor. */
    private MonitorThreadCLI() { }

    /**
     * Returns the singleton instance of MonitorThreadCLI.
     * @return The MonitorThreadCLI instance.
     */
    public static MonitorThreadCLI getInstance() {
        if (monitorThreadCLI == null) {
            monitorThreadCLI = new MonitorThreadCLI();
        }
        return monitorThreadCLI;
    }
    
    /** Handles the user input and executes the appropriate actions. */
    @Override
    public void run() { 
        var monitor = new MonitorHttpCLI();
        Scanner scanner = new Scanner(System.in);
        String input; //User's input. 
        switch (this.userInput) {
            case 4:
                monitor.inspectContainer();
                break;
            case 5:
                System.out.print("Type \"C\" for CPU usage or \"M\" for Memory usage diagram: ");
                input = scanner.nextLine();
                Graph.isForMemory = input.equalsIgnoreCase("M");
                Graph.displayGraph();
                break;       
            case 6:
                CSV csv = new CSV();
                csv.startProcess();   
                break;
            case 7:
                /*
                 * Use an object  of MonitorAPI, which is based on docker-java library on github,
                 * not Docker Engine API 
                 */
                SuperAPI.createDockerClient();
                MonitorAPI monitorAPI = new MonitorAPI();
                monitorAPI.initializeContainerList(true);
                monitorAPI.getContainerList();
                break;
            case 10:
                monitor.searchImage();
                break;
            case 11:
                monitor.listImages();
                monitor.estimateDisplayedImages();
                int images = scanner.nextInt();
                monitor.printImagesList(images);
                break;
            case 12:
                monitor.inspectSwarm();
                System.out.println(monitor.formatSwarmInfo());
                break;
            case 13: 
                System.out.print("Type \"D\" for Docker version or \"S\" for System overview: ");
                input = scanner.nextLine();
                //Input validation.
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
        //Spawn the thread that runs the database of the program and stores the data.
        dataBaseThread.setAction(this.userInput);
        Thread dataThread = new Thread(dataBaseThread);
        dataThread.start();
    }
}

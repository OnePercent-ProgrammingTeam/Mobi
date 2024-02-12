package gr.aueb.dmst.onepercent.programming.cli;

import gr.aueb.dmst.onepercent.programming.core.SuperThread;

/**
 * An executor thread responsible for executing actions related to Docker object management.
 * 
 * <p>This thread is used in the CLI version of the application and extends 
 * {@link gr.aueb.dmst.onepercent.programming.core.SuperThread}.
 * It contains the logic for processing user input and invoking appropriate methods 
 * to manage Docker objects, such as starting, stopping, pulling, or removing containers and images.
 */
public class ExecutorThreadCLI extends SuperThread {
    
    /** The singleton instance of ExecutorThreadCLI. */
    private static ExecutorThreadCLI executorThreadCLI;

    /** Default constructor. */
    private ExecutorThreadCLI() { }

    /**
     * Returns the singleton instance of ExecutorThreadCLI.
     * @return The singleton instance of ExecutorThreadCLI.
     */
    public static ExecutorThreadCLI getInstance() {
        if (executorThreadCLI == null) {
            executorThreadCLI = new ExecutorThreadCLI();
        }
        return executorThreadCLI;
    }

    /**
     * Handles the user input and executes the appropriate actions. 
     * Based on the user input, it calls the appropriate methods that execute actions.
     */
    @Override
    public void run() { 
        ManagerCLI manager = new ManagerCLI();
        switch (this.userInput) {
            case 1:
                manager.startContainer();
                break;
            case 2:
                manager.stopContainer();
                break;
            case 3: 
                manager.removeContainer();
                break;
            case 8:
                manager.pullImage();
                break;
            case 9:
                manager.removeImage();
                break;
        }
        //Spawn the thread that runs the database of the program and stores the data.
        dataBaseThread.setAction(this.userInput);
        Thread dataThread = new Thread(dataBaseThread);
        dataThread.start();
    }
}

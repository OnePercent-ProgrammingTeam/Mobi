package gr.aueb.dmst.onepercent.programming.gui;

import gr.aueb.dmst.onepercent.programming.core.SuperThread;

/**
 * An executor thread responsible for executing actions related to Docker object management.
 * 
 * <p>This thread is used in the GUI version of the application and extends 
 * {@link gr.aueb.dmst.onepercent.programming.core.SuperThread}.
 * It contains the logic for processing user input, generated when clicking buttons 
 * and invoking appropriate methods to manage Docker objects, such as starting, stopping, pulling,
 * or removing containers and images.
 */
public class ExecutorThreadGUI extends SuperThread {
    
    /** Singleton instance of executor thread. */
    private static ExecutorThreadGUI executorThread;

    /** Default constructor. */
    private ExecutorThreadGUI() { }

    /**
     *  Provides a singleton instance of ExecutorThreadGUI.
     *
     * @return The singleton instance of ExecutorThreadGUI.
     */
    public static ExecutorThreadGUI getInstance() {
        if (executorThread == null) {
            executorThread = new ExecutorThreadGUI();
        }
        return executorThread;
    }

    /**
     * Handles the user input and executes the appropriate actions. 
     * Based on the user input, it calls the appropriate methods that execute actions.
     */
    @Override
    public void run() { 
        var containerManagerHttp = new ManagerGUI();
        switch (this.userInput) {
            case 1:
                containerManagerHttp.startContainer();
                break;
            case 2:
                containerManagerHttp.stopContainer();
                break;
            case 4:
                containerManagerHttp.pullImage();
                break;
            case 9:
            case 10: 
                break;
        }
    }
}

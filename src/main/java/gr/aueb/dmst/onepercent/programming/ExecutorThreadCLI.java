package gr.aueb.dmst.onepercent.programming;

/**
 * Class: ExecutorThreadCLI is a class that extends SuperThread and represents
 * a command-line interface (CLI) implementation of an executor thread.
 *
 * @see gr.aueb.dmst.onepercent.programming.SuperThread
 */
public class ExecutorThreadCLI extends SuperThread {
    // Singleton
    private static ExecutorThreadCLI executorThreadCLI;

    private ExecutorThreadCLI() { }

    /**
     * Method: getInstance provides a singleton instance of ExecutorThreadCLI.
     *
     * @return The singleton instance of ExecutorThreadCLI.
     */
    public static ExecutorThreadCLI getInstance() {
        if (executorThreadCLI == null) {
            executorThreadCLI = new ExecutorThreadCLI();
        }
        return executorThreadCLI;
    }

    /**
     * This method is called when the executor thread is started.
     * It contains the logic for handling different user inputs.
     */
    @Override
    public void run() { 
        ManagerHttpCLI containerManagerHttp = new ManagerHttpCLI();
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
        }
    }
}


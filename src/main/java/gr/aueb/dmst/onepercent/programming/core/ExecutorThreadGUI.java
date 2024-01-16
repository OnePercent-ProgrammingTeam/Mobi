package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.gui.ManagerHttpGUI;

/**
 * Class: ExecutorThreadGUI is a class that extends SuperThread and represents
 * a graphical user interface (GUI) implementation of an executor thread.
 *
 * @see gr.aueb.dmst.onepercent.programming.core.SuperThread
 */
public class ExecutorThreadGUI extends SuperThread {
    // Singleton
    private static ExecutorThreadGUI executorThreadGUI;

    private ExecutorThreadGUI() { }

    /**
     * Method: getInstance provides a singleton instance of ExecutorThreadGUI.
     *
     * @return The singleton instance of ExecutorThreadGUI.
     */
    public static ExecutorThreadGUI getInstance() {
        if (executorThreadGUI == null) {
            executorThreadGUI = new ExecutorThreadGUI();
        }
        return executorThreadGUI;
    }

    /**
     * This method is called when the executor thread is started.
     * It contains the logic for handling different user inputs.
     */
    @Override
    public void run() { 
        var containerManagerHttp = new ManagerHttpGUI();
        switch (this.userInput) {
            case 1:
                containerManagerHttp.startContainer();
                break;
            case 2:
                containerManagerHttp.stopContainer();
                break;
            case 4:
                // TO DO: containerManagerHttp.pullImageGUI();
                break;
        }
    }
}

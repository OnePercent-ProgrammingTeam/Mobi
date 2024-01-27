package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.data.DataBaseThread;

/**
 * Class: SuperThread is a superclass that contains a static variable, used in other classes,
 * representing the user input collected by the MenuThread.
 * It is used to create a mock object for the MenuThread class in the test classes.
 *
 * @see gr.aueb.dmst.onepercent.programming.core.MenuThread
 * @see gr.aueb.dmst.onepercent.programming.cli.MonitorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.cli.ExecutorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.core.ExecutorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.data.DataBaseThread
 */
public abstract class SuperThread implements Runnable {
    /**
     * Field: userInput is the input of the user that is used in threads to define his options.
     * It represents the user input collected by the MenuThread.
     */
    protected int userInput;

    /**
     * ok
     */
    protected DataBaseThread dataBaseThread = DataBaseThread.getInstance();
    //protected Thread dataThread = new Thread(dataBaseThread);

    /**
     * Method: setUserInput sets the user input.
     *
     * @param userInput The input provided by the user.
     */
    public void setUserInput(int userInput) {
        this.userInput = userInput;
    }

    /**
     * Method: getUserInput gets the user input.
     *
     * @return The user input stored in the userInput field.
     */
    public int getUserInput() {
        return this.userInput;
    }

    /**
     * This must be implemented as SuperThread implements Runnable.
     * It is abstract, so children of the class must @Override it.
     */
    public abstract void run();

    /** 
     * Default Constructor
     */
    public SuperThread() {

    }
    
}

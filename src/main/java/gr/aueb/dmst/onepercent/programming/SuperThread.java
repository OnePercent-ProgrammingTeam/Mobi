package gr.aueb.dmst.onepercent.programming;

/**
 * Class: SuperThread is a superclass that contains a static variable, used in other classes,
 * representing the user input collected by the MenuThread.
 * It is used to create a mock object for the MenuThread class in the test classes.
 *
 * @see gr.aueb.dmst.onepercent.programming.MenuThread
 * @see gr.aueb.dmst.onepercent.programming.MonitorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.MonitorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.ExecutorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.ExecutorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.DataBaseThread
 */
public abstract class SuperThread implements Runnable {
    /**
     * Field: userInput is the input of the user that is used in threads to define his options.
     * It represents the user input collected by the MenuThread.
     */
    protected int userInput;

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

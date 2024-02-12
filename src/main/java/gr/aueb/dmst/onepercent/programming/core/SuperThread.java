package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.data.DataBaseThread;

/**
 * Abstract class that serves as the superclass for all threads in the application.
 * 
 * <p>All thread classes in both the CLI and GUI versions of the application extend this class.
 * 
 * <p>Subclasses of this class should implement the specific functionality required for their
 * respective tasks. They must override the {@code run()} method to define the behavior
 * of the thread during execution.
 * 
 * <p>By extending this class, thread subclasses inherit common properties and behaviors.
 * 
 * <p>The {@code userInput} field stores the input provided by the user, which can be accessed
 * and modified by subclasses as needed.
 * 
 * <p>The {@code dataBaseThread} field provides access to the singleton instance of the
 * {@code DataBaseThread} class, allowing threads to interact with the database.
 * 
 * @see gr.aueb.dmst.onepercent.programming.cli.MenuThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.cli.ExecutorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.cli.MonitorThreadCLI
 * 
 * @see gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.gui.ExecutorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI
 */
public abstract class SuperThread extends Thread {

    /** User's input. */
    protected int userInput;

    /** Database thread instance. */
    protected DataBaseThread dataBaseThread = DataBaseThread.getInstance();

    /** Default Constructor. */
    public SuperThread() { }

    /**
     * Initiates the execution of the the thread.
     * This method overrides the run method from the Thread class 
     * and defines the behavior of the menu thread during execution.
     */
    public abstract void run();

    /** Get user's input.
     * @return the user's input
     */
    public int getUserInput() {
        return this.userInput;
    }

    /** 
     * Set user's input. 
     * @param userInput The user's input.
     */
    public void setUserInput(int userInput) {
        this.userInput = userInput;
    }
}

package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.data.DataBaseThread;
/**
 * A central manager-coordinator thread responsible for managing 
 * other threads in the application. It serves as the backbone for handling 
 * concurrent operations in both the CLI and GUI versions of the application.
 * 
 * <p>This class is designed to manage the coordination and interactions of various 
 * threads. It includes subclasses:
 * {@link gr.aueb.dmst.onepercent.programming.cli.MenuThreadCLI} and 
 * {@link gr.aueb.dmst.onepercent.programming.gui.MenuThreadGUI}, which are 
 * specifically used in the CLI and GUI versions, respectively, for thread management.
 */
public class MenuThread extends Thread {

    /** The underlying thread managed by the MenuThread. */
    protected Thread thread;

    /** Singleton instance of DataBaseThread for database operations. */
    protected DataBaseThread dataBaseThread = DataBaseThread.getInstance();
    
    /** Singleton instance of DataBase for database management. */
    protected DataBase dataBase = DataBase.getInstance();

    /**
     * Default constructor for MenuThread.
     * Constructs a new instance of the MenuThread class.
     */
    public MenuThread() { }

    /**
     * Initiates the execution of the menu thread.
     * This method overrides the run method from the Thread class 
     * and defines the behavior of the menu thread during execution.
     */
    @Override
    public void run() { }

    /**
     * Waits for the underlying thread to complete its execution.
     * This method ensures that the current thread waits for the 
     * underlying thread managed by MenuThread to complete its execution.
     */
    public void waitThread() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

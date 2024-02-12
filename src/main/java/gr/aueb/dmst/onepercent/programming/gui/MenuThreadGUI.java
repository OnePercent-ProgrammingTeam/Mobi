package gr.aueb.dmst.onepercent.programming.gui;

import gr.aueb.dmst.onepercent.programming.core.MenuThread;

/**
 * A central-manager thread responsible for coordinating the threads of the
 * GUI version of the application.
 * 
 * <p>This thread ensures proper handling of user input and execution of appropriate actions.
 * It manages which tasks should be executed every time user presses a button.
 * 
 * <p>Extends {@link gr.aueb.dmst.onepercent.programming.core.MenuThread}.
 * 
 * @see gr.aueb.dmst.onepercent.programming.gui.ExecutorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.gui.MonitorThreadGUI
 * @see gr.aueb.dmst.onepercent.programming.gui.ManagerGUI
 */
public class MenuThreadGUI extends MenuThread {
    
    /** Singleton instance of executor thread. */
    ExecutorThreadGUI executor = ExecutorThreadGUI.getInstance();
    /** Singleton instance of monitor thread. */
    MonitorThreadGUI monitor = MonitorThreadGUI.getInstance();
    
    /**
     * Default Constructor
     */
    public MenuThreadGUI() { }
    
    @Override
    public void run() {
        //TO DO(Scobioala-Koronellou): Database Connectivity.
        //dataBaseThread.setInterfaceType("GUI");
    }

    /**
     * Handles the user input.
     * @param input The user's input.
    */
    public void handleUserInput(int input) {
        switch (input) {
            case 1:
            case 2:
            case 4:
            case 9:
            case 10:
                executor.setUserInput(input);
                thread = new Thread(executor);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Executor"); 
                thread.start();
                waitThread();     
                break;
            case 3:
            case 5:
            case 6:
            case 11:
            case 12:
                monitor.setUserInput(input);
                thread = new Thread(monitor);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Monitor"); 
                thread.start();
                waitThread();   
                break;
            default:
                System.out.println("Non Valid Input.");
        }
    }
}

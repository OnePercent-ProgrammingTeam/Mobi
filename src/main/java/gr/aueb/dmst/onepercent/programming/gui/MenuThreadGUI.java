package gr.aueb.dmst.onepercent.programming.gui;


import gr.aueb.dmst.onepercent.programming.core.ExecutorThreadGUI;
import gr.aueb.dmst.onepercent.programming.core.MenuThread;

/**
 * MenuThreadGUI class represents the graphical user interface (GUI) implementation 
 * of the main menu thread.
 * It extends the abstract MenuThread class and is responsible for 
 * handling user input in a GUI environment.
 *
 * @see MenuThread
 */
public class MenuThreadGUI extends MenuThread {
 
    ExecutorThreadGUI executorThreadGUI = ExecutorThreadGUI.getInstance();
    MonitorThreadGUI monitorThreadGUI = MonitorThreadGUI.getInstance();
    
    @Override
    public void run() {
        dataBaseThread.setInterfaceType("GUI");
    }

    /**
     * Method: handleUserInputGUI(int answer) handles the user's input in a GUI environment.
     * It initiates the appropriate threads based on the user's choice.
     *
     * @param answer The user's input representing the chosen action.
     */
    public void handleUserInputGUI(int answer) {
        switch (answer) {
            case 1:
            case 2:
            case 4:
            case 9:
            case 10:
                executorThreadGUI.setUserInput(answer);
                thread = new Thread(executorThreadGUI);
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
                monitorThreadGUI.setUserInput(answer);
                thread = new Thread(monitorThreadGUI);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Monitor"); 
                thread.start();

                waitThread();
                
                break;
            default:
                System.out.println("Non Valid Input.");
        }
    }

    /**
     * Default Constructor
     */
    public MenuThreadGUI() {

    }
}

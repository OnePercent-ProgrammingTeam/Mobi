package gr.aueb.dmst.onepercent.programming.gui;

import gr.aueb.dmst.onepercent.programming.core.DataBase;
import gr.aueb.dmst.onepercent.programming.core.ExecutorThreadGUI;
import gr.aueb.dmst.onepercent.programming.core.MenuThread;
import graphics.DataUsers;

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
        DataUsers user = new DataUsers();
        user.createUser();
        DataBase contain = new DataBase();
        contain.createDatabase();
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
                executorThreadGUI.setUserInput(answer);
                thread = new Thread(executorThreadGUI);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Executor"); 
                thread.start();

                waitThread();

                /*start concurrently the database Thread*/
                /*dataThread.setUserInput(answer);
                thread = new Thread(dataThread);
                thread.setName("DataBase"); 
                thread.start();*/
                
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
                monitorThreadGUI.setUserInput(answer);
                thread = new Thread(monitorThreadGUI);
                thread.setName("Monitor");
                thread.start();

                waitThread();
                    
                /*start concurrently the database Thread*/
                dataThread.setUserInput(answer);
                thread = new Thread(dataThread);
                thread.setName("DataBase"); 
                thread.start();
                break;
            default:
                System.out.println("Non Valid Input.");
        }
       // if (answer == 5) {
       //     while (Graph.end == false) {
       //         waitThread();
       //     }
       // } else {
       //     waitThread();
       // }   
    }

    /**
     * Default Constructor
     */
    public MenuThreadGUI() {

    }
}

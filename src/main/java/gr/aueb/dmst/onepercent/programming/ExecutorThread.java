package gr.aueb.dmst.onepercent.programming;

/**  Class: ExecutorThread is the thread that primarly executes the
 *  core functinalities described below.
 *  1. Starts a container 
 *  2. Stops a container
 *  The user is asked to type the id of the container he wants to start/stop.
 *  He is able to choose from a list of containers that are already installed
 *  in his machine.
 */
public class ExecutorThread extends SuperThread {
    @Override
    public void run() { 
        var containerManagerHttp = new ManagerHttp();
        if (this.userInput == 1) {
            //for command line 
            //containerManagerHttp.startContainer();

            //for GUI
            containerManagerHttp.startContainerGUI();

        } else if (this.userInput == 2) {
            //for command line 
            //containerManagerHttp.stopContainer();

            //for GUI
            containerManagerHttp.stopContainerGUI();

        } else if (this.userInput == 4) {
            //for command line 
            containerManagerHttp.pullImage();

            //for GUI
            //containerManagerHttp.pullImageGUI();
        }
    }  
}

package gr.aueb.dmst.onepercent.programming;

/** Class: SuperThread is a superclass that contains a static variable, used in other classes,
 *  representing the user input collected by the MenuThread.
 *  It is used to create a mock object for the MenuThread class in the test classes.
 *  @see MenuThread
 *  @see MonitorThread
 *  @see ManagerThread
 *  @see DataBaseThread
 */
public abstract class SuperThread implements Runnable {
    /** Field: userInput is the input of the user that is used in threads to define his options */
    protected int userInput;

    /** Method: setUserInput() sets the user input */
    protected void setUserInput(int userInput) {
        this.userInput = userInput;
    }

    // This must be implemented as SuperThread implements Runnable
    // It is abstract so children of the class must @Override it
    public abstract void run();
}

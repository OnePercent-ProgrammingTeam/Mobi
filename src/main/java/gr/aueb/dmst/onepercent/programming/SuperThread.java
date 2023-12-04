package gr.aueb.dmst.onepercent.programming;

public abstract class SuperThread implements Runnable {
    // The userInput collected by the MenuThread
    protected int userInput;

    protected void setUserInput(int userInput) {
        this.userInput = userInput;
    }

    // This must be implemented as SuperThread implements Runnable
    // It is abstract so children of the class must @Override it
    public abstract void run();
}

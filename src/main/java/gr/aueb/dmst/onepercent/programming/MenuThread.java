package gr.aueb.dmst.onepercent.programming;
/** Class: MenuThread is a thread that prints the menu and handles the user's input. */
public class MenuThread extends Thread {
   
    Thread thread;
    DataBaseThread dataThread = new DataBaseThread();
    DataBase database = new DataBase();

    @Override
    public void run() { }

    /**
     * Waits for the thread to complete its execution. This method blocks the calling thread
     * until the thread on which it is called terminates or is interrupted.
     *
     * throws InterruptedException If the current thread is interrupted 
     * while waiting for the associated thread to complete.
     */
    public void waitThread() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /** 
     * Default Constructor
     */
    public MenuThread() {

    }
}

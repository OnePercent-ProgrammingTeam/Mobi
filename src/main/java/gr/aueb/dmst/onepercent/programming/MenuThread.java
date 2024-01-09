package gr.aueb.dmst.onepercent.programming;
/** Class: MenuThread is a thread that prints the menu and handles the user's input. */
public class MenuThread extends Thread {
   
    Thread thread;
    DataBaseThread dataThread = new DataBaseThread();
    DataBase database = new DataBase();

    @Override
    public void run() { }

    public void waitThread() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

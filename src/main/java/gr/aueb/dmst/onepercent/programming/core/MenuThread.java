package gr.aueb.dmst.onepercent.programming.core;

import gr.aueb.dmst.onepercent.programming.data.DataBase;
import graphics.DataUsers;

/** Class: MenuThread is a thread that prints the menu and handles the user's input. */
public class MenuThread extends Thread {
   
    /**
     * The thread that runs the menu.
     */
    protected Thread thread;
   
    protected DataUsers users = new DataUsers();
    protected DataBase dataBase = DataBase.getInstance();


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

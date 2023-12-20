package gr.aueb.dmst.onepercent.programming;

import java.util.InputMismatchException;
import java.util.Scanner;

/** Class: MenuThread is a thread that prints the menu and handles the user's input */
public class MenuThread extends Thread {
   
    private final Scanner INPUT = new Scanner(System.in);
    Thread thread;
    ExecutorThread executorThread = new ExecutorThread(); 
    MonitorThread monitorThread = new MonitorThread();

    @Override
    public void run() {
        printMenu(); 
    }

    /** Method: printMenu() prints the main menu of the program indicating
     * the available options to the user
    */
    public void printMenu() {
        System.out.println("-LIST OF CONTAINERS-");
        System.out.println("------------------------------------------------------------");
        SuperAPI.createDockerClient();
        MonitorAPI containerMonitor = new MonitorAPI();
        containerMonitor.initializeContainerModels();
        containerMonitor.getContainerList();
        System.out.println("\n");
        do {
            System.out.println("-MENU-");
            System.out.println("------------------------------------------------------------");
            System.out.println("1) Start container");
            System.out.println("2) Stop container");
            System.out.println("3) Search image");
            System.out.println("4) Pull image");
            System.out.println("5) Plot CPU Usage diagram for a running container");
            System.out.println("6) Get information about a specific container");
            System.out.println("7) Store real-time data in .csv file for a running container");
            System.out.println("8) Print a list with the locally installed containers");
            System.out.println("-------------------------------------------------------------");
        } while (handleUserInput());
    }
    
    /** Method: handleUserInput() handles the user's input
     * @return true if the user wants to run the program again, false otherwise
     */
    public boolean handleUserInput() {
        System.out.print("Please enter a number: ");
        int answer = 0;
        try {
            answer = INPUT.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Please, enter a valid number.");
            return true;
        }
        
        switch (answer) {
            case 1, 2, 4:
                executorThread.setUserInput(answer);
                thread = new Thread(executorThread);
                thread.setName("Executor"); // set name to the thread so as to be easier to recognize it. 
                thread.start();
                break;
            case 3, 5, 6, 7, 8:
                monitorThread.setUserInput(answer);
                thread = new Thread(monitorThread);
                thread.setName("Monitor");
                thread.start();
                break;
            default:
                System.out.println("Non Valid Input.");
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        System.out.println("\nWant to run again?\n\nFor YES press Y.\nFor NO press N. ");
        System.out.print("Enter answer: ");
        char choice = INPUT.next().charAt(0);
        System.out.println();
        if (choice == 'N') {
            System.out.println("Thank you!");
            INPUT.close();
            System.exit(0);
        }
        return choice == 'Y'; 
    }
}

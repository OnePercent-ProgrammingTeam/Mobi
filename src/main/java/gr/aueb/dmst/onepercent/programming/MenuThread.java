package gr.aueb.dmst.onepercent.programming;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuThread extends Thread {
   
    private final Scanner INPUT = new Scanner(System.in);
    Thread thread;
    ExecutorThread executorThread = new ExecutorThread(); 
    MonitorThread monitorThread = new MonitorThread();

    @Override
    public void run(){
        printMenu(); 
    }

    public void printMenu(){
        do{
            System.out.println("-MENU-");
            System.out.println("------------------------------------------------------------");
            System.out.println("1) Start container");
            System.out.println("2) Stop container");
            System.out.println("3) Get information about a specific container");
            System.out.println("4) Plot CPU Usage diagram for a running container");
            System.out.println("5) Search image");
            System.out.println("6) Store real-time data in .csv file for a running container");
            System.out.println("-------------------------------------------------------------");
        } while (handleUserInput());
    }
    
    public boolean handleUserInput() {
        System.out.print("Please enter a number: ");
        int answer = 0;
        try {
            answer = INPUT.nextInt();
        } catch(InputMismatchException e) {
            System.out.println("Please, enter a valid number.");
            return true;
        }
        
        switch (answer) {
            case 1,2:
                executorThread.setUserInput(answer);
                thread = new Thread(executorThread);
                thread.setName("Executor"); // set name to the thread so as to be easier to recognize it. 
                thread.start();
                break;
            case 3,4,5,6:
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
        if (choice == 'N'){
            System.out.println("Thank you!");
            INPUT.close();
            System.exit(0);
        }
        return  (choice =='Y'); 
    }
}

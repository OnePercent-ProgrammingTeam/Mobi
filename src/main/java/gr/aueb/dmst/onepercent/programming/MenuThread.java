package gr.aueb.dmst.onepercent.programming;

import java.util.Scanner;

public class MenuThread extends Thread implements Runnable {
   
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
            System.out.println("1) Start");
            System.out.println("2) Stop ");
            System.out.println("3) Get information about a specific container");
            System.out.println("4) Plot CPU Usage diagram for container");
            System.out.println("5) Search image");
        } while (handleUserInput());
    }
    
    
    public boolean handleUserInput() {
       
        int answer = INPUT.nextInt();
        switch (answer) {
        case 1,2:
            executorThread.setUserInput(answer);
            thread = new Thread(executorThread);
            thread.setName("Executor"); // set name to the thread so as to be easier to recognize it. 
            thread.start();
            break;
        case 3,4,5:
            monitorThread.setUserInput(answer);
            thread = new Thread(monitorThread);
            thread.setName("Monitor");
            thread.start();
            try { 
                MenuThread.sleep(5000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            break;

        default:
        System.out.println("Non Valid Input.");
    }
    try {
        thread.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    
    System.out.println("Want to run again?\n\nFor YES press Y.\nFor NO press N. ");
    char choice = INPUT.next().charAt(0);
    if (choice == 'N'){
        INPUT.close();
    }
    
    return  (choice=='Y'); 
}
}

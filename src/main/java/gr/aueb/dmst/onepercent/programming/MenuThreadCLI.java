package gr.aueb.dmst.onepercent.programming;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuThreadCLI extends MenuThread {
    
    private final Scanner INPUT = new Scanner(System.in);
    ExecutorThreadCLI executorThreadCLI = ExecutorThreadCLI.getInstance();
    MonitorThreadCLI monitorThreadCLI = MonitorThreadCLI.getInstance();

    @Override
    public void run() {
        //database.createDatabase();
        printMenu();
    }

    /** Method: printMenu() prints the main menu of the program indicating
     *  the available options to the user.
    */
    public void printMenu() {
        System.out.println("-LIST OF CONTAINERS-");
        System.out.println("------------------------------------------------------------");
        SuperAPI.createDockerClient();
        MonitorAPI containerMonitor = new MonitorAPI();
        containerMonitor.initializeContainerModels(true);
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


    /** Method: handleUserInput() handles the user's input.
     * @return true if the user wants to run the program again, false otherwise.
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
            case 1:
            case 2:
            case 4:
                executorThreadCLI.setUserInput(answer);
                thread = new Thread(executorThreadCLI);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Executor"); 
                thread.start();

                waitThread();

                /*start concurrently the database Thread
                dataThread.setUserInput(answer);
                thread = new Thread(dataThread);
                thread.setName("DataBase"); 
                thread.start();*/
                
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
                monitorThreadCLI.setUserInput(answer);
                thread = new Thread(monitorThreadCLI);
                thread.setName("Monitor");
                thread.start();

                waitThread();

                /*if (answer == 3) {
                    
                    //start concurrently the database Thread
                    dataThread.setUserInput(answer);
                    thread = new Thread(dataThread);
                    thread.setName("DataBase"); 
                    thread.start();
                }*/
                

                break;
            default:
                System.out.println("Non Valid Input.");
        }
        if (answer == 5) {
            while (Graph.end == false) {
                waitThread();
            }
        } else {
            waitThread();
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

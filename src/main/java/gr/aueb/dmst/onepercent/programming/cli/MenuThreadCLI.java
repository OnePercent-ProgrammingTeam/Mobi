package gr.aueb.dmst.onepercent.programming.cli;

import exceptions.InvalidInputException;
import gr.aueb.dmst.onepercent.programming.core.Graph;
import gr.aueb.dmst.onepercent.programming.core.MenuThread;
import gr.aueb.dmst.onepercent.programming.core.MonitorAPI;
import gr.aueb.dmst.onepercent.programming.core.SuperAPI;

import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * MenuThreadCLI class represents the command-line interface (CLI) implementation 
 * of the main menu thread.
 * It extends the abstract MenuThread class and is responsible 
 * for printing the main menu, handling user input,
 * and executing corresponding actions through the ExecutorThreadCLI and MonitorThreadCLI.
 *
 * @see MenuThread
 * @see ExecutorThreadCLI
 * @see MonitorThreadCLI
 */
public class MenuThreadCLI extends MenuThread {
    
    ExecutorThreadCLI executorThreadCLI = ExecutorThreadCLI.getInstance();
    MonitorThreadCLI monitorThreadCLI = MonitorThreadCLI.getInstance();
    UserAuthenticationCLI userAuth   = new UserAuthenticationCLI();

    @Override
    public void run() {
        printWelcomeMessage();
        userAuth.logIn();   
        printMenu();
    }
    
    private void printWelcomeMessage() {
        System.out.println("\n\nWelcome to Mobi,\nYour Docker Captain - CLI Version\n\n");
        System.out.println("Docker Hub login, please proceed.");
    }
    
    /** Method: printMenu() prints the main menu of the program indicating
     *  the available options to the user.
    */
    private void printMenu() {
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
            System.out.println("5) Plot resource usage diagram for a running container");
            System.out.println("6) Get information about a specific container");
            System.out.println("7) Store real-time data in .csv file for a running container");
            System.out.println("8) Print a list with the locally installed containers");
            System.out.println("9) Remove container");
            System.out.println("10) Remove image");
            System.out.println("11) Inspect Swarm");
            System.out.println("12) See your History");
            System.out.println("-------------------------------------------------------------");
        } while (handleUserInput());
    }


    /** Method: handleUserInput() handles the user's input.
     * @return true if the user wants to run the program again, false otherwise.
     */
    public boolean handleUserInput() {
        Scanner INPUT = new Scanner(System.in);
        /*TO DO:Find a way to run the test with INPUT being global.*/
        System.out.print("Please enter a number: ");
        int answer = 0;
        try {
            answer = INPUT.nextInt();
            executeUserChoice(answer);
        } catch (InputMismatchException e) {
            System.out.println("Please, enter a valid number.");
            return true;
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return true;
        } finally {
            INPUT.nextLine();
        }
        System.out.println("\nWant to run again?\n\nFor YES press Y.\nFor NO press N. ");
        System.out.print("Enter answer: ");
        char choice = INPUT.next().charAt(0);
        System.out.println();
        if (choice == 'N' || choice == 'n') {
            System.out.println("Thank you!");
            INPUT.close();
            System.exit(0);
        }
        return choice == 'Y' || choice == 'y'; 
    }


    /**
     * 
     */
    private void executeUserChoice(int answer) throws InvalidInputException {
        switch (answer) {
            case 1:
            case 2:
            case 4:
            case 9:
            case 10:
                executorThreadCLI.setUserInput(answer);
                thread = new Thread(executorThreadCLI);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Executor"); 
                thread.start();

                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
                monitorThreadCLI.setUserInput(answer);
                thread = new Thread(monitorThreadCLI);
                thread.setName("Monitor");
                thread.start();                

                break;
            default:
                throw new InvalidInputException("Please, enter a valid number.");
        }
        if (answer == 5) {
            while (Graph.end == false) {
                waitThread();
            }
        } else {
            waitThread();
        }


    }
        
    /**
     * Default Constructor
     */
    public MenuThreadCLI() {
        
    }

}

package gr.aueb.dmst.onepercent.programming.cli;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.YELLOW;

import gr.aueb.dmst.onepercent.programming.core.MenuThread;
import gr.aueb.dmst.onepercent.programming.core.DockerInformationRetriever;
import gr.aueb.dmst.onepercent.programming.exceptions.InvalidInputException;

import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * A central-manager thread responsible for coordinating the CLI version of the application.
 * 
 * <p>This thread ensures proper handling of user input and execution of appropriate actions.
 * It manages the printing of the menu and user interaction. 
 * 
 * <p>Extends {@link gr.aueb.dmst.onepercent.programming.core.MenuThread}.
 * 
 * @see gr.aueb.dmst.onepercent.programming.cli.ExecutorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.cli.MonitorThreadCLI
 * @see gr.aueb.dmst.onepercent.programming.cli.ManagerHttpCLI
 */

public class MenuThreadCLI extends MenuThread {
    
    /** The singleton instance of Executor Thread. */
    ExecutorThreadCLI executorthread = ExecutorThreadCLI.getInstance();
    /** The singleton instance of Monitor Thread. */
    MonitorThreadCLI monitorthread = MonitorThreadCLI.getInstance();
    /** The user authentication for the CLI. */
    UserAuthenticationCLI userAuthCLI = new UserAuthenticationCLI();

    /** Default Constructor. */
    public MenuThreadCLI() { }

    /** Runs the core of the program. */
    @Override
    public void run() {
        printWelcomeMessage();
        userAuthCLI.logIn();   
        printMenu();
    }
    
    /** Prints the welcome message. */
    private void printWelcomeMessage() {
        System.out.println("\n\nWelcome to Mobi,\nYour Docker Captain - CLI Version\n\n");
        System.out.println("Docker Hub login, please proceed.");
    }
    
    /** Prints the menu. */
    private void printMenu() {
        System.out.println("-LIST OF CONTAINERS-");
        System.out.println("------------------------------------------------------------");
        DockerInformationRetriever.createDockerClient();
        DockerInformationRetriever containerMonitor = new DockerInformationRetriever();
        containerMonitor.initializeContainerList(true);
        containerMonitor.printContainerList();
        System.out.println("\n");
        do {
            System.out.println("-MENU-");
            System.out.println("------------------------------------------------------------");
            System.out.println(YELLOW.concat("1.").concat(RESET).concat(" Start container"));
            System.out.println(YELLOW.concat("2.").concat(RESET).concat(" Stop container"));
            System.out.println(YELLOW.concat("3.").concat(RESET).concat(" Remove container"));
            System.out.println(YELLOW.concat("4.").concat(RESET).concat(" Inspect container"));
            System.out.println(YELLOW.concat("5.")
                                     .concat(RESET)
                                     .concat(" Plot resource usage for a running container"));
            System.out.println(YELLOW.concat("6.")
                                     .concat(RESET)
                                     .concat(
                                    " Store real time data for a running container in CSV file"));
            System.out.println(YELLOW.concat("7.").concat(RESET).concat(" List containers"));
            System.out.println(YELLOW.concat("8.").concat(RESET).concat(" Pull image"));
            System.out.println(YELLOW.concat("9.").concat(RESET).concat(" Remove image"));
            System.out.println(YELLOW.concat("10.").concat(RESET).concat(" Search image"));
            System.out.println(YELLOW.concat("11.").concat(RESET).concat(" List images")); 
            System.out.println(YELLOW.concat("12.").concat(RESET).concat(" Inspect Swarm"));
            System.out.println(YELLOW.concat("13.").concat(RESET).concat(" System info"));
            System.out.println(YELLOW.concat("14.").concat(RESET).concat(" History"));
            System.out.println("-------------------------------------------------------------");
        } while (handleUserInput());
    }

    /** Handles the user input. 
     * @return true if the user wants to run again, false otherwise.
    */
    public boolean handleUserInput() {
        Scanner INPUT = new Scanner(System.in);
        System.out.print("Please enter a number: ");
        int answer = 0;
        try {
            answer = INPUT.nextInt();
            spawnThreads(answer);
        } catch (InputMismatchException e) {
            System.out.println(ConsoleUnits.RED + "Please, enter a valid number." 
                             + ConsoleUnits.RESET);
            return true;
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return true;
        } finally {
            INPUT.nextLine();
        }
        System.out.println("\nWant to run again?\n\nFor YES press Y.\nFor NO press N. ");
        System.out.print("Enter answer: ");
        String choice = INPUT.nextLine();
        System.out.println();
        while (!(choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N"))) {
            System.out.print("\n".concat(ConsoleUnits.RED)
                               .concat("Wrong input, press 'Y' to continue or 'N' to exit: ")
                               .concat(RESET));
            choice = INPUT.nextLine();
        }
        if (choice.equalsIgnoreCase("N")) {
            System.out.println("Thank you!");
            INPUT.close();
            System.exit(0);
        }
        /* The user's input is case insensitive. */
        return choice.equalsIgnoreCase("Y");
    }

    /**
     * Spawn the threads.
     * @param answer the user's input.
     */
    private void spawnThreads(int answer) throws InvalidInputException {
        //Set the interface type that will appear in the history/log of the application.
        dataBaseThread.setInterfaceType("CLI");
        switch (answer) {
            case 1:
            case 2:
            case 3:
            case 8:
            case 9:
                //Spawn the executor thread.
                executorthread.setUserInput(answer);
                thread = new Thread(executorthread); 
                thread.setName("Executor"); 
                thread.start();
                waitThread();
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 13:
                //Spawn the monitor thread.
                monitorthread.setUserInput(answer);
                thread = new Thread(monitorthread);
                thread.setName("Monitor");
                thread.start();
                waitThread();
                break;
            case 14:
                dataBase.getHistoryList();
                break;
            default:
                throw new InvalidInputException("Please, type one of the recommended numbers.");
        }
    }
}

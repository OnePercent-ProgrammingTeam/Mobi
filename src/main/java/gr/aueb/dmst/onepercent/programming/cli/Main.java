package gr.aueb.dmst.onepercent.programming.cli;

import java.util.Scanner;



/** Class: Main runs the entire program. */
public class Main {

    /**
     * Method: handleInput(String outputMessage)
     * 
     * Get input from the user, display the specified output message, and return the user's input.
     * 
     * @param outputMessage The message to be printed before receiving input.
     * @return The user's input as a String.
     */
    public static String handleInput(String outputMessage) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(outputMessage);
        String input = scanner.next();
        System.out.println();
        scanner.nextLine(); 
       
        return input;
    } 

    /**
     * Main method to start the command-line interface (CLI) menu thread.
     * Creates an instance of MenuThreadCLI, initializes a thread with it, and starts the thread.
     * This method serves as the entry point for the CLI application.
     *
     * @param args The command-line arguments passed to the application (not used in this case).
     */
    public static void main(String[] args) {
        MenuThreadCLI menuThreadCLI = new MenuThreadCLI();
        Thread thread = new Thread(menuThreadCLI);
        thread.start();
    }

    /**
     * Default Constructor
     */
    public Main() {

    }
}

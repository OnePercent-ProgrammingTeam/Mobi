package gr.aueb.dmst.onepercent.programming.cli;

import java.util.Scanner;

/**
 * Utility class for console-related functionality, including ANSI color codes and input handling.
 * 
 * <p>This class provides ANSI color constants for styling console output, such as text color and
 * formatting. Additionally, it includes a method for reading input from the console.
 */
public class ConsoleUnits {
    /** ANSI escape reset color code */
    public static final String RESET = "\u001B[0m";
    /** ANSI escape green color code */
    public static final String GREEN = "\u001B[32m";
    /** ANSI escape red color code */
    public static final String RED = "\u001B[31m";
    /** ANSI escape yellow color code */
    public static final String YELLOW = "\u001B[33m";
    /** ANSI escape blue color code */
    public static final String BLUE = "\u001B[34m";

    /**
     * Prompts the user for input and returns the user's input as a String.
     * @param outputMessage The message to be printed before receiving input.
     * @return The user's input as a String.
    */
    public static String promptForInput(String outputMessage) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(outputMessage);
        String input = scanner.next();
        System.out.println();
        scanner.nextLine();
        return input;
    } 


}

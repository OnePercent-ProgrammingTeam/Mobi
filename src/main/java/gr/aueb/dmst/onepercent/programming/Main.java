package gr.aueb.dmst.onepercent.programming;

import java.util.Scanner;

/** Class: Main runs the entire program. */
public class Main {

    /** Get input from the user and handle it. */
    public static String handleInput(String outputMessage) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(outputMessage);
        String input = scanner.next();
        System.out.println();
        scanner.nextLine(); 
        //TO DO: check if it can be implented for testing 
        //scanner.close();;
        return input;
    } 

    public static void main(String[] args) {
        MenuThreadCLI menuThreadCLI = new MenuThreadCLI();
        Thread thread = new Thread(menuThreadCLI);
        thread.start();
    }
}

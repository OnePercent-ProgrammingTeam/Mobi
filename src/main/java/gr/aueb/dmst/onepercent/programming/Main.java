package gr.aueb.dmst.onepercent.programming;

import java.util.Scanner;

/** Class: Main runs the entire program */
public class Main {
    static final Scanner SC = new Scanner(System.in);

    /** Get input from the user and handle it */
    public static String handleInput(String outputMessage) {
        
        System.out.println(outputMessage);
        String input = SC.next();
        System.out.println();
        SC.nextLine(); //clear buffer
        return input;
    } 

    public static void main(String[] args) {
        MenuThread menuThread = new MenuThread();
        Thread thread = new Thread(menuThread);
        thread.start();

    }
}

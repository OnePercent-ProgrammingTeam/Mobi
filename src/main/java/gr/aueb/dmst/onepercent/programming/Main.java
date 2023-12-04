package gr.aueb.dmst.onepercent.programming;

import java.util.Scanner;

public class Main {
    static final Scanner SC = new Scanner(System.in);

    //handle user input and make sure it is valid
    public static String handleInput(String outputMessage){
        
        System.out.println(outputMessage);
        String input = SC.next();
        System.out.println();
        SC.nextLine();//clear buffer
        return input;
    } 

    public static void main(String[] args) {
        MenuThread menuThread = new MenuThread();
        Thread thread = new Thread(menuThread);
        thread.start();
    }
}

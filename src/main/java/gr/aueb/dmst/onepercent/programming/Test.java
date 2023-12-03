package gr.aueb.dmst.onepercent.programming;

import java.io.IOException;
import java.util.Scanner;

public class Test {

    // handle user input and make sure it is valid
    public static String handleInput(String outputMessage) {
        Scanner sc = new Scanner(System.in);
        System.out.println(outputMessage);
        String input = sc.next();
        sc.nextLine();// clear buffer
        return input;
    }

    public static void main(String[] args) {

        // ContainerManager cm = new ContainerManager();
        ContainerManager.createDockerClient();
        // cm.startContainer();
        // cm.stopContainer();
        // ContainerMonitorHttp obj = new ContainerMonitorHttp();
        // obj.getContainerStats();
        // CSV csv = new CSV();
        // csv.writeContainerInfoToCsv();

        MenuThread menuThread = new MenuThread();
        Thread thread = new Thread(menuThread);
        thread.start();
    }
}

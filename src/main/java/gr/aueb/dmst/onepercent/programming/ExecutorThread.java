package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;

import java.util.List;
import java.util.Scanner;

public class ExecutorThread implements Runnable{

    private DockerClient dockerClient; //declare a field dockerClient of type DockerClient 7that allows interaction with the Docker daemon

    public ExecutorThread(DockerClient dockerClient) { //the constructor of the ExecutorThread class, which takes a DockerClient as a parameter and initializes the dockerClient field.
        this.dockerClient = dockerClient;
    }

    @Override
    public void run() { //indicate the start of the run method, which will be executed when the thread starts
        while (true) {
            // Display options for the user
            displayOptions(); //calls the displayOptions method to show a menu of options to the user

            // Get user input
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            // Perform the chosen action
            switch (choice) {  //use switch statement to perform the action based on the user's choice. If the user chooses 1, it calls the startContainer method 2, it calls the stopContainer method, and if they choose 3, it returns, effectively exiting the thread. If the user enters an invalid choice, it prints an error message.
                case 1:
                    startContainer();
                    break;
                case 2:
                    stopContainer();
                    break;
                case 3:
                    return; // Exit the thread
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayOptions() { //print a menu of options for the user to choose from.
        System.out.println("-----------EXECUTOR THREAD OPTIONS-----------");
        System.out.println("1. Start Container");
        System.out.println("2. Stop Container");
        System.out.println("3. Exit Executor Thread");
        System.out.print("Enter your choice: ");
    }

    private void startContainer() { // the user enter the ID of the container to start,call the startContainerCmd method of the dockerClient to start the specified container.
        System.out.print("Enter the ID of the container to start: ");
        Scanner scanner = new Scanner(System.in);
        String containerId = scanner.nextLine();

        dockerClient.startContainerCmd(containerId).exec();
        System.out.println("Container started successfully.");
    }

    private void stopContainer() {
        System.out.print("Enter the ID of the container to stop: ");
        Scanner scanner = new Scanner(System.in);
        String containerId = scanner.nextLine();

        dockerClient.stopContainerCmd(containerId).exec();
        System.out.println("Container stopped successfully.");
    }
}
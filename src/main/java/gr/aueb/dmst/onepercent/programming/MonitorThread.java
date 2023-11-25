package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;


import java.util.List;

public class MonitorThread extends Thread implements Runnable {

    private DockerClient dockerClient; // declare a field dockerClient of type DockerClient that allows interaction with the Docker daemon.
    

    public MonitorThread(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    } // the constructor of the MonitorThread class, which takes a DockerClient as a parameter and initializes the dockerClient field

    @Override
    public void run() { // run method, which will be executed when the thread starts
        while (true) { //start a loop The thread will keep running as long as the program is running
            // Display information about containers
            displayContainerInfo(); //call the displayContainerInfo method to show information about Docker containers
            

            try { //This block puts the thread to sleep for a specified interval (in this case, 5000 milliseconds or 5 seconds) before looping again. The InterruptedException is caught in case of interruption during sleep.
                // Sleep for a specific interval before displaying information again
                Thread.sleep(5000); // Adjust the interval as needed (in milliseconds)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayContainerInfo() { //declare the displayContainerInfo method, which is responsible for retrieving and displaying information about Docker containers
        System.out.println("-----------------------CONTAINER INFORMATION-----------------------");

        // Get the list of all containers
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec(); //use the dockerClient to get a list of all containers, including stopped ones
        CSV csv = new CSV();
        csv.writeContainerInfoToCsv() ;

        // Display information for each container
        for (Container container : containers) { //start a loop that iterates through each container in the list.
            System.out.println("ID: " + container.getId());
            System.out.println("State: " + container.getState());
            System.out.println("Status: " + container.getStatus());
            System.out.println("Network: " + container.getNetworkSettings());
            System.out.println("Created: " + container.getCreated());
            System.out.println("-----------------------------------"); 
        }
    }
} //print various information about each container, such as ID, state, status, network settings, and creation time

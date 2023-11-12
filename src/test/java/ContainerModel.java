import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.ContainerPort;
import java.util.List;
import com.github.dockerjava.api.model.ContainerMount;

public class ContainerModel {
    
    private Container container;


    public ContainerModel(Container container) {
        this.container = container;
    }

    public String getContainerId()   {
        return container.getId();
    }

    /*returns the name of the image
     we have concluded that: Container Name <> Image Name*/
    public String getImageName(){  
        return container.getImage();
    }

    /* returns the id of the image
    (Starts with sha256:... indicating the cryptographic hash of the image)
    we have concluded that: Container Id <> Image Id*/
    public String getImageId() {
        return container.getImageId();
    }

    /*returns the name of the container
     we have concluded that: Container Name <> Image Name*/
    public String[] getNames(){
        return container.getNames();
    }

    /*returns the Size of Disk Read/Write
    if it is zero, it might mean that no disk I/O operations are occurring*/
    public Long getReadWriteSize() {
        return container.getSizeRw();
    }

    //We dont know yet
    public Long getSizeRootFs() {
        return container.getSizeRootFs();
    }

    /* returns statistics about the container
    public void getStats() {
         // Create a Docker client
         DockerClient dockerClient = DockerClientFactory.instance().client();

         // Replace "your-container-id" with the actual container ID or name
         String containerId = "your-container-id";
 
         // Get memory usage statistics for the container
         Statistics stats = dockerClient.statsCmd(containerId).exec();
 
         // Process the statistics to get memory-related information
         // Note: The structure of the Statistics object might vary based on your Docker version
         // You'll need to inspect the object to extract the relevant memory information
 
         // Example: Print the memory usage stats
         System.out.println("Memory Stats:");
         System.out.println("Usage: " + stats.getMemoryStats().getUsage());
         System.out.println("Limit: " + stats.getMemoryStats().getLimit());
         System.out.println("Max Usage: " + stats.getMemoryStats().getMaxUsage());
 
         // Close the Docker client when done
         dockerClient.close();
    } */

    public ContainerPort[] getPorts() {
        return container.getPorts();
    }

    // returns the command that was used to start the container
    public String getCommand() {
        return container.getCommand();
    }

    // returns the mounts of the container
    public List<ContainerMount> getMounts() {
        return container.getMounts();
    }

    /* prints: 
    1. the status of the container (Exited or Running) followed by a code 
    2. the time that has passed since the container was running (if it is running it prints: Up to *time*)
    */
    public String getStatus() {
        return container.getStatus();
    }
    
    /*  returns the time the container was created in unix timestamp,
     * in ContainerMonitor class we have a method that converts it to a formatted date 
     */
    public Long getCreated() {
        return container.getCreated();
    }

}

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.ContainerPort;
import java.util.List;
import com.github.dockerjava.api.model.ContainerMount;

public class ContainerModel {
    
    private Container container;

    /** Constructor */
    public ContainerModel(Container container) {
        this.container = container;
    }
    /** Get the id of a container */
    public String getContainerId()   {
        return container.getId();
    }

    /** Return the name of the image
     we have concluded that: Container Name <> Image Name*/
    public String getImageName(){  
        return container.getImage();
    }

    /** Return the id of the image
    (Starts with sha256:... indicating the cryptographic hash of the image)
    we have concluded that: Container Id <> Image Id*/
    public String getImageId() {
        return container.getImageId();
    }

    /** Return the name of the container
     we have concluded that: Container Name <> Image Name*/
    public String[] getNames(){
        return container.getNames();
    }

    /** Return the size of Disk Read/Write
    if it is zero, it might mean that no disk I/O operations are occurring*/
    public Long getReadWriteSize() {
        return container.getSizeRw();
    }

    //We have to check it out further
    public Long getSizeRootFs() {
        return container.getSizeRootFs();
    }

    /** Return a table with the ports of a container */
    public ContainerPort[] getPorts() {
        return container.getPorts();
    }

    /** Return the command that was used to start the container*/
    public String getCommand() {
        return container.getCommand();
    }

    /** Return a list with the mounts of the container*/
    public List<ContainerMount> getMounts() {
        return container.getMounts();
    }

    /** Return: 
    * 1. the status of the container (Exited or Running) followed by a code 
    * 2. the time that has passed since the container was running (if it is running it prints: Up to *time*)
    */
    public String getStatus() {
        return container.getStatus();
    }
    
    /**  Return the time the container was created in unix timestamp,
     * in ContainerMonitor class we have a method that converts it to a formatted date 
     */
    public Long getCreated() {
        return container.getCreated();
    }
}

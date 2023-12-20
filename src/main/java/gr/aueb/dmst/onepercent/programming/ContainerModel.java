package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;

/**
 * Class: ContainerModel class is used to create a model of a container.
 * It is used in ContainerMonitor class to print the information about the containers.
 * It's purpose is modeling the container and making it easier to print the information about it.
 */
public class ContainerModel {
    
    private Container container;

    /** Constructor: ContainerModel(Container).
     * @param container is the container that is going to be modeled.
     */
    public ContainerModel(Container container) {
        this.container = container; 
    }

    /** Method: getNames() returns the name of the locally installed containers.*/
    public String[] getNames() {
        return container.getNames();
    }

    /** Method: getId() returns the id of a container. */
    public String getId() {
        return container.getId();
    }

    /** Method: getImage() returns the image that was used to start the container. */
    public String getImage() {
        return container.getImage();
    }

    /** Method: getImageId() returns the id of the image.
    *(Starts with sha256:... indicating the cryptographic hash of the image).
    *Note that: Container Id <> Image Id.
    */
    public String getImageId() {
        return container.getImageId();
    }

    /** Method: getPorts() returns a table with the ports of the containers. */
    public ContainerPort[] getPorts() {
        return container.getPorts();
    }

    /** Method: getCommand() the command that was used to start the container.*/
    public String getCommand() {
        return container.getCommand();
    }

    /** Method: getStatus() returns: 
     * 1. the status of the container (Exited or Running) followed by a code. 
     * 2. the time that has passed since the container was running 
     * (if it is running it prints: Up to *time*).
     */
    public String getStatus() {
        return container.getStatus();
    }
    
    /** Method: getCreated() returns the time the container was created in unix timestamp.
     * @see ContainerMonitor class
     * In ContainerMonitor class, the method convertUnixToRealTime() 
     * converts it to a formatted date. 
     */
    public Long getCreated() {
        return container.getCreated();
    }
}

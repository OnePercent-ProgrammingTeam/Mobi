package gr.aueb.dmst.onepercent.programming;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.model.Container;
import java.util.Scanner;
import java.util.List;

public class ContainerManager extends ContainerManagement {


    
    /** Start container and check if it is already started */
    public void startContainer() {
        this.input = handleInput();
        if (input == null){return;} 
        if (checkActiveContainerStatus()) {
            System.out.println("Container already started");
            return;
        }
        dc.startContainerCmd(input).exec();
        System.out.println("Container started");
    }

    /** Stop container, practicly kill it and check if it is already stopped */
    public void stopContainer() {
        String lastIdInput = handleInput();
        if (lastIdInput == null){return;} 
        if(!checkActiveContainerStatus()) {
            System.out.println("Container already stopped");
            return;
        }
        dc.stopContainerCmd(handleInput()).exec();
        System.out.println("Container stopped");
    }

        /** Handle user input and make sure it is valid */
        public String handleInput(){
            System.out.println("Please enter the id of the container you want to start/stop");
            this.input = sc.next();
            if (!checkAllContainerStatus()) {
                System.out.println("Container does not exist");
                return null;
            }
            sc.nextLine(); //clear buffer
            return input;
        } 
    

    /** Check if container is active */
    public boolean checkActiveContainerStatus() {
        List<Container> containers;
        containers= dc.listContainersCmd().withShowAll(false).exec();
        for (Container c : containers) {
            if (c.getId().equals(input)) {
                return true; //container is active
            }
        }
        return false; //container is not active
    }

    /** Check if container exists */
    public boolean checkAllContainerStatus() {
        List<Container> containers;
        containers= dc.listContainersCmd().withShowAll(true).exec();
        for (Container c : containers) {
            if (c.getId().equals(input)) {
                return true; //container exists
            }
        }
        return false;//container does not exist
    }

}
        
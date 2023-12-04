//package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.model.Container;
import java.util.Scanner;
import java.util.List;

public class ContainerManager extends ContainerManagement {
  
    /** Handle user input and make sure it is valid */
    @Override
    public String handleInput(){
        System.out.println("Please enter the id of the container you want to start/stop");
        String input = sc.next();
        if (!checkAllContainerStatus(input)) {
            System.out.println("Container does not exist");
            return null;
        }
        sc.nextLine(); //clear buffer
        return input;
    } 
    
    /** Check if container has already started and if not, start it */
    public void startContainer() {
        String lastIdInput = handleInput();
        if (lastIdInput == null){return;} 
        if (checkActiveContainerStatus(lastIdInput)) {
            System.out.println("Container already started");
            return;
        }
        dc.startContainerCmd(lastIdInput).exec();
        System.out.println("Container started");
    }

    /** Check if container has already stopped and if not, stop it (practically kill it) */
    public void stopContainer() {
        String lastIdInput = handleInput();
        if (lastIdInput == null){return;} 
        if(!checkActiveContainerStatus(lastIdInput)) {
            System.out.println("Container already stopped");
            return;
        }
        dc.stopContainerCmd(handleInput()).exec();
        System.out.println("Container stopped");
    }


    /** Check if container is active */
    public boolean checkActiveContainerStatus(String idInput) {
        List<Container> containers;
        containers= dc.listContainersCmd().withShowAll(false).exec();
        for (Container c : containers) {
            if (c.getId().equals(idInput)) {
                return true; //container is active
            }
        }
        return false; //container is not active
    }

    /** Check if container exists */
    public boolean checkAllContainerStatus(String idInput) {
        List<Container> containers;
        containers= dc.listContainersCmd().withShowAll(true).exec();
        for (Container c : containers) {
            if (c.getId().equals(idInput)) {
                return true; //container exists
            }
        }
        return false; //container does not exist
    }
}
        
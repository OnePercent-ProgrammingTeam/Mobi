package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.model.Container;
import java.util.List;
import java.util.Scanner;

public class ManagerAPI  extends SuperAPI {
    
    

    /** Method: handleInput() handles user input and make sure it is valid. */
    public String handleInput() {
        /** Field: sc is a scanner object. */
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the id of the container you want to start/stop");
        String input = sc.next();
        if (!checkAllContainerStatus(input)) {
            System.out.println("Container does not exist");
            return null;
        }
        sc.nextLine(); //clear buffer
        sc.close();
        return input;
    } 
    
    /** Method: startContainer checks if container has already started and if not, start it. */
    public void startContainer() {
        String lastIdInput = handleInput();
        if (lastIdInput == null) { 
            return; } 
        if (checkActiveContainerStatus(lastIdInput)) {
            System.out.println("Container already started");
            return;
        }
        dc.startContainerCmd(lastIdInput).exec();
        System.out.println("Container started");
    }

    /** Method: stopContainer() checks if container has already stopped. 
     * If not, stop it. */
    public void stopContainer() {
        String lastIdInput = handleInput();
        if (lastIdInput == null) {
            return; } 
        if (!checkActiveContainerStatus(lastIdInput)) {
            System.out.println("Container already stopped");
            return;
        }
        dc.stopContainerCmd(handleInput()).exec();
        System.out.println("Container stopped");
    }


    /** Method: checkActiveContainerStatus checks if container is active. */
    public boolean checkActiveContainerStatus(String idInput) {
        List<Container> containers;
        containers = dc.listContainersCmd().withShowAll(false).exec();
        for (Container c : containers) {
            if (c.getId().equals(idInput)) {
                return true; //container is active
            }
        }
        return false; //container is not active
    }

    /** Method: checkAllContainerStatus checks if container exists. */
    public boolean checkAllContainerStatus(String idInput) {
        List<Container> containers;
        containers = dc.listContainersCmd().withShowAll(true).exec();
        for (Container c : containers) {
            if (c.getId().equals(idInput)) {
                return true; //container exists
            }
        }
        return false; //container does not exist
    }
}

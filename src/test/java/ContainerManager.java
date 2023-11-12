import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.model.Container;
import java.util.Scanner;
import java.util.List;

public class ContainerManager {

    protected static DockerClient dc; //docker client 
    Scanner sc = new Scanner(System.in); //scanner for user input

    //create docker client
    public static void createDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig
        .createDefaultConfigBuilder()
        .withDockerHost("tcp://localhost:2375") //daemon host
        .build();
        dc = DockerClientBuilder.getInstance(config).build();
        dc.versionCmd().exec();
    }

    //handle user input and make sure it is valid
    public String handleInput(){
        System.out.println("Please enter the id of the container you want to start/stop");
        String input = sc.next();
        if (!checkAllContainerStatus(input)) {
            System.out.println("Container does not exist");
            return null;
        }
        sc.nextLine();//clear buffer
        return input;
    } 
    
    // start container and check if it is already started
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

    // stop container, practicly kill it and check if it is already stopped
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


    // check if container is active
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

    // check if container exists
    public boolean checkAllContainerStatus(String idInput) {
        List<Container> containers;
        containers= dc.listContainersCmd().withShowAll(true).exec();
        for (Container c : containers) {
            if (c.getId().equals(idInput)) {
                return true; //container exists
            }
        }
        return false;//container does not exist
    }

}
    
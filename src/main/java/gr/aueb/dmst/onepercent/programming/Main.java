package gr.aueb.dmst.onepercent.programming;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder; 
import java.util.List;


public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        
        /* Used the instructions below instead of the usage of the deprecated method:
         * DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder();
         *  DockerClient dockerClient = DockerClientBuilder.getInstance(builder).build();
         */

        DefaultDockerClientConfig config = DefaultDockerClientConfig
        .createDefaultConfigBuilder()
        .withDockerHost("tcp://localhost:2375")
        .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
        dockerClient.versionCmd().exec();
        
         
        
        List<Container> containers;
        System.out.println("-----------------------ALL CONTAINER INSTANCES-----------------------");
        containers= dockerClient.listContainersCmd().withShowAll(true).exec();
        containers.forEach(c-> System.out.println("ID: " + c.getId() + "\nState: " + c.getState() + "\nStatus: " + c.getStatus() + "\nNetwork: "  + c.getNetworkSettings() + "\nCreated: " + c.getCreated() + "\n\n"));
        String id = containers.get(0).getId();
        dockerClient.stopContainerCmd(id).exec();
        Thread.sleep(1000);
        System.out.println("-----------------------ACTIVE CONTAINER INSTANCES-----------------------");
        containers = dockerClient.listContainersCmd().withShowAll(false).exec();
        containers.forEach(c-> System.out.println(c.getId() + " " + c.getState()));
        dockerClient.startContainerCmd(id).exec();
        System.out.println("-----------------------------------");
        containers = dockerClient.listContainersCmd().withShowAll(false).exec();
        containers.forEach(c-> System.out.println(c.getId() + " " + c.getState()));
    }
}
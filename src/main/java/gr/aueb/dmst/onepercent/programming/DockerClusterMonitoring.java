package gr.aueb.dmst.onepercent.programming;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.model.SwarmNode; 

public class DockerClusterMonitoring {
    public static void main(String[] args) {
    // Create a Docker client
    DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
    .withDockerHost("tcp://localhost:2375").build();
    DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
    dockerClient.versionCmd().exec();
    // after initializing a docker swarm on your machine, use a loop to print infos about the 
    //nodes of the cluster
    for (SwarmNode node : dockerClient.listSwarmNodesCmd().exec()) {
        System.out.println("Node ID: " + node.getId());
        System.out.println("Node State: " + node.getStatus().getState());
        System.out.println("Node Availability: " + node.getSpec().getAvailability());
        System.out.println("Node Version" + node.getVersion());
        System.out.println("Node Created at: " + node.getCreatedAt());
        System.out.println("Node Updated at: " + node.getUpdatedAt());
        System.out.println("Node Description: " + node.getDescription()); 
        }
    }
}

package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.util.Scanner;


public class ContainerManagement {

    /** Static variable, used in many classes, representing the docker client */
    protected static DockerClient dc;
    /** Global variable scanner, used in the methods below */
    Scanner sc = new Scanner(System.in); // scanner for user input

    protected String input;

    /** Create a docker client */
    public static void createDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost("tcp://localhost:2375") // daemon host
                .build();
        dc = DockerClientBuilder.getInstance(config).build();
        dc.versionCmd().exec();
    }

    public String handleInput() {
        return this.input;
    }
}

package gr.aueb.dmst.onepercent.programming.core;
//package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.annotations.VisibleForTesting;

/** Class: SuperAPI is a superclass that contains a static variable, used in other classes, 
 *  representing the docker client.
 * It is used to create a docker client.
 */
public class SuperAPI {
    
    /** Field: dc is a static variable, used in many classes, representing the docker client */
    public static DockerClient dc;

    /**
     * Gets the Docker client instance.
     *
     * @return The DockerClient instance represented by the static variable dc.
     *
     * This method is intended for testing purposes.
     * It should only be used in testing scenarios to expose certain behavior for testing.
     */
    @VisibleForTesting
    public static DockerClient getDc() {
        return dc;
    }

    /** Method: createDockerClient() creates a docker client */
    public static void createDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withDockerHost("tcp://localhost:2375") //daemon host
            .build();
        dc = DockerClientBuilder.getInstance(config).build();
        dc.versionCmd().exec();
    }

    /**
     * Default Constructor
     */
    public SuperAPI() {

    }
}

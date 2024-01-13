package gr.aueb.dmst.onepercent.programming;
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
    protected static DockerClient dc;
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

}

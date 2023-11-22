package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Initialize Docker client
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost("tcp://localhost:2375")
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
        dockerClient.versionCmd().exec();

        // Create and start the MonitorThread
        MonitorThread monitorThread = new MonitorThread(dockerClient);
      
        Thread thread= new Thread(monitorThread);
        thread.start();
        // Create and start the ExecutorThread
        //ExecutorThread executorThread = new ExecutorThread(dockerClient);
       // executorThread.start();
        //monitorThread.join();
        //executorThread.join(); //use the join method to wait for the monitorThread and executorThread to finish. This ensures that the main method doesn't exit until both threads have completed their execution.
    }
}




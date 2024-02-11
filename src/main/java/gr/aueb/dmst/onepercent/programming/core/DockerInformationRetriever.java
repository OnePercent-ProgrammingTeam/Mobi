package gr.aueb.dmst.onepercent.programming.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.annotations.VisibleForTesting;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides methods for retrieving information about the docker objects
 * installed in the user's system. 
 * 
 * <p>The class provides methods for retrieving information about docker objects such as:
 * names, IDs, status, and time of creation.
 * 
 * <p>In constract to other classes that communicate with the docker daemon via the end points 
 * specified in <a href="https://docs.docker.com/engine/api/v1.43/">Docker Engine API v1.43</a>,
 * it uses the <a href="https://github.com/docker-java/docker-java">docker-java library</a> from
 * Github.
 * 
 */
public class DockerInformationRetriever {
    
    /** The Docker client instance. */
    public static DockerClient dockerClient;
    
    /** List of containers installed in user's system. */
    List<Container> containers = new ArrayList();

    /** Default Constructor. */
    public DockerInformationRetriever() { }

    /**
     * Retrieves the list of ContainerModels.
     * This method is annotated with @VisibleForTesting 
     * to indicate that its visibility has been expanded
     * for testing purposes, allowing tests to access the containerModels directly.
     *
     * @return List of ContainerModels containing information about locally installed containers.
     */
    @VisibleForTesting
    public List<Container> getContainersList() {
        return containers;
    }
 
    /**
     * Retrieves the Docker client instance.
     * This method is annotated with @VisibleForTesting
     * to indicate that its visibility has been expanded
     * for testing purposes, allowing tests to access the Docker client directly.
     * 
     * @return DockerClient instance
     */
    @VisibleForTesting
    public static DockerClient getDc() {
        return dockerClient;
    }

    /**
     * Creates a Docker client instance.
     * 
     * Initializes a Docker client using the default configuration
     * with the Docker daemon running on localhost at port 2375. It builds the
     * Docker client configuration and creates the Docker client instance using
     * the DockerClientBuilder.
     * 
     * @see com.github.dockerjava.api.DockerClient
     * @see com.github.dockerjava.core.DefaultDockerClientConfig
     */
    public static void createDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withDockerHost("tcp://localhost:2375") //daemon host
            .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
        dockerClient.versionCmd().exec();
    }

    /**
     * Initializes the list of locally installed containers.
     * 
     * @param showAll indicates whether to show both stopped and running containers (true) 
     * or only running ones (false)
     */
    public void initializeContainerList(boolean showAll) {
        containers = dockerClient.listContainersCmd().withShowAll(showAll).exec();
    }

   /** 
    * Prints a formatted list of container names, IDs, statuses, and creation timestamps.
    */
    public void printContainerList() {
        System.out.printf("%-30s%-70s%-30s%-20s%n", 
                          "Container Name", 
                          "Container Id", 
                          "Status", 
                          "Time Created");
        System.out.println(" ");
        /* Loop through the list of containers and print the information. */
        containers.forEach(c -> {
            long unixTimestamp = c.getCreated();
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), 
                                                                          ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            for (String name: c.getNames())
                System.out.printf("-%-29s", name.substring(1));

            System.out.printf("%-70s%-30s%-20s%n", c.getId(), c.getStatus(), formattedDateTime);
        
        });
    } 
   
    /** Print the status of each container in the list of locally installed containers. */
    public void printContainerStatus() {
        containers.forEach(c -> System.out.println(c.getStatus()));
    }

   /**
    * Checks whether a container is running or not.
    * @param id the id of the container
    * @return true if the container is running, false otherwise
    */
    public boolean isContainerRunning(String id) {
        for (Container c : containers) {
            if (c.getId().equals(id)) {
                return c.getStatus().split(" ")[0].equals("Up");
            } 
        }
        return false;
    }
 
    /*
     * Retrieves a list of names of the containers that are installed in the user's system.
     * 
     * @return An ArrayList of Strings representing the container names.
     */
    public ArrayList<String> getContainerNames() {
        ArrayList<String> nameList = new ArrayList<>();
        for (Container c : containers) {
            StringBuilder namesBuilder = new StringBuilder();
            for (String name : c.getNames()) {
                /* Remove the leading '/' from the container name. */
                namesBuilder.append(name.substring(1)).append(" ");
            }
            /* Remove the trailing whitespace from the container name. */
            String formattedNames = namesBuilder.toString().trim();
            nameList.add(formattedNames);
        }
        return nameList;
    }

    /**
     * Retrieves a list of container IDs that are installed in the user's system.
     *
     * @return An ArrayList of Strings representing the container IDs.
     */
    public ArrayList<String> getIdList() {
        ArrayList<String> id = new ArrayList<String>();
        containers.forEach(c -> {
            id.add(c.getId());
        });
        return id;
    }

    /**
     * Retrieves a list with the status of each container.
     *
     * @return An ArrayList of Strings representing the container status.
     */
    public ArrayList<String> getStatusList() {
        ArrayList<String> status = new ArrayList<String>();
        containers.forEach(c -> {
            status.add(c.getStatus());
        });
        return status;
    }

    /**
     * Retrieves a list of formatted creation timestamps of the installed containers.
     *
     * @return An ArrayList of Strings representing the formatted creation timestamps.
     */
    public ArrayList<String> getTimeCreatedList() {
        ArrayList<String> created = new ArrayList<String>();
        containers.forEach(c -> {
            long unixTimestamp = c.getCreated();

            /* Convert Unix timestamp to LocalDateTime. */
            LocalDateTime dateTime = LocalDateTime
                                    .ofInstant(Instant.ofEpochSecond(unixTimestamp), 
                                                            ZoneId.systemDefault());
            /* Format the LocalDateTime. */
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            created.add(formattedDateTime);
        }); 
        return created;
    }

    /**
     * Retrieves a list of image IDs of the locally installed images.
     *
     * @return An ArrayList of Strings representing the image IDs.
     */
    public ArrayList<String> getImageIdList() {
        ArrayList<String> imageid = new ArrayList<String>();
        containers.forEach(c -> {
            imageid.add(c.getImageId());
        });
        return imageid;
    }

    /**
     * Retrieves a list of image names of the locally installed images.
     *
     * @return An ArrayList of Strings representing the image names.
     */
    public ArrayList<String> getImageNameList() {
        ArrayList<String> imagename = new ArrayList<String>();
        containers.forEach(c -> {
            imagename.add(c.getImage());
        });
        return imagename;
    }
}

package gr.aueb.dmst.onepercent.programming.core;

import com.github.dockerjava.api.model.Container;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.github.dockerjava.api.model.ContainerPort;
import com.google.common.annotations.VisibleForTesting;


/** Class: MonitorAPI is a class that contains methods that retrieves information
 * about the containers, it monitors the docker system. It is a subclass of SuperAPI.
 * @see SuperAPI
 */
public class MonitorAPI extends SuperAPI {
    /** Field: List<ContainerModel> is the list of container models. */
    List<ContainerModel> containerModels = new ArrayList();

    /**
     * Retrieves the list of ContainerModels.
     * This method is annotated with @VisibleForTesting 
     * to indicate that its visibility has been expanded
     * for testing purposes, allowing tests to access the containerModels directly.
     *
     * @return List of ContainerModels containing information about locally installed containers.
     */
    @VisibleForTesting
    public List<ContainerModel> getContainerModels() {
        return containerModels;
    }
        
    /**
     * Method: initializeContainerModels(boolean showAll) initializes container models 
     * based on the locally installed containers.
     * It retrieves a list of containers, considering the 'showAll' parameter
     * and creates corresponding ContainerModel instances.
     *
     * @param showAll Indicates whether to include both stopped and running containers (true) 
     * or only running ones (false)
     */
    public void initializeContainerModels(boolean showAll) {
        List<Container> containers;
        containers = MonitorAPI.dc.listContainersCmd().withShowAll(showAll).exec();
        containers.forEach(c -> {
            if (c != null)
                 containerModels.add(new ContainerModel(c));
        }); // add all containers to containerModels list
    }

   /** Method: getContainerList() prints the names of the locally installed containers, their ids, 
    *  their status and the time they were created. 
    */
    public void getContainerList() {
        System.out.printf("%-30s%-70s%-30s%-20s%n", 
                          "Container Name", 
                          "Container Id", 
                          "Status", 
                          "Time Created");
        System.out.println(" ");
        containerModels.forEach(c -> {
            long unixTimestamp = c.getCreated();
            LocalDateTime dateTime = LocalDateTime
                                    .ofInstant(Instant
                                               .ofEpochSecond(unixTimestamp), 
                                                              ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            for (String name: c.getNames())
                System.out.printf("-%-29s", name.substring(1));

            System.out.printf("%-70s%-30s%-20s%n", c.getId(), c.getStatus(), formattedDateTime);
        
        });
    } 

   /** Method: getContainerPorts() prints the information about the ports of the containers. */  
    public void getContainerPorts() {
        containerModels.forEach(c -> {
            for (ContainerPort port: c.getPorts()) {
                System.out.println(port.getIp() + " ");
                System.out.println(port.getPrivatePort() + " ");
                System.out.println(port.getPublicPort() + " ");
                System.out.println(port.getType() + " ");
            }   
            System.out.println("\n--------------------------------\n"); 
        });
    }

    /** Method: getContainerCommands() prints the command that was used to create the 
     *  container. */  
    public void getContainerCommands() {
        containerModels.forEach(c -> System.out.println(c.getCommand()));
    }

   
    /** Method: getContainerStatus() prints the status of the container as described in 
     *  ContainerModel.java. */
    public void getContainerStatus() {
        containerModels.forEach(c -> System.out.println(c.getStatus()));
    }


    /**
     * ok
     * @param id ok
     * @return ok
     */
    public boolean getContainerStatus(String id) {
        for (ContainerModel c : containerModels) {
            if (c.getId().equals(id)) {
                return c.getStatus().split(" ")[0].equals("Up");
            } 
        }
        return false;
    }

   /**
   *  Method: convertUnixToRealTime() retrieves and prints the creation date of each container.
   * 
   * This method iterates the list of container models and prints the creation date
   * of each container. 
   * unixTimestamp is expressed in seconds since the Unix epoch 
   * (January 1, 1970, 00:00:00 UTC).
   * 
   *    An alternative that might result in an exception.
   *    <code>
   *    import java.util.Date;
   *    import java.text.SimpleDateFormat ;
   *        .
   *        .
   *        .
   *    var containerInfo = ContainerManager.dc.inspectContainerCmd(c.getContainerId()).exec();
   *    var createdTimestamp = Integer.parseInt(containerInfo.getCreated());
   *    Date createdDate = new Date(createdTimestamp * 1000);
   *    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   *    String formattedDate = dateFormat.format(createdDate);
   *    System.out.println("Container created date (Docker Java API): " + formattedDate);
   *    </code>
   */
    public void convertUnixToRealTime() {
        containerModels.forEach(c -> {
            long unixTimestamp = c.getCreated();

            // Convert Unix timestamp to LocalDateTime
            LocalDateTime dateTime = LocalDateTime
                                    .ofInstant(Instant
                                               .ofEpochSecond(unixTimestamp), 
                                                              ZoneId.systemDefault());

            // Format the LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            System.out.println("Unix Timestamp: " + unixTimestamp);
            System.out.println("Formatted Date Time: " + formattedDateTime);
            System.out.println("\n--------------------------------\n");
        });
    }
    //TO DO: Check if it is needed
    /**
     * Retrieves information about containers, including their IDs, names, image IDs
     * and creation timestamps.
     * Converts Unix timestamps to human-readable date-time format.
     *
     * @return A 2D array containing container information, where each row represents a container
     *         and columns contain ID, formatted names, image ID, and creation timestamp.
     */
    public String[][] getContainerInfo() {
        ArrayList<String[]> containerInfo = new ArrayList<>();
  
        for (ContainerModel c : containerModels) {
            long unixTimestamp = c.getCreated();
            LocalDateTime dateTime = LocalDateTime
                                     .ofInstant(Instant
                                                .ofEpochSecond(unixTimestamp), 
                                                               ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
  
            StringBuilder namesBuilder = new StringBuilder();
            for (String name : c.getNames()) {
                namesBuilder.append("-").append(name.substring(1)).append(" ");
            }
            String formattedNames = namesBuilder.toString().trim();
  
            String[] containerData = new String[4]; 
            containerData[1] = formattedNames; 
            containerData[0] = c.getId(); 
            containerData[2] = c.getImageId(); 
            containerData[3] = formattedDateTime; 
  
            containerInfo.add(containerData);
        }
  
        // Convert ArrayList<String[]> to String[][]
        String[][] containerArray = new String[containerInfo.size()][4];
        for (int i = 0; i < containerInfo.size(); i++) {
            containerArray[i] = containerInfo.get(i);
        }
  
        return containerArray;
    }

    /**
     * Retrieves a list of formatted container names from the ContainerModels.
     *
     * @return An ArrayList of Strings representing the formatted container names.
     */
    public ArrayList<String> getNameList() {
        ArrayList<String> nameList = new ArrayList<>();
        for (ContainerModel c : containerModels) {
            StringBuilder namesBuilder = new StringBuilder();
            for (String name : c.getNames()) {
                namesBuilder.append(name.substring(1)).append(" ");
            }
            String formattedNames = namesBuilder.toString().trim();
            nameList.add(formattedNames);
        }
        return nameList;
    }

    /**
     * Retrieves a list of container IDs from the ContainerModels.
     *
     * @return An ArrayList of Strings representing the container IDs.
     */
    public ArrayList<String> getIdList() {
        ArrayList<String> id = new ArrayList<String>();
        containerModels.forEach(c -> {
            id.add(c.getId());
        });
        return id;
    }

    /**
     * Retrieves a list of container statuses from the ContainerModels.
     *
     * @return An ArrayList of Strings representing the container statuses.
     */
    public ArrayList<String> getStatusList() {
        ArrayList<String> status = new ArrayList<String>();
        containerModels.forEach(c -> {
            status.add(c.getStatus());
        });
        return status;
    }

    /**
     * Retrieves a list of formatted creation timestamps for each container in ContainerModels.
     *
     * @return An ArrayList of Strings representing the formatted creation timestamps.
     */
    public ArrayList<String> getTimeCreatedList() {
        ArrayList<String> created = new ArrayList<String>();
        containerModels.forEach(c -> {
            long unixTimestamp = c.getCreated();

            // Convert Unix timestamp to LocalDateTime
            LocalDateTime dateTime = LocalDateTime
                                    .ofInstant(Instant
                                               .ofEpochSecond(unixTimestamp), 
                                                              ZoneId.systemDefault());

            // Format the LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            created.add(formattedDateTime);
        }); 
        return created;
    }

    /**
     * Retrieves a list of image IDs associated with the containers from the ContainerModels.
     *
     * @return An ArrayList of Strings representing the image IDs.
     */
    public ArrayList<String> getImageIdList() {
        ArrayList<String> imageid = new ArrayList<String>();
        containerModels.forEach(c -> {
            imageid.add(c.getImageId());
        });
        return imageid;
    }

    /**
     * Retrieves a list of image names associated with the containers from the ContainerModels.
     *
     * @return An ArrayList of Strings representing the image names.
     */
    public ArrayList<String> getImageNameList() {
        ArrayList<String> imagename = new ArrayList<String>();
        containerModels.forEach(c -> {
            imagename.add(c.getImage());
        });
        return imagename;
    }

    /**
     * Default Constructor
     */
    public MonitorAPI() {

    }
}

package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.model.Container;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.github.dockerjava.api.model.ContainerPort;


/** Class: MonitorAPI is a class that contains methods that retrieves information
 * about the containers, it monitors the docker system. It is a subclass of SuperAPI.
 * @see SuperAPI
 */
public class MonitorAPI extends SuperAPI {
   /** Field: List<ContainerModel> is the list of container models */
   List<ContainerModel> containerModels = new ArrayList();
        
   /** Method: initializeContainerModels() pass the locally installed containers to a list of container models*/
   public void initializeContainerModels(){
      List<Container> containers;
      containers = MonitorAPI.dc.listContainersCmd().withShowAll(true).exec();
      containers.forEach(c->{
         if (c!=null)
            containerModels.add(new ContainerModel(c));
      }); // add all containers to containerModels list
   }

   /** Method: getContainerList() prints the names of the locally installed containers, their ids, 
    *  their status and the time they were created 
    */
   public void getContainerList() {
      System.out.printf("%-30s%-70s%-30s%-20s%n", "Container Name", "Container Id", "Status", "Time Created");
      System.out.println(" ");
      containerModels.forEach(c-> {
         long unixTimestamp = c.getCreated();
         LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault());
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         String formattedDateTime = dateTime.format(formatter);
         for (String name: c.getNames())
            System.out.printf("-%-29s", name.substring(1));

            System.out.printf("%-70s%-30s%-20s%n", c.getId(), c.getStatus(), formattedDateTime);
        
      });
   }

   /** Method: getContainerPorts() prints the information about the ports of the containers */  
   public void getContainerPorts() {
      containerModels.forEach(c-> {
         for (ContainerPort port: c.getPorts()){
            System.out.println(port.getIp() + " " );
            System.out.println(port.getPrivatePort() + " " );
            System.out.println(port.getPublicPort() + " " );
            System.out.println(port.getType() + " " );
         }   
         System.out.println("\n--------------------------------\n"); 
      });
   }

   /** Method: getContainerCommands() prints the command that was used to create the container */  
   public void getContainerCommands() {
        containerModels.forEach(c -> System.out.println(c.getCommand()));
   }

   
   /**  Method: getContainerStatus() prints the status of the container as described in ContainerModel.java */
   public void getContainerStatus() {
      containerModels.forEach(c -> System.out.println(c.getStatus()));
   }

   /**
   *  Method: convertUnixToRealTime() retrieves and prints the creation date of each container.
   * 
   * This method iterates the list of container models and prints the creation date
   * of each container. 
   * @param unixTimestamp is expressed in seconds since the Unix epoch (January 1, 1970, 00:00:00 UTC)
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
         LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault());

         // Format the LocalDateTime
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         String formattedDateTime = dateTime.format(formatter);
         System.out.println("Unix Timestamp: " + unixTimestamp);
         System.out.println("Formatted Date Time: " + formattedDateTime);
         System.out.println("\n--------------------------------\n");
      });
   }
}

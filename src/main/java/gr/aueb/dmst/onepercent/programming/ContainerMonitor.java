package gr.aueb.dmst.onepercent.programming;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import java.util.List;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.api.model.ContainerMount;
import java.util.Date;

public class ContainerMonitor {
     
     List<ContainerModel> containerModels =  new ArrayList();
        
    /**  Pass the locally installed containers to a list of container models*/
     public void initializeContainerModel(){
        List<Container> containers;
        containers = ContainerManager.dc.listContainersCmd().withShowAll(true).exec();
        containers.forEach(c->{
                                 if (c!=null)
                                    containerModels.add(new ContainerModel(c));
                              }
                           ); // add all containers to containerModels list
      }

      /** Print the names of the containers. As it is mentioned in ContainerModel class,
       * Container Name <> Image Name
       */
     public void getContainerNames() {
        containerModels.forEach(c-> {
                                 for (String name: c.getNames())
                                    System.out.print(name + " " );
                                    
                                 System.out.println("\n"); 
                                 });
     }

   /** Print the information about the ports of the containers */  
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

   /** Print the command that was used to create the container */  
   public void getContainerComands() {
        containerModels.forEach(c -> System.out.println(c.getCommand()));
   }

    // we have to check it out further
    public void getContainerMounts() {
      containerModels.forEach(c ->{
                           for(ContainerMount mount: c.getMounts()){
                              System.out.println(mount.getName());
                              System.out.println(mount.getDestination());
                           }
                           System.out.println("\n--------------------------------\n");
                               } );
   }
   
   /** Print the status of the container as described in ContainerModel.java */
   public void getContainerStatus() {
      containerModels.forEach(c -> System.out.println(c.getStatus()));
   }

   /**
   * Retrieve and print the creation date of each container.
   * 
   * This method iterates the list of container models and prints the creation date
   * of each container. 
   * @param unixTimestamp is expressed in seconds since the Unix epoch (January 1, 1970, 00:00:00 UTC)
   * 
   *    An alternative that might result in an exception.
   *    <code>
   *    var containerInfo = ContainerManager.dc.inspectContainerCmd(c.getContainerId()).exec();
   *    var createdTimestamp = Integer.parseInt(containerInfo.getCreated());
   *    Date createdDate = new Date(createdTimestamp * 1000);
   *    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   *    String formattedDate = dateFormat.format(createdDate);
   *    System.out.println("Container created date (Docker Java API): " + formattedDate);
   *    </code>
   */
   public void getContainerCreated() {
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

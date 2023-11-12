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
        
    
     public void initializeContainerModel(){
        List<Container> containers;
        containers = ContainerManager.dc.listContainersCmd().withShowAll(true).exec();
        containers.forEach(c->{
                                 if (c!=null)
                                    containerModels.add(new ContainerModel(c));
                              }
                           ); // add all containers to containerModels list
      }
      // prints the name of the containers
     public void getContainerNames() {
        containerModels.forEach(c-> {
                                 for (String name: c.getNames())
                                    System.out.print(name + " " );
                                    
                                 System.out.println("\n"); 
                                 });
     }

   // prints the information about the ports of the containers  
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

   // prints the command that was used to create the container  
   public void getContainerComands() {
        containerModels.forEach(c -> System.out.println(c.getCommand()));
   }

    // we have to check it out
    public void getContainerMounts() {
      containerModels.forEach(c ->{
                           for(ContainerMount mount: c.getMounts()){
                              System.out.println(mount.getName());
                              System.out.println(mount.getDestination());
                           }
                           System.out.println("\n--------------------------------\n");
                               } );
   }
   
   // prints the status of the container as dercibted in ContainerModel.java
   public void getContainerStatus() {
      containerModels.forEach(c -> System.out.println(c.getStatus()));
   }

   // prints the time the container was created in unix timestamp and formatted date
   public void getContainerCreated() {
      containerModels.forEach(c -> {
         /*
         This should run too but there is an Exception occuring, i will check it out - Scobioala
         var  containerInfo = ContainerManager.dc.inspectContainerCmd(c.getContainerId()).exec();
         var createdTimestamp =Integer.parseInt(containerInfo.getCreated());
         Date createdDate = new Date(createdTimestamp*1000);
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         String formattedDate = dateFormat.format(createdDate);
         System.out.println(c.getCreated());
         System.out.println("Container created date: " + formattedDate);   */
         
         //unixTimestamp is expressed in seconds since the Unix epoch (January 1, 1970, 00:00:00 UTC)
         long unixTimestamp = c.getCreated();
         // Convert Unix timestamp to LocalDateTime
         LocalDateTime dateTime = LocalDateTime
                                  .ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneId.systemDefault());
         // Format the LocalDateTime
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         String formattedDateTime = dateTime.format(formatter);
         System.out.println("Unix Timestamp: " + unixTimestamp);
         System.out.println("Formatted Date Time: " + formattedDateTime);
         System.out.println("\n--------------------------------\n");
      });
}
}

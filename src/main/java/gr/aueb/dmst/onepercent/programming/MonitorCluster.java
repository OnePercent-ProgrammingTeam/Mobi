package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.model.SwarmNode; 

/** Class: MonitorCluster is a class that contains methods that retrieves information
 * about the nodes of the docker cluster, it monitors the docker system. It is a 
 * subclass of SuperAPI.
 * @see SuperAPI
 */
public class MonitorCluster extends SuperAPI {
    
    /** Method: inspectNode() prints info for the nodes of the docker cluster */
    public void inspectNode() {
        for (SwarmNode node : dc.listSwarmNodesCmd().exec()) {
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

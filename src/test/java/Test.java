public class Test {
    public static void main (String[] args) {
        
                //ContainerManager cm = new ContainerManager();
                ContainerManager.createDockerClient();
                //cm.startContainer();
                //cm.stopContainer(); */
   
                // you should insert the container id of the container you want to start/stop
                // try invalid id to see the error message as well and the other cases, 
                //everything is properly handled
        
                
                ContainerMonitor containerMonitor = new ContainerMonitor();
                containerMonitor.initializeContainerModel();
                containerMonitor.getContainerCreated();


    }
}


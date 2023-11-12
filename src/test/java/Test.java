import java.util.Scanner;
public class Test {

    //handle user input and make sure it is valid
    public String handleInput(String outputMessage){
        Scanner sc = new Scanner(System.in);
        System.out.println(outputMessage);
        String input = sc.next();
        sc.nextLine();//clear buffer
        sc.close();
        return input;
    } 

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


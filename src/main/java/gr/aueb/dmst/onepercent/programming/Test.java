package gr.aueb.dmst.onepercent.programming;
import java.io.IOException;
import java.util.Scanner;
public class Test {

    //handle user input and make sure it is valid
    public static String handleInput(String outputMessage){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the container ID to " + outputMessage + " the container: ");
        String input = sc.next();
        sc.nextLine();//clear buffer
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
        
                
                //ContainerManagerHttp.pullImage();
                //ContainerManagerHttp.startContainer();
                
                //ContainerManagerHttp.stopContainer();
                
                
                
                //ContainerMonitorHttp.getContainerInformation();
                // ContainerMonitorHttp.getContainerInformation();
                
               // ContainerMonitorHttp obj = new ContainerMonitorHttp();
                //obj.getContainerStats();
                
                CSV csv = new CSV();
                csv.writeContainerInfoToCsv();
               


    }
}


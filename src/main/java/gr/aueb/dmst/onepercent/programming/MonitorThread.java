package gr.aueb.dmst.onepercent.programming;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;


import java.util.List;

public class MonitorThread extends Thread implements Runnable {
    private int userInput;

    protected void setUserInput(int userInput) {
        this.userInput = userInput;
    }

    @Override
    public void run() { 
        var containerMonitorHttp = new ContainerMonitorHttp();
        if (this.userInput == 3) {
            containerMonitorHttp.getContainerInformation();
        } else if (this.userInput == 4) {
            ContainerVisualization.main(new String[] {"Plot","Diagram"});    
        } else if (this.userInput ==5 ) {
            containerMonitorHttp.searchImages(); 
        }
        
    
    }

}
 


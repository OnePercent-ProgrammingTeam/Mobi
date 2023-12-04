package gr.aueb.dmst.onepercent.programming;

public class ExecutorThread extends SuperThread {
    @Override
    public void run() { 
        var containerManagerHttp = new ContainerManagerHttp();
        if (this.userInput == 1) {
            containerManagerHttp.startContainer();
        } else if (this.userInput == 2) {
            containerManagerHttp.stopContainer();
        }   
    }  
}

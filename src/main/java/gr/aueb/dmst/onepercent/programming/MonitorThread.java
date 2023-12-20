package gr.aueb.dmst.onepercent.programming;


/** Class: MonitorThread is a class that contains methods that retrieves information
 *  about the containers of the docker system, it monitors the docker system. 
 *  It is a subclass of SuperThread.
 *  @see SuperThread
 */
public class MonitorThread extends SuperThread {
    @Override
    public void run() { 
        var containerMonitorHttp = new MonitorHttp();
        if (this.userInput == 3) {
            containerMonitorHttp.searchImages();
        } else if (this.userInput == 5) {
            Graph.executeDiagram(); 
        } else if (this.userInput == 6) {
            containerMonitorHttp.inspectContainer();
        } else if (this.userInput == 7) {
            CSV csv = new CSV();
            csv.startSavingData();   
        } else if (this.userInput == 8) {
            SuperAPI.createDockerClient();
            MonitorAPI containerMonitor = new MonitorAPI();
            containerMonitor.initializeContainerModels();
            containerMonitor.getContainerList();
        }
    }
}

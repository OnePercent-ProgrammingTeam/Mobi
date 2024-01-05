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
            //for command line
            //containerMonitorHttp.searchImages();

            //for GUI
            containerMonitorHttp.searchImagesGUI();
            
        } else if (this.userInput == 5) {

            //for command line
            //Graph.executeDiagram();

            //for GUI 
            Graph.executeDiagramGUI();

        } else if (this.userInput == 6) {
            //for command line
            //containerMonitorHttp.inspectContainer();

            //for GUI
            containerMonitorHttp.inspectContainerGUI();

        } else if (this.userInput == 7) {
            //for command line
            CSV csv = new CSV();
            csv.startSavingData();   
        } else if (this.userInput == 8) {
            //only for command line
            SuperAPI.createDockerClient();
            MonitorAPI containerMonitor = new MonitorAPI();
            containerMonitor.initializeContainerModels();
            containerMonitor.getContainerList();
        }
    }
}

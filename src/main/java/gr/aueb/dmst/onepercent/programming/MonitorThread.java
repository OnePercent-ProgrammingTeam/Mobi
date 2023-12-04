package gr.aueb.dmst.onepercent.programming;

public class MonitorThread extends SuperThread {
    @Override
    public void run() { 
        var containerMonitorHttp = new ContainerMonitorHttp();
        if (this.userInput == 3) {
            containerMonitorHttp.inspectContainer();
        } else if (this.userInput == 4) {
            ContainerVisualization.executeDiagram();    
        } else if (this.userInput == 5 ) {
            containerMonitorHttp.searchImages(); 
        } else if (this.userInput == 6) {
            CSV csv = new CSV();
            csv.startSavingData();
        }
    }
}

package gr.aueb.dmst.onepercent.programming;

public class MonitorThreadCLI extends SuperThread {
    
    //Singleton
    private static MonitorThreadCLI monitorThreadCLI;

    private MonitorThreadCLI() { }

    public static MonitorThreadCLI getInstance() {
        if (monitorThreadCLI == null) {
            monitorThreadCLI = new MonitorThreadCLI();
        }
        return monitorThreadCLI;
    }
    
    @Override
    public void run() { 
        var containerMonitorHttp = new MonitorHttpCLI();
        switch (this.userInput) {
            case 3:
                containerMonitorHttp.searchImage();
                break;
            case 5:
                Graph.executeDiagram();
                break;
            case 6:
                containerMonitorHttp.inspectContainer();
                break;
            case 7:
                CSV csv = new CSV();
                csv.startSavingData();   
                break;
            case 8:
                //only for command line
                SuperAPI.createDockerClient();
                MonitorAPI containerMonitor = new MonitorAPI();
                containerMonitor.initializeContainerModels(true);
                containerMonitor.getContainerList();
                break;
        }
        
    }

}

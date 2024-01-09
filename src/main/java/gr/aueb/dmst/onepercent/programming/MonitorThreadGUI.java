package gr.aueb.dmst.onepercent.programming;

public class MonitorThreadGUI extends SuperThread {
    //Singleton
    private static MonitorThreadGUI monitorThreadGUI;

    private MonitorThreadGUI() { }

    public static MonitorThreadGUI getInstance() {
        if (monitorThreadGUI == null) {
            monitorThreadGUI = new MonitorThreadGUI();
        }
        return monitorThreadGUI;
    }
    
    @Override
    public void run() { 
        var containerMonitorHttp = new MonitorHttpGUI();
        switch (this.userInput) {
            case 3:
                containerMonitorHttp.searchImage();
                break;
            case 5:
                Graph graph = Graph.getInstance();
                graph.executeDiagramGUI();
                break;
            case 6:
                containerMonitorHttp.inspectContainer();
                break;
            case 7:
                //TO DO: make the functionality work for GUI 
                break;
        }
    }

}

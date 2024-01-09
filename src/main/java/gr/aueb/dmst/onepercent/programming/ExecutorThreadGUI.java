package gr.aueb.dmst.onepercent.programming;

public class ExecutorThreadGUI extends SuperThread {
    //Singleton
    private static ExecutorThreadGUI executorThreadGUI;

    private ExecutorThreadGUI() { }

    public static ExecutorThreadGUI getInstance() {
        if (executorThreadGUI == null) {
            executorThreadGUI = new ExecutorThreadGUI();
        }
        return executorThreadGUI;
    }

    @Override
    public void run() { 
        var containerManagerHttp = new ManagerHttpGUI();
        switch (this.userInput) {
            case 1:
                containerManagerHttp.startContainer();
                break;
            case 2:
                containerManagerHttp.stopContainer();
                break;
            case 4:
                //TO DO: containerManagerHttp.pullImageGUI();
                break;
        }
    }
}

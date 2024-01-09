package gr.aueb.dmst.onepercent.programming;

public class ExecutorThreadCLI extends SuperThread {
    //Singleton
    private static ExecutorThreadCLI executorThreadCLI;

    private ExecutorThreadCLI() { }

    public static ExecutorThreadCLI getInstance() {
        if (executorThreadCLI == null) {
            executorThreadCLI = new ExecutorThreadCLI();
        }
        return executorThreadCLI;
    }
    
    @Override
    public void run() { 
        ManagerHttpCLI containerManagerHttp = new ManagerHttpCLI();
        switch (this.userInput) {
            case 1:
                containerManagerHttp.startContainer();
                break;
            case 2:
                containerManagerHttp.stopContainer();
                break;
            case 4:
                containerManagerHttp.pullImage();
                break;
        }
    }
}

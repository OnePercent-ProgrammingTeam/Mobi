package gr.aueb.dmst.onepercent.programming;

public class ExecutorThread implements Runnable {
    private int userInput;

    protected void setUserInput(int userInput) {
        this.userInput = userInput;
    }

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
package gr.aueb.dmst.onepercent.programming;

public class MenuThreadGUI extends MenuThread {
 
    ExecutorThreadGUI executorThreadGUI = ExecutorThreadGUI.getInstance();
    MonitorThreadGUI monitorThreadGUI = MonitorThreadGUI.getInstance();

    @Override
    public void run() {
        database.createDatabase();
    }

    public void handleUserInputGUI(int answer) {
        switch (answer) {
            case 1:
            case 2:
            case 4:
                executorThreadGUI.setUserInput(answer);
                thread = new Thread(executorThreadGUI);
                //set name to the thread so as to be easier to recognize it. 
                thread.setName("Executor"); 
                thread.start();

                waitThread();

                /*start concurrently the database Thread*/
                dataThread.setUserInput(answer);
                thread = new Thread(dataThread);
                thread.setName("DataBase"); 
                thread.start();
                
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
                monitorThreadGUI.setUserInput(answer);
                thread = new Thread(monitorThreadGUI);
                thread.setName("Monitor");
                thread.start();

                waitThread();
                    
                /*start concurrently the database Thread*/
                dataThread.setUserInput(answer);
                thread = new Thread(dataThread);
                thread.setName("DataBase"); 
                thread.start();
                break;
            default:
                System.out.println("Non Valid Input.");
        }
       // if (answer == 5) {
       //     while (Graph.end == false) {
       //         waitThread();
       //     }
       // } else {
       //     waitThread();
       // }   
    }


}

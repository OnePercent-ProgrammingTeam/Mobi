package gr.aueb.dmst.onepercent.programming.data;


/** Class: DataBaseThread is a class that contains methods that 
 *  insert data in the tables so as to store the actions of the customer.
 *  @see Thread
 */
public class DataBaseThread extends Thread {

    //Sigleton
    private static DataBaseThread dataBaseThread;

    private DataBaseThread() { }

    /**
     * ok
     * @return ok
     */
    public static DataBaseThread getInstance() {
        if (dataBaseThread == null) {
            dataBaseThread = new DataBaseThread();
        }
        return dataBaseThread;
    }



    private String imName;
    /**
     * ok
     * @param imName ok
     */
    public void setImageName(String imName) {
        this.imName = imName;
    }

    private int command;
    /**
     * ok
     * @param command ok
     */
    public void setCommand(int command) {
        this.command = command;
    }

    private String state;
    /**
     * ok
     * @param state ok
     */
    public void setState(String state) {
        this.state = state;
    }

    private String means;
    /**
     * ok
     * @param means ok
     */
    public void setMeans(String means) {
        this.means = means;
    }



    @Override
    public void run() {
        DataBase dataBase = DataBase.getInstance();
        

        String datetime = dataBase.getDateTime();

        int last_id = dataBase.insertMetricsToDatabase(datetime, command, state, means);

        if (command == 1 || command == 2 || command == 5 || command == 6 || command == 9) {
            dataBase.insertContainerToDatabase(last_id);
            dataBase.getAllMetrics(); //NOT helpful
            //dataBase.getAllContainer(); //NOT helpful


        } else if (command == 3 || command == 4 || command == 10) {
            dataBase.insertImageToDatabase(last_id, imName);
            //dataBase.getAllMetrics(); //helpful
            //dataBase.getAllImage(); //helpful
            //dataBase.getImageForSearch();

        } 
    }
}

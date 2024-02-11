package gr.aueb.dmst.onepercent.programming.data;


/** Class: DataBaseThread is a class that contains methods that 
 *  insert data in the tables so as to store the actions of the customer.
 *  @see Thread
 */
public class DataBaseThread extends Thread {

    //Sigleton
    private static DataBaseThread dataBaseThread;

    private String interface_type;
    
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

    private int action;
    /**
     * ok
     * @param action ok
     */
    public void setAction(int action) {
        this.action = action;
    }
  
    private String state;
    /**
     * ok
     * @param state ok
     */
    public void setState(String state) {
        this.state = state;
    }


    /**
     * Sets the interface type.
     * @param interface_type The interface type can be CLI or GUI.
     */
    public void setInterfaceType(String interface_type) {
        this.interface_type = interface_type;
    }

    @Override
    public void run() {
        DataBase database = DataBase.getInstance();
        
        String datetime = database.getDateTime();

        int last_id; 
        // Insert in db tables data related to container (basically needed for the log/history)
        if (action == 1 || action == 2 || action == 3 || action == 4 || action == 5) {
            last_id = database.insertMetricsToDatabase(datetime, action, state, interface_type);
            database.insertContainerToDatabase(last_id);
            //dataBase.getAllMetrics(); //NOT helpful
            //dataBase.getAllContainer(); //NOT helpful
            
        // Insert in db tables data related to images (basically needed for the log/history)
        } else if (action == 8 || action == 9 || action == 10) {
            last_id = database.insertMetricsToDatabase(datetime, action, state, interface_type);
            database.insertImageToDatabase(last_id, imName);
            //dataBase.getAllMetrics(); //helpful
            //dataBase.getAllImage(); //helpful
            //dataBase.getImageForSearch();
        } else if (action == 13) {
            database.insertMetricsToDatabase(datetime, action, state, interface_type);
        }
    }
}

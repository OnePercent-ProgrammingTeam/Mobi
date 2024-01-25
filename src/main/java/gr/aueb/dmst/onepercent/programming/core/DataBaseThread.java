package gr.aueb.dmst.onepercent.programming.core;


/** Class: DataBaseThread is a class that contains methods that 
 *  insert data in the tables so as to store the actions of the customer.
 *  It is a subclass of SuperThread.
 *  @see SuperThread
 */
public class DataBaseThread extends SuperThread {

    private int answer;

    /**
     * Default Constructor
     */
    public DataBaseThread() {

    }

    /**
     * Default Constructor
     */
    public DataBaseThread(int answer) {
        this.answer = answer;
    }



    @Override
    public void run() {
        DataBase database = DataBase.getInstance();

        String datetime = database.getDateTime();
        int last_id = database.insertMetricsToDatabase(datetime, answer);
        System.out.println("inside run datathreaad with: " + answer);

        if (answer == 1 || answer == 2 || answer == 6) {
            database.insertContainerToDatabase(last_id);
          //  database.getAllMetrics(); //NOT helpful
          //  database.getAllContainer(); //NOT helpful


        } else if (answer == 3) {
            System.out.println("inside image database ");
            database.insertImageToDatabase(last_id);
            database.getAllMetrics(); //helpful
            database.getAllImage(); //helpful
            database.getImageForSearch();

        } 
    }
}

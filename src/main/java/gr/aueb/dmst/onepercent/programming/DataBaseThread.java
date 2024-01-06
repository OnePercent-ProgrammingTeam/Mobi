package gr.aueb.dmst.onepercent.programming;

/** Class: DataBaseThread is a class that contains methods that 
 *  insert data in the tables so as to store the actions of the customer.
 *  It is a subclass of SuperThread.
 *  @see SuperThread
 */

public class DataBaseThread extends SuperThread {
    @Override
    public void run() {
        DataBase database = new DataBase();
        String datetime;
        int last_id;

        if (this.userInput == 1 || this.userInput == 2 || this.userInput == 6) {
            datetime = database.getDateTime();
            last_id = database.insertMetricsToDatabase(datetime);
            database.insertContainerToDatabase(last_id);
            database.getAllMetrics(); //helpful
            database.getAllContainer(); //helpful


        } else if (this.userInput == 3) {
            datetime = database.getDateTime();
            last_id = database.insertMetricsToDatabase(datetime);
            database.insertImageToDatabase(last_id);
            database.getAllMetrics(); //helpful
            database.getAllImage(); //helpful
            database.getImageForSearch();

        } 
    }
    
}

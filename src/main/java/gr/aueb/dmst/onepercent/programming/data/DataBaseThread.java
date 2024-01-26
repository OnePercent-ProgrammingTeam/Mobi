package gr.aueb.dmst.onepercent.programming.data;


/** Class: DataBaseThread is a class that contains methods that 
 *  insert data in the tables so as to store the actions of the customer.
 *  @see Thread
 */
public class DataBaseThread extends Thread {

    private int answer;

    /* 
    public DataBaseThread() { }
    public DataBaseThread(int answer) {
        this.answer = answer;
    }
    */


    //Sigleton
    private static DataBaseThread dataBaseThread;

    private DataBaseThread() { }

    public static DataBaseThread getInstance() {
        if (dataBaseThread == null) {
            dataBaseThread = new DataBaseThread();
        }
        return dataBaseThread;
    }


    public void setAnswer(int answer) {
        this.answer = answer;
    }



    @Override
    public void run() {
        DataBase dataBase = DataBase.getInstance();

        String datetime = dataBase.getDateTime();
        int last_id = dataBase.insertMetricsToDatabase(datetime, answer);
        //System.out.println("inside run datathreaad with: " + answer);

        if (answer == 1 || answer == 2 || answer == 6) {
            dataBase.insertContainerToDatabase(last_id);
            //retrieveData.getAllMetrics(); //NOT helpful
            //retrieveData.getAllContainer(); //NOT helpful


        } else if (answer == 3) {
            System.out.println("inside image database ");
            dataBase.insertImageToDatabase(last_id);
            dataBase.getAllMetrics(); //helpful
            dataBase.getAllImage(); //helpful
            dataBase.getImageForSearch();

        } 
    }
}

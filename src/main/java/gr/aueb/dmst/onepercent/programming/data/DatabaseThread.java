package gr.aueb.dmst.onepercent.programming.data;

/**
 * A thread for handling database operations in the background.
 * 
 * <p>This class is designed following the Singleton pattern.
 * It is used in the CLI Version of the application. To be implemented in the GUI Version.
 */
public class DatabaseThread extends Thread {
    //TO DO(Dionisia Koronellou): Implement the db to GUI version of the application.

    /** Singleton instance of the DataBaseThread class. */
    private static DatabaseThread dataBaseThread;

    /** The interface type (CLI or GUI) for database operations. */
    private String interface_type;

    /** The name of the image. */
    private String image_name;

    /** The action to be performed. */
    private int action;

    /** The state of the database operation. */
    private String state;

    /** Private constructor to prevent direct instantiation. */
    private DatabaseThread() { }

    /**
     * Retrieves the singleton instance of the DataBaseThread class.
     * @return The singleton instance of DataBaseThread.
     */
    public static DatabaseThread getInstance() {
        if (dataBaseThread == null) {
            dataBaseThread = new DatabaseThread();
        }
        return dataBaseThread;
    }

    /**
     * Sets the name of the image.
     * @param name The name of the image.
     */
    public void setImageName(String name) {
        this.image_name = name;
    }

    /**
     * Sets the action to be performed.
     * @param action The action to be performed.
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * Sets the state of the database operation.
     * @param state The state of the database operation.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Sets the interface type for database operations (CLI or GUI).
     * @param interface_type The interface type (CLI or GUI).
     */
    public void setInterfaceType(String interface_type) {
        this.interface_type = interface_type;
    }

    @Override
    public void run() {
        Database database = Database.getInstance();
        String datetime = database.getDateTime();
        int last_id; 

        // Insert data related to container into database
        if (action >= 1 && action <= 5) {
            last_id = database.insertIntoMetrics(datetime, action, state, interface_type);
            database.insertIntoContainer(last_id);
        }
        // Insert data related to images into database
        else if (action >= 8 && action <= 10) {
            last_id = database.insertIntoMetrics(datetime, action, state, interface_type);
            database.insertIntoImage(last_id, image_name);
        }
        // Insert other metrics into database
        else if (action == 13) {
            database.insertIntoMetrics(datetime, action, state, interface_type);
        }
    }
}

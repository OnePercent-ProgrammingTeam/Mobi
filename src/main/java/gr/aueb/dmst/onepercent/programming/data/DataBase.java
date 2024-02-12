package gr.aueb.dmst.onepercent.programming.data;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.GREEN;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

import gr.aueb.dmst.onepercent.programming.cli.MonitorCLI;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.google.common.annotations.VisibleForTesting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The DataBase of the application, containing metrics for all each user.
 * 
 * <p>It provides methods for database connectivity, table creation, data insertion, and history
 * retrieval.
 */
public class Database {
    /** The url for database connectivity. */
    protected String url = "jdbc:h2:./databases/metricsbase";
    
    /** The query sent to the database. */
    protected String query;

    /** A monitor instance from CLI Version. */
    MonitorCLI monitor = new MonitorCLI();

    /** Singleton instance of database. */
    private static Database database;

    /** A list with the dates of any action happened from the user in Mobi. */
    private ArrayList<String> date = new ArrayList<>();
    
    /** A list with the options-actions that the user has selected to execute. */
    private ArrayList<Integer> actions = new ArrayList<>();
    
    /** A list with the result-state of the action that the user has chosen (success/failure). */
    private ArrayList<String> state = new ArrayList<>();

    /** A list with the interface type in which the user has done an action (CLI/GUI). */
    private ArrayList<String> interface_type = new ArrayList<>();

    /** A list with the object for which the task was executed (Container/Image/System). */
    private ArrayList<String> object = new ArrayList<>();

    /** A list with the identifier of the object for which the task was executed (ID/image). */
    private ArrayList<String> identifier = new ArrayList<>();

    /** Private Constructor. */
    private Database() { }

     /**
     * Gets the singleton database instance. 
     * There could only be one database.
     * @return The database instance.
     */
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    /**
     * Getter for the URL, used for connecting to the H2 database.
     * 
     * <p>This method is marked with {@code @VisibleForTesting} to make it accessible
     * for testing purposes within the same package.
     *
     * @return The URL for connecting to the H2 database.
     */
    @VisibleForTesting
    public String getUrl() {
        return url;
    }
    
    /**
     * Set the name of the database, based on user's name.
     * @param username The name of the user.
     */
    public void setDatabaseName(String username) {
        url = url + "/" //Make a random name.
            + username.charAt(0) 
            + username.charAt(username.length() / 2) 
            + username.charAt(username.length() - 1); 
    }

    /**
     * Creates the tables of the database.
     * 
     * <p>Tables created are: 
     * 1) Metrics (ID, Date, Action, State, Interface)
     * 2) Container(ID,C_ID, C_NAME, Status, Image_ID, Network_ID, Gateway, IP_Address, Mac_Address)
     * 3) Image (ID,NAME)
     */
    public void createTables() {
        try {
            Class.forName("org.h2.Driver"); //Register JDBC driver. 
            Connection connection = DriverManager.getConnection(url); //Open a connection.
            //Use the connection to create a statement.
            Statement statement = connection.createStatement();
            //Query that creates the "Metrics" table
            query = "CREATE TABLE IF NOT EXISTS Metrics ("
                + "ID INT AUTO_INCREMENT PRIMARY KEY,"
                + "Date DATETIME,"
                + "Action INT,"
                + "State VARCHAR(10),"
                + "Interface VARCHAR(10)"
                + ");";
            statement.execute(query);
            //Query that creates the "Container" table
            query = "CREATE TABLE IF NOT EXISTS Container ("
                + "ID INT,"
                + "C_ID VARCHAR(100),"
                + "C_NAME VARCHAR(100),"
                + "Status VARCHAR(100),"
                + "Image_ID VARCHAR(100),"
                + "Network_ID VARCHAR(100),"
                + "Gateway VARCHAR(100),"
                + "IP_Address VARCHAR(100),"
                + "Mac_Address VARCHAR(100),"
                + "CONSTRAINT c1 PRIMARY KEY(ID,C_ID),"
                + "FOREIGN KEY (ID) REFERENCES Metrics(ID)"
                + ");";
            statement.execute(query);
            //Query that creates the "Image" table
            query = "CREATE TABLE IF NOT EXISTS Image ("
                + "ID INT,"
                + "NAME VARCHAR(100),"
                + "CONSTRAINT c2 PRIMARY KEY(ID,NAME),"
                + "FOREIGN KEY (ID) REFERENCES Metrics(ID)"
                + ");";
            statement.execute(query);
        } catch (ClassNotFoundException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        } catch (SQLException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
    }

    /**
     * Retrieve current local date and time.
     * @return The current timestamp.
     */
    public String getDateTime() {
        LocalDate date = LocalDate.now(); 
        LocalTime time = LocalTime.now();
        String datetime = date.toString() + " " + time.toString().substring(0, 8);
        return datetime;
    }

    /**
     * Insert data into 'Metrics' table.
     * @param datetime The datetime to be inserted.
     * @param action The action the user has chosen.
     * @param state The result/status of the action.
     * @param interface_type The interface in which 
     * @return The ID of the last inserted record.
     */
    public int insertIntoMetrics(String datetime, 
                                       int action, 
                                       String state, 
                                       String interface_type) {
        int last_id = 0;
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();    
            query = "INSERT INTO Metrics (Date, Action, State, Interface)" 
                     + "VALUES ('" + datetime + "','" + action 
                     + "','" + state + "','" + interface_type + "');";
            statement.execute(query); 
            query = "SELECT MAX(ID) AS LAST_ID FROM Metrics";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                last_id = result.getInt("LAST_ID");
            }           
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
        return last_id;
    }

    /**
     * Insert data into 'Container' table.
     *
     * @param last_id The last ID, inserted in the 'Metrics' table.
     */
    public void insertIntoContainer(int last_id) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();            
            String[] info = monitor.retrieveContainerInfoArray();
            query = "INSERT INTO Container (ID, C_ID, C_NAME, Status,"
                + "Image_ID, Network_ID, Gateway, IP_Address, Mac_Address)"
                + "VALUES ('" + last_id + "','" + info[0] + "','" + info[1] + "','" + info[2] 
                + "','" + info[3] + "','" + info[4] + "','" + info[5] + "','" + info[6]
                + "','" + info[7] + "');";
            statement.execute(query);
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException | JsonProcessingException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
    }

    /**
     * Insert data into 'Image' table.
     * @param last_id The last inserted ID in the "Metrics" table.
     * @param name The name of the image.
     */
    public void insertIntoImage(int last_id, String name) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();

            query = "INSERT INTO Image (ID, NAME) VALUES ('" + last_id + "','" + name + "');";
            statement.execute(query); 

            connection.close();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
    }

    /**
     * Provides suggestions to the user for searching images, based on his previous searches.
     *
     * @return An ArrayList containing names of images.
     */
    public ArrayList<String> getSearchSuggestions() {
        ArrayList<String> names = new ArrayList<String>();
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
              
            query = "SELECT DISTINCT NAME "
                    + "FROM (SELECT NAME, ID "
                    +        "FROM Image "
                    +        "ORDER BY ID DESC "
                    +        "LIMIT 5)";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String imname = result.getString("NAME");
                names.add(imname);
            }
            statement.close(); 
            connection.close();        
        } catch (ClassNotFoundException | SQLException e) { 
            System.out.println(RED + "A database error has occured." + RESET);
        } 
        return names;
    }

    /**
     * Creates the history for the user, containing information about the action he has done 
     */
    public void createHistory() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
            query = "SELECT Metrics.Date, Metrics.Action, Metrics.State, Metrics.Interface, "
                    + "COALESCE(Image.NAME, Container.C_NAME) AS Identifier "
                    + "FROM Metrics "
                    + "LEFT JOIN Image ON Metrics.ID = Image.ID "
                    + "LEFT JOIN Container ON Metrics.ID = Container.ID "
                    + "ORDER BY Metrics.Date DESC;";
            ResultSet result = statement.executeQuery(query);
            String date_time;
            int action;
            String interfaceType;
            String status;
            String id;
            while (result.next()) {
                /* 
                 * Customization of the history table.
                 * Make sure that no null values are inserted in history, where
                 * there is the possibility of a null value. Instead, insert "N/A".
                 */
                date_time = result.getString("Date");
                action = result.getInt("Action");
                interfaceType = result.getString("Interface");
                if (result.getString("State").isEmpty()) {
                    status = "N/A";
                } else {
                    status = (result.getString("State").equals("success")) ? 
                        GREEN + "success" + RESET : RED + "failure" + RESET;
                }
                id = result.getString("Identifier").isEmpty() ? 
                        "N/A" : result.getString("Identifier");
                date.add(date_time);
                actions.add(action);
                state.add(status);
                interface_type.add(interfaceType);
                identifier.add(id);
            }
            statement.close(); 
            connection.close(); 
        } catch (ClassNotFoundException | SQLException e) { 
            System.out.println(RED + "A database error has occured." + RESET);
        } 
    }

    /**
     * Convert the user's options to text.
     * @param act An array list containing the actions that the user has executed.
     * @return An array list with the customizes action names.
     */
    private ArrayList<String> convertActiontoString(ArrayList<Integer> act) {
        ArrayList<String> acts = new ArrayList<>();
        for (int i = 0; i < act.size(); i++) {
            switch (act.get(i)) {
                case 1: 
                    acts.add("start");
                    object.add("Container");
                    break;
                case 2: 
                    acts.add("stop");
                    object.add("Container");
                    break;
                case 3: 
                    acts.add("remove");
                    object.add("Container");
                    break;
                case 4: 
                    acts.add("inspect");
                    object.add("Container");
                    break;
                case 5: 
                    acts.add("plot");
                    object.add("Container");
                    break;
                case 8: 
                    acts.add("pull");
                    object.add("Image");
                    break;
                case 9: 
                    acts.add("remove");
                    object.add("Image");
                    break;
                case 10: 
                    acts.add("search");
                    object.add("Image");
                    break;
                case 13:
                    acts.add("info");
                    object.add("System");
                default:
                    break;
            }
        }
        return acts;
    }

    /**
     * Prints history to the console.
     */
    public void printHistory() {
        String id = "";
        ArrayList<String> acts;
        createHistory();
        if (date.isEmpty()) {
            System.out.println("Your history is empty");
        } else {
            System.out.printf("%-30s%-20s%-20s%-50s%-20s%-10s%n", 
                            "Date", 
                            "Action", 
                            "Object", 
                            "Identifier",
                            "State", 
                            "Interface");
            System.out.println(" ");
            acts = convertActiontoString(actions);
            for (int i = 0; i < date.size(); i++) {
                if (identifier.get(i).isEmpty()) { 
                    id = "N/A";
                } else {
                    id = (identifier.get(i).toString().length() > 35) ? 
                    this.identifier.get(i).substring(0, 35).concat("...") : this.identifier.get(i);
                }
                System.out.printf("%-30s%-20s%-20s%-50s%-20s%-10s%n",
                                    date.get(i), 
                                    acts.get(i),
                                    object.get(i),
                                    id,
                                    state.get(i),
                                    interface_type.get(i));
            }
        }
    } 
}

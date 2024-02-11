package gr.aueb.dmst.onepercent.programming.data;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.GREEN;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;

import gr.aueb.dmst.onepercent.programming.cli.MonitorHttpCLI;

/**
 * ok
 */
public class DataBase {
    /** ok */
    protected String url = "jdbc:h2:./databases/metricsbase";

    /**
     * Gets the URL used for connecting to the H2 database.
     * This method is marked with {@code @VisibleForTesting} to make it accessible
     * for testing purposes within the same package.
     *
     * @return The URL for connecting to the H2 database.
     */
    @VisibleForTesting
    public String getUrl() {
        return url;
    }
     /** ok */
    protected String query;

    /**
     * Set the name of the database of metrics to be created for safety reasons
     * @param username ok
     */
    public void setURL(String username) {
        url = url + "/" 
            + username.charAt(0) 
            + username.charAt(username.length() / 2) 
            + username.charAt(username.length() - 1); 
    }

    //Sigleton
    private static DataBase database;

     
    private DataBase() { }

    /**
     * ok
     * @return ok
     */
    public static DataBase getInstance() {
        if (database == null) {
            database = new DataBase();
        }
        return database;
    }

    /**
     * Method: createDatabase() creates tables in the H2 database.
     * Creation of 4 tables: 
     * 1) Metrics (ID, Date, Action, State, Interface)
     * 2) Container(ID,C_ID, C_NAME, Status, Image_ID, Network_ID, Gateway, IP_Address, Mac_Address)
     * 3) Image (ID,NAME)
     */
    public void createDatabaseMetrics() {
        try {
            Class.forName("org.h2.Driver"); //Register JDBC driver 
            Connection connection = DriverManager.getConnection(url); //Open a connection
            Statement statement = connection.createStatement(); 
            //Use the connection to create a statement


            //Query that creates the "Metrics" table
            query = "CREATE TABLE IF NOT EXISTS Metrics ("
                + "ID INT AUTO_INCREMENT PRIMARY KEY,"
                + "Date DATETIME,"
                + "Action INT,"
                + "State VARCHAR(10),"
                + "Interface VARCHAR(10)"
                + ");";

            //execute the query 
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
            
            //execute the query 
            statement.execute(query);

            //Query that creates the "Image" table
            query = "CREATE TABLE IF NOT EXISTS Image ("
                + "ID INT,"
                + "NAME VARCHAR(100),"
                + "CONSTRAINT c2 PRIMARY KEY(ID,NAME),"
                + "FOREIGN KEY (ID) REFERENCES Metrics(ID)"
                + ");";

            //execute the query 
            statement.execute(query);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    MonitorHttpCLI monitor = new MonitorHttpCLI();
    /**
     * Method returns as a String the local date time
     * @return The datetime
     */
    public String getDateTime() {
        LocalDate date = LocalDate.now(); 
        LocalTime time = LocalTime.now();

        //Date and Time in one
        String datetime = date.toString() + " " + time.toString().substring(0, 8);
        return datetime;
    }

    /**
     * Insert data into "Metrics"
     * @param datetime The datetime to be inserted.
     * @param action ok
     * @param state ok
     * @param interface_type ok
     * @return The ID of the last inserted record.
     */
    public int insertMetricsToDatabase(String datetime, 
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
            e.printStackTrace();
        }
        return last_id;
    }

    /**
     * Insert data into "Container"
     *
     * @param last_id The last inserted ID in the "Metrics" table.
     */
    public void insertContainerToDatabase(int last_id) {
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
            e.printStackTrace();
        }
    }

    /**
     * Insert data into "Image"
     * @param last_id The last inserted ID in the "Metrics" table.
     * @param name ok
     */
    public void insertImageToDatabase(int last_id, String name) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();

            query = "INSERT INTO Image (ID, NAME) VALUES ('" + last_id + "','" + name + "');";
            statement.execute(query); 

            connection.close();
            statement.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Method for showing all metrics
     */
    public void getAllMetrics() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
              
            query = "SELECT * FROM Metrics";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("ID");
                String datetime = result.getString("Date");
                int action = result.getInt("Action");
                String state = result.getString("State");
                String interface_type = result.getString("Interface");
                
                System.out.println();
                System.out.println("ID of metrics: " + id);
                System.out.println("Date of metrics: " + datetime);
                System.out.println("Action: " + action);
                System.out.println("State: " + state);
                System.out.println("Interface : " + interface_type);
                System.out.println();
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
    }

    /**
     * Method for showing the elements 
     * of all the containers
     */
    public void getAllContainer() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
              
            query = "SELECT * FROM Container";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("ID");
                String cid = result.getString("C_ID");
                String cname = result.getString("C_NAME");
                String status = result.getString("Status");
                String imid = result.getString("Image_ID");
                String netid = result.getString("Network_ID");
                String gate = result.getString("Gateway");
                String ip = result.getString("IP_Address");
                String mac = result.getString("Mac_Address");

                
                System.out.println("ID of metrics: " + id);
                System.out.println("C_ID: " + cid);
                System.out.println("C_NAME: " + cname);
                System.out.println("Status: " + status);
                System.out.println("Image_ID" + imid);
                System.out.println("Network_ID: " + netid);
                System.out.println("Gateway: " + gate);
                System.out.println("IP_Address: " + ip);
                System.out.println("Mac_Address: " + mac);
                System.out.println();
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
    }


    /**
     * Method for showing the id and name of images
     */
    public void getAllImage() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
              
            query = "SELECT * FROM Image";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("ID");
                String imname = result.getString("NAME");

                
                System.out.println("ID of metrics: " + id);
                System.out.println("Name of Image: " + imname);
                System.out.println();
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
    }


    /**
     * Method for checking for some "Metrics"
     *
     * @param last_id The last inserted ID in the "Metrics" table.
     */
    public void getSomeMetrics(int last_id) {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
              
            query = "SELECT * FROM Metrics WHERE ID=" + String.valueOf(last_id);
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int id = result.getInt("ID");
                String datetime = result.getString("Date");

                
                System.out.println("ID of metrics: " + id);
                System.out.println("Date of metrics: " + datetime);
                System.out.println();
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
    }

    /**
     * Method for checking for "Image"
     *
     * @return An ArrayList containing names of images.
     */
    public ArrayList<String> getImageForSearch() {
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
                System.out.println("GUI");
                System.out.println("Name of Image: " + imname);
                System.out.println();
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
        return names;
    }

    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<Integer> actions = new ArrayList<>();
    private ArrayList<String> state = new ArrayList<>();
    private ArrayList<String> interface_type = new ArrayList<>();
    private ArrayList<String> identifier = new ArrayList<>();
    private ArrayList<String> object = new ArrayList<>();

    /**
     * ok
     */
    public void getHistory() {
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
            e.printStackTrace();
        } 
    }

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
     * ok
     */
    public void getHistoryList() {
        String id = "";
        ArrayList<String> acts;
        getHistory();
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

package gr.aueb.dmst.onepercent.programming.data;

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
import gr.aueb.dmst.onepercent.programming.core.MonitorHttp;


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
     * 1) Metrics (ID, Date, Command, State, Means)
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
                + "Command INT,"
                + "State VARCHAR(10),"
                + "Means VARCHAR(10)"
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

    MonitorHttp monitorHttp = new MonitorHttpCLI();
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
     * @param command ok
     * @param state ok
     * @param means ok
     * @return The ID of the last inserted record.
     */
    public int insertMetricsToDatabase(String datetime, int command, String state, String means) {
        int last_id = 0;

        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            
             
            query = "INSERT INTO Metrics (Date, Command, State, Means)" 
                     + "VALUES ('" + datetime + "','" + command 
                     + "','" + state + "','" + means + "');";



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

            
            String[] info = monitorHttp.getTableforContainer();

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
                int command = result.getInt("Command");
                String state = result.getString("State");
                String means = result.getString("Means");
                
                System.out.println();
                System.out.println("ID of metrics: " + id);
                System.out.println("Date of metrics: " + datetime);
                System.out.println("Command: " + command);
                System.out.println("State: " + state);
                System.out.println("Means : " + means);
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
    private ArrayList<Integer> com = new ArrayList<>();
    private ArrayList<String> state = new ArrayList<>();
    private ArrayList<String> means = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> object = new ArrayList<>();

    /**
     * ok
     */
    public void getHistory() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(url); 
            Statement statement = connection.createStatement(); 
              
            query = "SELECT Metrics.Date, Metrics.Command, Metrics.State, Metrics.Means, "
                    + "COALESCE(Image.NAME, Container.C_NAME) AS Name "
                    + "FROM Metrics "
                    + "LEFT JOIN Image ON Metrics.ID = Image.ID "
                    + "LEFT JOIN Container ON Metrics.ID = Container.ID "
                    + "ORDER BY Metrics.Date DESC;";

                        
                    
            ResultSet result = statement.executeQuery(query);
    
            while (result.next()) {
                String d = result.getString("Date");
                int c = result.getInt("Command");
                String s = result.getString("State");
                String m = result.getString("Means");
                String n = result.getString("Name");


                date.add(d);
                com.add(c);
                state.add(s);
                means.add(m);
                name.add(n);
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
    }


    private ArrayList<String> convertCommandtoString(ArrayList<Integer> com) {
        ArrayList<String> commands = new ArrayList<>();
        for (int i = 0; i < com.size(); i++) {
            switch (com.get(i)) {
                case 1: 
                    commands.add("start");
                    object.add("Container");
                    break;
                case 2: 
                    commands.add("stop");
                    object.add("Container");
                    break;
                case 5: 
                    commands.add("get plot");
                    object.add("Container");
                    break;
                case 6: 
                    commands.add("get info");
                    object.add("Container");
                    break;
                case 9: 
                    commands.add("remove");
                    object.add("Container");
                    break;
                case 3: 
                    commands.add("search");
                    object.add("Image");
                    break;
                case 4: 
                    commands.add("pull");
                    object.add("Image");
                    break;
                case 10: 
                    commands.add("remove");
                    object.add("Image");
                    break;
                default:
                    break;
            }
        }
        return commands;
    }



    /**
     * ok
     */
    public void getHistoryList() {
        getHistory();
        if (date.isEmpty()) {
            System.out.println("Your history is empty");
        } else {
            System.out.printf("%-30s%-20s%-20s%-50s%-20s%-10s%n", 
                            "Date", 
                            "Action", 
                            "Object", 
                            "Name",
                            "State", 
                            "Means");
            System.out.println(" ");

            ArrayList<String> command = convertCommandtoString(com);
            for (int i = 0; i < date.size(); i++) {
                System.out.printf("%-30s%-20s%-20s%-50s%-20s%-10s%n",
                                    date.get(i), 
                                    command.get(i),
                                    object.get(i),
                                    name.get(i),
                                    state.get(i),
                                    means.get(i));
            }
        }
    } 

}

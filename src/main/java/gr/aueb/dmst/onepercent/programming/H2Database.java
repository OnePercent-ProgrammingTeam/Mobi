package gr.aueb.dmst.onepercent.programming;

import java.sql.*;

public class H2Database {

    static MonitorHttp containerMonitorHttp = new MonitorHttp();
    static String url = "jdbc:h2:~"; // after the prefix jdbc:h2: put the location where you want the database file to be stored at , currently using the project folder , WIP for multiple databases at once
    static String user = "username"; //select username 
    static  String password = "password"; //select password
    static  String query; //create the query string


    public void inserMetricsToDatabase() {//Use this method to store the data of the container you want in the database, currently manual, should be automated in the future
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            conn = DriverManager.getConnection(url, user, password); // Open a connection

            MonitorHttp monitorHttp = new MonitorHttp();
            monitorHttp.getContainerStats("CSV");
            String[] info = monitorHttp.prepareStoragedData();

            double cpuUsage = Double.parseDouble(info[4]);
            String containerId = info[1];
            String dateTime = info[5];
            String query = "INSERT INTO metrics (cpu_usage, datetime, contid) VALUES (?, ?, ?)";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, cpuUsage);
            pstmt.setString(2, dateTime);
            pstmt.setString(3, containerId);

            pstmt.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Error handling
        } finally {
            try {
                // Close resources in finally block to ensure they are closed
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Error handling for closing resources
            }
        }
    }

    

    public void getAllMetrics() { //Prints all the metrics stored in the database, a new method for the metrics of a specific container  is WIP
        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
              
           query = "Select * from metrics";
           ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String id = rs.getString("id");
                String cpu = rs.getString("cpu_usage");
                String datetime = rs.getString("datetime");
                String contid = rs.getString("contid");

                // Process retrieved values (e.g., print or store them)
                System.out.println( "Container ID: " + contid +", CPU USAGE: " + cpu + ", Timestamp: " + datetime + ", Metric ID: " +id);
            }

            stmt.close();//close statement
            conn.close();//close connection
           
        } catch (ClassNotFoundException e) {//error handling
            e.printStackTrace();
        } catch (SQLException e) {//error handling
             e.printStackTrace();
 
        }
    }


    public void getAllContainerInfo() { //Gives all the information of every container in the database
        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
              
           query = "Select * from containers";
           ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String id = rs.getString("id");
                String imageid = rs.getString("imageid");
                String name = rs.getString("name");
                String created = rs.getString("Time_Created");

                // Process retrieved values (e.g., print or store them)
                System.out.println( "Container Name: " + name +", Container ID: " + id + ", Container Imageid: " + imageid + ", Container Created At: " + created);
            }

            stmt.close();//close statement
            conn.close();//close connection
           
        } catch (ClassNotFoundException e) {//error handling
            e.printStackTrace();
        } catch (SQLException e) {//error handling
             e.printStackTrace();
 
        }
    }


    public void insertContainersToDatabase() {//Method that inserts the information of containers to the cointainers table, has to run at the launch and every time a new container is created
         try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
              
         
        SuperAPI.createDockerClient();
        MonitorAPI containerMonitor = new MonitorAPI();
        containerMonitor.initializeContainerModels();
        String[][] containerInfo = containerMonitor.getContainerInfo();
        String id;
        String name;
        String image;
        String created;

        for (int i = 0; i < containerInfo.length; i++) {
            id = containerInfo[i][0];
            name = containerInfo[i][1];
            image = containerInfo[i][2];
            created = containerInfo[i][3];
 
            query = "INSERT INTO CONTAINERS(id, imageid, name, Time_Created) VALUES ('" + id + "', '" + image + "', '" + name + "', '" + created + "')";//query that inserts container info to the database table containers

             stmt.execute(query);
        }
           
            stmt.close();//close statement
            conn.close();//close connection
           
        } catch (ClassNotFoundException e) {//error handling
            e.printStackTrace();
        } catch (SQLException e) {//error handling
             System.out.println(" ");
 
        }
    }



    public void createDatabase() { //Creates the database, has to run one time on the launch for the programm to work
        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
              
        
             query = "CREATE TABLE IF NOT EXISTS containers ("//query that creates the containers table
            + "id VARCHAR(100) PRIMARY KEY, "
            + "imageid VARCHAR(100), "
            + "name VARCHAR(100),"
            + "Time_Created datetime"
            + ");";
           
            stmt.execute(query);//excecute the query
            
            query = "CREATE TABLE IF NOT EXISTS metrics ("//query that creates the metrics table
        + "id INT AUTO_INCREMENT PRIMARY KEY, "
        + "cpu_usage DECIMAL(12,10), "
        + "datetime DATETIME, "
        + "contid VARCHAR(100), "
        + "CONSTRAINT fk_contid FOREIGN KEY (contid) REFERENCES containers(id)"
        + ");";

        stmt.execute(query);//excecute the query

           
            stmt.close();//close statement
            conn.close();//close connection
           
        } catch (ClassNotFoundException e) {//error handling
            e.printStackTrace();
        } catch (SQLException e) {//error handling
            e.printStackTrace();
 
        }
    }

}




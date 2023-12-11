package gr.aueb.dmst.onepercent.programming;

import java.sql.*;

public class H2Database {

    static MonitorHttp containerMonitorHttp = new MonitorHttp();
    static String url = "jdbc:h2:~"; // after the prefix jdbc:h2: put the location where you want the database file to be stored at , currently using the project folder , WIP for multiple databases at once
    static String user = "username"; //select username 
    static  String password = "password"; //select password
    static  String query; //create the query string

    public void insertDataToDatabase() { //UNDER DEVELOPMENT
        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
           
  
        SuperAPI.createDockerClient();
        MonitorAPI containerMonitor = new MonitorAPI();
        containerMonitor.initializeContainerModels();
        String[][] containerInfo = containerMonitor.getContainerInfo();

        for (int i = 0; i < containerInfo.length; i++) {
            for (int j = 0; j < containerInfo[i].length; j++) {
                System.out.print(containerInfo[i][j] + " ");
            }
            System.out.println(); // Print a newline after each row
        }
           
        } catch (ClassNotFoundException e) {//error handling
            e.printStackTrace();
        } catch (SQLException e) {//error handling
            e.printStackTrace();
            System.out.println(" ");
            System.out.println("No container with such ID exists ");

        }
    }



    public void getDataFromDatabase(String name) { //takes the name of the container for input, returns its stored cpu usage and datetime
       //UNDER DEVELOPMENT
    }




    public void createDatabase() { //Creates the database, has to run one time on install/launch for the programm to work
        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
              
             query = "CREATE TABLE IF NOT EXISTS containers ("
            + "id VARCHAR(100) PRIMARY KEY, "
            + "imageid VARCHAR(100), "
            + "name VARCHAR(100),"
            + "Time_Created datetime"
            + ");";
           
            stmt.execute(query);//excecute the query
            
            query = "CREATE TABLE IF NOT EXISTS metrics ("
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





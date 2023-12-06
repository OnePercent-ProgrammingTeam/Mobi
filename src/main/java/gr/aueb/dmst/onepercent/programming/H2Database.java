package gr.aueb.dmst.onepercent.programming;

import java.sql.*;

            //how queries work in h2database:
            //1) you create a statement using the connection to the database
            //2) you create an sql query as a string
            //3) you use the statement to execute the query

public class H2Database {

     static ContainerMonitorHttp containerMonitorHttp = new ContainerMonitorHttp();

    public static void main(String[] args) { //the method will take for input
        // JDBC URL, username, and password
        String url = "jdbc:h2:~"; // after the prefix jdbc:h2: put the location where you want the database file to be stored at , currently using the project folder , WIP for multiple databases at once
        String user = "username";//select username 
        String password = "password"; //select password
        try {
            Class.forName("org.h2.Driver"); // Register JDBC driver
            Connection conn = DriverManager.getConnection(url, user, password); // Open a connection
            Statement stmt = conn.createStatement();// Use the connection for database operations
            String query;//create the query string
            
            containerMonitorHttp.getContainerStats("CSV");
            String[] info = containerMonitorHttp.prepareStoragedData();

            String name = info[0];
            String cpu = info[4];
            String datetime = info[5];
      
            query = "CREATE TABLE IF NOT EXISTS " + name + "("
            + "cpu_usage DECIMAL(10,8),"
            + "date_time TIMESTAMP"
            + ");";

            stmt.execute(query);

            query = "Insert into " + name +" (cpu_usage,date_time) "
            + "values "
            //+ "(12.2223,'2023-12-05 19:39:40.4');";  Example values, used for testing
           + "(" + cpu + ", " + "'" +datetime +"');"; //for this to work I changed the double space in line 204 of containermonitorhttp to a single space
                
            stmt.execute(query);



            query = "SELECT * FROM " + name;
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                double cpuUsage = resultSet.getDouble("cpu_usage");
                Timestamp dateTime = resultSet.getTimestamp("date_time");

                System.out.printf("CPU Usage: %.8f , Date & Time: %s\n", cpuUsage, dateTime);
               
            }

        


            
            stmt.close();//close statement
            conn.close();//close connection
           
        } catch (ClassNotFoundException e) {//error handling
            e.printStackTrace();
        } catch (SQLException e) {//error handling
            e.printStackTrace();
        }
    }

}

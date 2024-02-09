package gr.aueb.dmst.onepercent.programming.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;


/**
 * Class: DataUser is responsible for interacting with the embedded form of H2 database.
 * It has methods for storing data about users that use our app.
 * As a result it helps in creating tables, inserting data, and querying information.
 */
public class DataUsers {
    static final String urlgeneral = "jdbc:h2:./databases/user";

    String query;

    /**
     * Default constructor
     */
    public DataUsers() {
    }


    /**
     * Method: createUser() creates a table in the H2 database.
     * The table is named "Users" and contains the names and the passwords 
     * of the users that are signed up in our application.
     */
    public void createUser() {
        try {
            Class.forName("org.h2.Driver"); //Register JDBC driver 
            Connection connection = DriverManager.getConnection(
                                        urlgeneral); //Open a connection
            Statement statement = connection.createStatement(); 
            //Use the connection to create a statement
            //Query that creates the "Users" table
            query = "CREATE TABLE IF NOT EXISTS Users ("
                + "NAME VARCHAR(50),"
                + "PASSWORD VARCHAR(50),"
                + "IMAGE INT,"
                + "REMEMBER BOOLEAN,"
                + "CONSTRAINT c PRIMARY KEY(NAME,PASSWORD) "
                + ");";

            //execute the query 
            statement.execute(query);

            statement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Insert data into "Users" if the user does not already exist
     *
     * @param username The name of the user that is provided through the Sign up.
     * @param password The password of the user that is provided through the Sign up.
     * @param remember ok
     */
    public void insertUsers(String username, String password, Boolean remember) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(urlgeneral);

            Random random = new Random();
            int number = random.nextInt(17) + 1; //from 1 to 17

            // Use a prepared statement to insert data into the "Users" table
            String query = "INSERT INTO Users (NAME, PASSWORD, IMAGE, REMEMBER)"
                           + "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Set the parameters using the user-provided values
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, number);
            preparedStatement.setBoolean(4, remember);

            // Execute the query
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }



   /**
     * Method that checks the existance of the user in the table "Users"
     *
     * @param username The name of the user that is provided through the Sign up.
     * @param password The password of the user that is provided through the Sign up.
     * @return If the user has already sign up or it is the first time
     */
    public boolean getUserExistanceInDatabase(String username, String password) {
        boolean flag = false;
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(urlgeneral); 
            Statement statement = connection.createStatement(); 
            

    
            query = "SELECT count(*) AS COUNT_USERS FROM Users WHERE NAME = '" +
                     username + "' AND PASSWORD = '" + password + "'" +
                    " HAVING count(*) = 1 ;";

            
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                flag = true;
            }
            statement.close(); 
            connection.close(); 
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
        return flag;
    }

    
    /**
     * ok
     * @param username ok
     * @param password ok
     * @return ok
     */
    public int getUser(String username, String password) {
        int image = 0;
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(urlgeneral); 
            Statement statement = connection.createStatement(); 
            
              
            query = "SELECT IMAGE FROM Users WHERE NAME = '" +
                    username + "' AND PASSWORD = '" + password + "';";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                image = result.getInt("IMAGE");
            }
            
            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
        return image;
    }

    /**
     * ok
     * @param username ok
     * @return ok
     */
    public String remember(String username) {
        String password = null;
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(urlgeneral); 
            Statement statement = connection.createStatement(); 
            
              
            query = "SELECT PASSWORD FROM Users WHERE NAME = '" +
                    username + "' AND REMEMBER = true;";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                password = result.getString("PASSWORD");
            }

            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
        return password;
    }

    /**
     * ok
     * @param username ok
     * @param rememberNew ok
     */
    public void setRemember(String username, Boolean rememberNew) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(urlgeneral);

            // Use a prepared statement to update data into the "Users" table
            String query = "UPDATE Users SET REMEMBER = ? WHERE NAME = '" +
                            username + "'AND REMEMBER != " + rememberNew + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Set the parameters using the user-provided values
            preparedStatement.setBoolean(1, rememberNew);


            // Execute the query
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ok
     * @param username ok
     * @param password ok
     * @param rememberNew ok
     */
    public void handleDataUsers(String username, String password, Boolean rememberNew) {
        if (!getUserExistanceInDatabase(username, password)) {
            insertUsers(username, password, rememberNew);
        } else {
            //compareRemember(username, remember);
            setRemember(username, rememberNew);
        }
    }
}


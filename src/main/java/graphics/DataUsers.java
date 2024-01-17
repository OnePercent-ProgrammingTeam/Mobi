package graphics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.UserExistsException;
import exceptions.UserNotFoundException;

/**
 * Class: DataUser is responsible for interacting with the embedded form of H2 database.
 * It has methods for storing data about users that use our app.
 * As a result it helps in creating tables, inserting data, and querying information.
 */
public class DataUsers {
    static final String urlgeneral = "jdbc:h2:./user";

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
     */
    public void insertUsers(String username, String password) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(urlgeneral);

            // Use a prepared statement to insert data into the "Users" table
            String query = "INSERT INTO Users (NAME, PASSWORD) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Set the parameters using the user-provided values
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

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
     * @param isForSignUp The isForSignUp shows if the method is being called 
     * for the functionality Sign Up or for the functionality Log In 
     * @throws UserExistsException If the user tries to Sign up but the account already exists 
     * @throws UserNotFoundException If the user tries to Log in but the account does not exist 
     * @return If the user has already sign up or it is the first time
     */
    public boolean getUserExistance(String username, String password) {
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
                int count = result.getInt("COUNT_USERS");
                System.out.println("number " + count);
                flag = true;
                /* 
                if (count == 1) {
                    System.out.println("exists " + flag);
                    flag = true;
                }
                */
            }
            statement.close(); 
            connection.close(); 
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
        return flag;
    }

    
    /**
     * Method that shows all the users in the database in the table "Users". 
     * This method is mainly for checking that the users are inserted in the database.
     */
    public void getAllUsers() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(urlgeneral); 
            Statement statement = connection.createStatement(); 
            
              
            query = "SELECT *  FROM Users";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                String name = result.getString("NAME");
                String password = result.getString("PASSWORD");
                System.out.println("User name " + name);
                System.out.println("Pass " + password);
            }
            
            statement.close(); 
            connection.close(); 
           
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
    }
}


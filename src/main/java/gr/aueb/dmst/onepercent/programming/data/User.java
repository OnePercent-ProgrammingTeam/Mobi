package gr.aueb.dmst.onepercent.programming.data;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Random;

/**
 * A database containing the credentials of the users.
 * 
 * <p>It has methods for storing data about users that use Mobi.
 */
public class User {
    
    /** The url for database connectivity. */
    static final String urlgeneral = "jdbc:h2:./databases/user";

    /** The query sent to the database. */
    String query;

    /** Default constructor. */
    public User() { }

    /**
     * Creates a table for the users.
     * 
     * <p>The table is named 'Users' and contains the names and the passwords 
     * of the users that are signed up in our application using Docker Hub credentials.
     */
    public void createUser() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(
                                        urlgeneral);
            Statement statement = connection.createStatement(); 
            query = "CREATE TABLE IF NOT EXISTS Users ("
                + "NAME VARCHAR(50),"
                + "PASSWORD VARCHAR(50),"
                + "IMAGE INT,"
                + "REMEMBER BOOLEAN,"
                + "CONSTRAINT c PRIMARY KEY(NAME,PASSWORD) "
                + ");";
            statement.execute(query);
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        } catch (SQLException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
    }

    /**
     * Insert data into 'Users'.
     *
     * @param username The name of the user that is provided through the Sign up.
     * @param password The password of the user that is provided through the Sign up.
     * @param remember The option to remember the user or not.
     */
    public void insertUsers(String username, String password, Boolean remember) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(urlgeneral);
            Random random = new Random();
            //Randomly, a user icon of our collection will be assigned to user's profile.
            int number = random.nextInt(17) + 1;
            // Use a prepared statement to insert data into the "Users" table.
            String query = "INSERT INTO Users (NAME, PASSWORD, IMAGE, REMEMBER)"
                           + "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Set the parameters using the user-provided values.
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, number);
            preparedStatement.setBoolean(4, remember);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
    }

   /**
     * Checks the existance of the user in the table "Users".
     *
     * @param username The name of the user that is provided through the Sign up.
     * @param password The password of the user that is provided through the Sign up.
     * @return If the user has already signed up or it is the first time.
     */
    public boolean userExists(String username, String password) {
        boolean exists = false;
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(urlgeneral); 
            Statement statement = connection.createStatement();     
            query = "SELECT count(*) AS COUNT_USERS FROM Users WHERE NAME = '" +
                     username + "' AND PASSWORD = '" + password + "'" +
                    " HAVING count(*) = 1 ;";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                exists = true;
            }
            statement.close(); 
            connection.close(); 
        } catch (ClassNotFoundException | SQLException e) { 
            System.out.println(RED + "A database error has occured." + RESET);
        } 
        return exists;
    }

    /**
     * Gets the index of the user icon that is as assigned to his profile by Mobi.
     * 
     * <p>User icons are numbered from 1 to 17.
     * @param username The name of the user.
     * @param password The password of the user.
     * @return The index of the user icon.
     */
    public int getUserIconIndex(String username, String password) {
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
            System.out.println(RED + "A database error has occured." + RESET);
        } 
        return image;
    }

    //TO DO(Anyone): Find a more secure way to transfer the password, maybe encrypted.
    /**
     * Returns the user's password, to automatically fill it at the password field of login page,
     * if the user has selected before the 'Remember me' checkbox.
     * @param username the name of the user
     * @return the password of the user
     */
    public String getPassword(String username) {
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
            System.out.println(RED + "A database error has occured." + RESET);
        } 
        return password;
    }

    /**
     * Set the user to be remember it, if he has selected 'Remember me'.
     * @param username The name of the user.
     * @param remember_me The option to remember the user or not.
     */
    public void setRemember(String username, Boolean remember_me) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(urlgeneral);
            String query = "UPDATE Users SET REMEMBER = ? WHERE NAME = '" +
                            username + "'AND REMEMBER != " + remember_me + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, remember_me);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(RED + "A database error has occured." + RESET);
        }
    }

    /**
     * Handles the connection of the user to Mobi.
     *
     * @param username The name of the user.
     * @param password The password of the user.
     * @param remember_me The option to remember the user or not.
     */
    public void handleDataUsers(String username, String password, Boolean remember_me) {
        if (!userExists(username, password)) {
            insertUsers(username, password, remember_me);
        } else {
            setRemember(username, remember_me);
        }
    }
}

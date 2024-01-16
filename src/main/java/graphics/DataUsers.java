package graphics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.UserExistsException;
import exceptions.UserNotFoundException;

public class DataUsers {
    static final String urlgeneral = "jdbc:h2:./user";

    String query;

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

    public void insertUsers(String username, String password) {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection(urlgeneral);

            if (!getUserExistance(username, password, true)) {
                // Use a prepared statement to insert data into the "Users" table
                String query = "INSERT INTO Users (NAME, PASSWORD) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    // Set the parameters using the user-provided values
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    // Execute the query
                    preparedStatement.execute();
                }
            }
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (UserExistsException e) {
            System.err.println(e.getMessage());
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean getUserExistance(String name, String password,
                                     boolean isForSignUp) 
                                     throws UserExistsException, UserNotFoundException {
        boolean flag = false;
        try {
            Class.forName("org.h2.Driver"); 
            Connection connection = DriverManager.getConnection(urlgeneral); 
            Statement statement = connection.createStatement(); 
            
            query = "SELECT count(*) AS COUNT_USERS FROM Users WHERE NAME = '" +
                     name + "' AND PASSWORD = '" + password + "'";

            
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int count = result.getInt("COUNT_USERS");
                if (count == 1) {
                    flag = true;
                }
            }
            /* 
            statement.close(); 
            connection.close(); 
           */
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
        } 
        if (flag && isForSignUp) {
            throw new UserExistsException(name);
        } else if (!flag && !isForSignUp) {
            throw new UserNotFoundException(name);
        }
        return flag;
    }


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


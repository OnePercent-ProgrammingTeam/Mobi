package gr.aueb.dmst.onepercent.programming.core;

import java.io.IOException;

import org.apache.http.HttpEntity;

import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.data.DataUsers;
import gr.aueb.dmst.onepercent.programming.exceptions.UserNotFoundException;

/**
 * Abstract class for user authentication functionality.
 * 
 * <p>This class provides functionality for user authentication, including checking if a user
 * is signed in Docker Hub, retrieving the username and password, 
 * and executing HTTP requests related to user authentication so as to be connected and proceed
 * in the application.
 * 
 * Mobi uses for authentication the Docker Hub credentials of the user. As a result signing in to
 * Mobi can only be done with Docker Hub user and password. 
 * 
 * This is done by sending an HTTP request. Documentation can be found on:
 * <a href="https://docs.docker.com/docker-hub/api/latest/">Docker Hub API</a>
 *
 * <p>It is the superclass of user authentication classes in both CLI and GUI versions of the 
 * application.
 * 
 * @see SuperHttp
 * @see gr.aueb.dmst.onepercent.programming.cli.UserAuthenticationCLI
 * @see gr.aueb.dmst.onepercent.programming.gui.UserAuthenticationGUI
 */
public abstract class UserAuthentication extends SuperHttp {

    /** Indicates whether the user exists or not. */
    protected boolean userExists;

    /** The instance of the DataUsers class for user data management. */
    protected DataUsers users = new DataUsers();

    /** The instance of the DataBase class for database operations. */
    protected DataBase dataBase = DataBase.getInstance();

    /** The username of the user. */
    protected String username;

    /** The password of the user. */
    protected String password;

    /** The HTTP entity of the response. */
    protected static HttpEntity entity;

    /** Default constructor. */
    public UserAuthentication() { }

    /** Checks if authintication credentials do match. */
    public abstract void checkAuth();

    /**
     * Handles the output after executing the HTTP request.
     *
     * @throws UserNotFoundException if the user is not found.
     */
    public abstract void handleOutput() throws UserNotFoundException;
    
    /**
     * Checks if the user is signed in.
     * 
     * @return true if the user is signed in, false otherwise.
     */
    public boolean isSignedIn() {
        return userExists;
    }

    /**
     * Retrieves the username.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Executes the HTTP request.
     * 
     * <p>This method executes the HTTP request based on the provided message.
     * 
     * @param message the message provided by the user.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public void executeRequest(String message) {
        try {
            if (message.equals("check")) {
                this.http_response = HTTP_CLIENT.execute(postRequest); // Check the user
            }
            entity = this.http_response.getEntity();
            handleOutput();      
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Oops.. something went wrong related to network connection.");
        } 
    }   
}

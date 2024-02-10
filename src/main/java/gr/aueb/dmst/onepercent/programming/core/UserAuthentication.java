package gr.aueb.dmst.onepercent.programming.core;

import java.io.IOException;

import org.apache.http.HttpEntity;

import gr.aueb.dmst.onepercent.programming.data.DataBase;
import gr.aueb.dmst.onepercent.programming.data.DataUsers;
import gr.aueb.dmst.onepercent.programming.exceptions.UserNotFoundException;

/**
 * ok
 */
public abstract class UserAuthentication extends SuperHttp {
    /**ok */
    public UserAuthentication() { }

    /**
     * userExists
     */
    protected boolean userExists;

    /**
     * does the user exists?
     * @return if the user exists 
     */
    public boolean getUserExistanceInDocker() {
        return userExists;
    }

    /**
     * ok
     */
    protected DataUsers users = new DataUsers();

    /**
     * ok
     */
    protected DataBase dataBase = DataBase.getInstance();

    /**
     * username
     */
    protected String username;

    /**
     * ok
     * @return ok
     */
    public String getUsername() {
        return username;
    }

    /**
     * password
     */
    protected String password;

    /**
     * ok
     * @return ok
     */
    public String getPassword() {
        return password;
    }

    /**
     * The entity of the http response.
     */
    protected static HttpEntity entity;

    /**
     * ok
     */
    public abstract void checkAuth();

    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeRequest(String message) {
        try {
            if (message.equals("check")) {
                this.http_response = HTTP_CLIENT.execute(postRequest); // Check the user
            }
            entity = this.http_response.getEntity();
            handleOutput(message);      
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Oops.. something went wrong related to network connection.");
        } 
    }

    /**
     * ok
     * @param message ok
     */
    public abstract void handleOutput(String message) throws UserNotFoundException;
    
}


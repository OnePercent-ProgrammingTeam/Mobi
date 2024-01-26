package gr.aueb.dmst.onepercent.programming.core;

import org.apache.http.HttpEntity;

import gr.aueb.dmst.onepercent.programming.data.DataBase;
import graphics.DataUsers;

public abstract class UserAuthentication extends SuperHttp {

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

    protected DataUsers users = new DataUsers();
    protected DataBase dataBase = DataBase.getInstance();

    /**
     * username
     */
    protected String username;

    public String getUsername() {
        return username;
    }

    /**
     * password
     */
    protected String password;

    public String getPassword() {
        return password;
    }

    /**
     * The entity of the http response.
     */
    protected static HttpEntity entity;

    public abstract void checkAuth();

    /** Method: executeHttpRequest(String) executes the http request 
     * @param message the message that is given by the user.
     * throws Exception if an error occurs while executing the http request.
     */
    @Override
    public void executeHttpRequest(String message) {
        try {
            if (message.equals("check")) {
                this.response = HTTP_CLIENT.execute(postRequest); // Check the user
            }
            entity = this.response.getEntity();
            handleOutput(message);      
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace of the error
        } 
    }

    public abstract void handleOutput(String message);
    
}


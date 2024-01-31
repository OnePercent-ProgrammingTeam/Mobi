package gr.aueb.dmst.onepercent.programming.cli;

import java.io.Console;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import gr.aueb.dmst.onepercent.programming.core.UserAuthentication;

/**
 * ok
 */

public class UserAuthenticationCLI extends UserAuthentication {
    /**ok */
    public UserAuthenticationCLI() { }
    /**
     * ANSI color code for resetting text color.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    
    /**
     * ANSI color code for green text.
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * ok
     */
    public static final String ANSI_YELLOW = "\u001B[33m";


    /**
     * ok
     */
    public void logIn() {
        //while the user does not exists try to log in
        checkAuth(); 
        while (!getUserExistanceInDocker()) {
            checkAuth();
        }
        users.createUser();
        //if exists then handle him in the database
        //when runninf form the cli i do not want to have the remember for the first time
        users.handleDataUsers(getUsername(), getPassword(), false);
        dataBase.setURL(getUsername());
        dataBase.createDatabaseMetrics(); 
    }
    
    @Override
    public void checkAuth() {
        String message = "check";
        username = Main.handleInput("Username: ");

        // Use Console to read password without echoing characters
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword("Password: ");
            password = new String(passwordArray);
            // Clear the password from memory for security reasons
            java.util.Arrays.fill(passwordArray, ' ');
        } else {
            // Fallback to your original method if console is not available
            password = Main.handleInput("Password: ");
        }

        
        
        postRequest = new HttpPost("https://hub.docker.com/v2/users/login");

        String jsonBody = "{\"username\": \"" + username 
                        + "\", \"password\": \"" + password + "\"}";
        postRequest.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        executeRequest(message);
    }




    /** Method handleOutput(String) prints the appropriate message, based on the status 
     *  code of the http response and the request that has been done.
     * @param message the message that indicates the action that is going to be executed. 
     */
    @Override
    public void handleOutput(String message) {
        String output = "";
        if (this.response == null) {
            output = "response has not been initialized";
        } else {
            switch (this.response.getStatusLine().getStatusCode()) {
                case 200: 
                    output = ANSI_GREEN + "Successfull Authentication" + ANSI_RESET;
                    userExists = true;
                    break;
                case 401: 
                    output = "Authentication failed. Try again!";
                    userExists = false;
                    break;
                case 404:
                    output = "This is not a Docker Hub user. Try again!";
                    userExists = false;
                    break;
            }
        } 
        System.out.println(output);
        System.out.println("------------------------------------------------------------");
    }
}

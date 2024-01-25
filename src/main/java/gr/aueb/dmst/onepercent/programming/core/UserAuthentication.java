package gr.aueb.dmst.onepercent.programming.core;

import java.io.Console;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import exceptions.UserNotFoundException;
import gr.aueb.dmst.onepercent.programming.cli.Main;

public class UserAuthentication extends SuperHttp {
    /**
     * ANSI color code for resetting text color.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    
    /**
     * ANSI color code for green text.
     */
    public static final String ANSI_GREEN = "\u001B[32m";


    /**
     * userExists
     */
    private boolean userExists;

    public boolean getUserExistanceInDocker() {
        return userExists;
    }


    /**
     * username
     */
    private String username;

    public String getUsername() {
        return username;
    }

    /**
     * password
     */
    private String password;

    public String getPassword() {
        return password;
    }

    /**
     * The entity of the http response.
     */
    private static HttpEntity entity;

    public void checkAuth() {
        String message = "check";
        System.out.println("Docker Hub login, please proceed");
        System.out.println("-------------------------------------------------------------");
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

        
        System.out.println("-------------------------------------------------------------");
        postRequest = new HttpPost("https://hub.docker.com/v2/users/login");

        String jsonBody = "{\"username\": \"" + username 
                        + "\", \"password\": \"" + password + "\"}";
        postRequest.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        executeHttpRequest(message);
    }

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


    /** Method handleOutput(String) prints the appropriate message, based on the status 
     *  code of the http response and the request that has been done.
     * @param message the message that indicates the action that is going to be executed. 
     * @return the correct message
     */
    public String handleOutput(String message) throws UserNotFoundException {
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
                    userExists = false;
                    throw new UserNotFoundException(username);
            }
        } 
        System.out.println(output);
        System.out.println("------------------------------------------------------------");
        return output;
    }
}


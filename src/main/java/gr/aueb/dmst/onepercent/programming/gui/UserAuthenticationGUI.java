package gr.aueb.dmst.onepercent.programming.gui;

import gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits;
import gr.aueb.dmst.onepercent.programming.core.UserAuthentication;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

/**
 * A class responsible for user authentication in the GUI version of the application.
 * 
 * <p>This class extends {@link gr.aueb.dmst.onepercent.programming.core.UserAuthentication}
 * and implements methods to perform the login process, handle user authentication, and 
 * manage user data related to Docker Hub (store them in application's database).
 * 
 * <p>Mobi uses for authentication the Docker Hub credentials of the user. As a result signing in to
 * Mobi can only be done with Docker Hub user and password.
 * 
 * <p>It includes methods to prompt the user for credentials, securely read the password,
 * send an HTTP POST request to 
 * to authenticate the user, and handle the output message based 
 * on the response received from the HTTP request.
 * 
 * @see gr.aueb.dmst.onepercent.programming.core.UserAuthentication
 */
public class UserAuthenticationGUI extends UserAuthentication {
   
    /** Default constructor. */
    public UserAuthenticationGUI() { }

    /**
     * Set user's credentials.
     * @param username The name of the user.
     * @param password The password of the user.
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

     /**
     * Checks the authentication credentials of the user.
     * 
     * <p>Sends an HTTP POST request to the Docker Hub API endpoint 
     * <a href="https://docs.docker.com/engine/api/v1.43/">Docker Engine API</a> with the provided 
     * username and password as JSON in the request body schema.
     * 
     * @see ConsoleUnits#promptForInput(String)
     * @see #executeRequest(String)
     */
    @Override
    public void checkAuth() {
        String message = "check";
        
        postRequest = new HttpPost("https://hub.docker.com/v2/users/login");

        String jsonBody = "{\"username\": \"" + username 
                        + "\", \"password\": \"" + password + "\"}";
        postRequest.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        executeRequest(message);
    }

    /** 
     * Handles the output message based on the response received from the HTTP request.
     * userExists is then used to show the appropriate message to the user.
     */
    @Override
    public void handleOutput() {
        if (this.http_response == null) {
            return;
        } else {
            switch (this.http_response.getStatusLine().getStatusCode()) {
                case 200: 
                    userExists = true;
                    break;
                case 401: 
                    userExists = false;
                    break;
            }
        } 
    }
}

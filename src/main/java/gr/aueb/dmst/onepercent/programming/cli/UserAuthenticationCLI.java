package gr.aueb.dmst.onepercent.programming.cli;


import gr.aueb.dmst.onepercent.programming.core.UserAuthentication;
import gr.aueb.dmst.onepercent.programming.exceptions.UserNotFoundException;

import java.io.Console;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;


/**
 * A class responsible for user authentication in the CLI version of the application.
 * 
 * <p>This class extends {@link gr.aueb.dmst.onepercent.programming.core.UserAuthentication}
 * and implements methods to perform the login process, handle user authentication, and 
 * manage user data related to Docker Hub (store them in application's database).
 * 
 * <p>It includes methods to prompt the user for credentials, securely read the password,
 * send an HTTP POST request to authenticate the user, and handle the output message based 
 * on the response received from the HTTP request.
 * 
 * @see gr.aueb.dmst.onepercent.programming.core.UserAuthentication
 */
public class UserAuthenticationCLI extends UserAuthentication {
    
    /** Default constructor. */
    public UserAuthenticationCLI() { }

    /**
     * Performs the login process for the user, based on his credentials in Docker
     * Hub.
     * 
     * <p>This method attempts to log in the user by checking authentication,
     * creating the user if they do not exist in Docker, and handling user data.
     * If the user exists, it updates the database URL and creates database metrics.
     */
    public void logIn() {
        /*If credentials to not match, check again until they do.*/
        checkAuth(); 
        while (!getUserExistanceInDocker()) { //
            checkAuth();
        }
        users.createUser();
        /*If user exists store his credentials in the database.*/
        users.handleDataUsers(getUsername(), getPassword(), false);
        dataBase.setURL(getUsername());
        dataBase.createDatabaseMetrics(); 
    }
    
    /**
     * Checks the authentication credentials of the user.
     * 
     * <p>Prompts the user to enter their username and password. If the Java Console is available,
     * the password is read securely without echoing characters. Otherwise, the password is read
     * using the {@link ConsoleUnits#promptForInput(String)} method.
     * 
     * <p>Sends an HTTP POST request to the Docker Hub API endpoint 
     * <a href="https://docs.docker.com/engine/api/v1.43/">Docker Engine API</a> with the provided 
     * username and password as JSON in the request body schema. The method then executes the 
     * request to authenticate the user.
     * 
     * @see ConsoleUnits#promptForInput(String)
     * @see #executeRequest(String)
     */
    @Override
    public void checkAuth() {
        String message = "check";
        username = ConsoleUnits.promptForInput("Username: ");
        /* Use console to read password without echoing characters for security reasons. */
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword("Password: ");
            password = new String(passwordArray);
            /* Clear the password from memory for security reasons. */
            java.util.Arrays.fill(passwordArray, ' ');
        } else {
            /* Fallback to your original method if console is not available. */
            password = ConsoleUnits.promptForInput("Password: ");
        }
        System.out.println("\nPlease wait...\n");
        postRequest = new HttpPost("https://hub.docker.com/v2/users/login");

        String jsonBody = "{\"username\": \"" + username 
                        + "\", \"password\": \"" + password + "\"}";
        postRequest.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        executeRequest(message);
    }

   /**
     * Handles the output message based on the response received from the HTTP request.
     * 
     * <p>If the response is null, sets the output message to indicate that the response 
     * has not been initialized. Otherwise, determines the appropriate output message 
     * based on the HTTP status code of the response:
     * <ul>
     *   <li>200 - Sets the output message to indicate successful authentication
     *   <li>401 - Sets the output message to indicate authentication failure
     *   <li>404 - Sets the output message to indicate that the user is not found on Docker Hub 
     * </ul>
     * 
     * @param message The message to be handled, typically indicating the action being performed.
     */
    @Override
    public void handleOutput(String message) throws UserNotFoundException {
        String output = "";
        if (this.http_response == null) {
            output = "Response has not been initialized";
        } else {
            switch (this.http_response.getStatusLine().getStatusCode()) {
                case 200: 
                    output = "\n" +  ConsoleUnits.GREEN
                         + "Successfull Authentication" + ConsoleUnits.RESET + "\n";
                    userExists = true;
                    System.out.println(output);
                    break;
                case 401: 
                    userExists = false;
                    String errorMessage = "Incorrect authentication credentials."
                         + " Wrong username: ".concat(username).concat(" or password.");
                    /* Throw customized request when authentication is failed. */
                    throw new UserNotFoundException(errorMessage, 
                                                    ConsoleUnits.RED, 
                                                    ConsoleUnits.RESET);
            }
        }   
    }
}

package gr.aueb.dmst.onepercent.programming.exceptions;

/** 
 * A customized exception, thrown when user's credentials are not found in DockerHub.
 * Extends the Exception class in order to be thrown. 
 * It is used in the GUI version of the application.
 */
public class UserNotFoundException extends Exception {
    //TODO: Implement it to the GUI version of the application.

    /** Default Constructor. */
    public UserNotFoundException() { }

    /** 
    * Constructor to display the message when the user is not found in DockerHub.
    * @param username The name with which the user is trying to login.
    */
    public UserNotFoundException(String username) {
        super("Incorrect authentication credentials."
            + " Wrong username: ".concat(username).concat(" or password."));
    }

    /**
     * Constructor to display the message when the user is not found in DockerHub used in the 
     * CLI version of the application.
     * @param message The message to be displayed.
     * @param color The ANSI color code of the message.
     * @param reset The ANSI color reset code.
     */
    public UserNotFoundException(String message, String color, String reset) {
        super(color.concat(message).concat(reset).concat("\n"));
    }
}

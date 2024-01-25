package exceptions;

/** Class: UserNotFoundException is the class that creates an exception 
 * if the user does not has an acoount */
public class UserNotFoundException extends Exception {
    /** 
     * Default Constructor
     */
    public UserNotFoundException() { }

    /** 
    * Constructor that show a message
    * @param username The name of the user
    */
    public UserNotFoundException(String username) {
        super("The user with the name '" + username 
                + "' and the specific password is not a DockerHub user. Try again!");
    }
}

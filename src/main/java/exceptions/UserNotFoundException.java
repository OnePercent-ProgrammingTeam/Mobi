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
        super("User with the name '" + username + "'' and the specific password does not exists");
    }
}
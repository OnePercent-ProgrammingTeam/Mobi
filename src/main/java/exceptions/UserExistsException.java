package exceptions;
/** Class: UserExistsException is the class that creates an exception 
 * if the user has already signed up */
public class UserExistsException extends Exception {

    /** 
     * Default Constructor
     */
    public UserExistsException() { }


    /** 
    * Constructor that show a message
    * @param username The name of the user
    */
    public UserExistsException(String username) {
        super("User with the name " + username + " and the specific password already exists");
    }
}

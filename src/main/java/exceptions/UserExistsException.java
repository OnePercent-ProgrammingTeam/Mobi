package exceptions;

public class UserExistsException extends Exception {
    public UserExistsException() { }

    public UserExistsException(String username) {
        super("User with the name " + username + " and the specific password already exists");
    }
}

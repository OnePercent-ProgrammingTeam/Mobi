package exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() { }

    public UserNotFoundException(String username) {
        super("User with the name '" + username + "'' and the specific password does not exists");
    }
}

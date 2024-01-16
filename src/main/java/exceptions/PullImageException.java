package exceptions;

/**
 * Exception class that extends RuntimeException.
 * It is used to throw an exception when the user tries to pull an image that
 * does not exist in the registry.
 * @see java.lang.RuntimeException
 */
public class PullImageException extends RuntimeException {

    // Regular Colors
    /**
    * ANSI color code for resetting text color.
    */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
    * ANSI color code for red text.
    */
    public static final String ANSI_RED = "\u001B[31m";


    /**
     * Default constructor.
     */
    public PullImageException() {

    }

    /**
     * Constructor with message.
     * @param message the message to be displayed.
     */
    public PullImageException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }

}

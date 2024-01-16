package exceptions;
/**
 * Exception class that extends UnsupportedOperationException.
 * It is used to throw an exception when the user provides an invalid input that
 * cannot be handled by the program. It is used in the CLI version of the program
 * to indicate that the user has provided an invalid input (not one of reccomended)
 * @see java.lang.UnsupportedOperationException
 */
public class InvalidInputException extends UnsupportedOperationException  {
    
    //ANSI color codes for text color.
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
    public InvalidInputException() {

    }

    /**
     * Constructor with message.
     * @param message the message to be displayed.
     */
    public InvalidInputException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }
}

package exceptions;

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
     * @param message
     */
    public InvalidInputException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }
}

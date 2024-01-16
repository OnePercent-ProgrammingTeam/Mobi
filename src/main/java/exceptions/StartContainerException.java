package exceptions;

/** Exception that occurs when user tries to start an already
 * started container.
 */
public class StartContainerException extends RuntimeException {
   
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
    public StartContainerException() {

    }

    /**
     * Constructor with message.
     * @param message the message to be displayed.
     */
    public StartContainerException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }

}

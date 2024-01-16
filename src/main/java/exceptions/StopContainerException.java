package exceptions;

/** Exception that occurs when user tries to stop an already
 * stopped container.
 */
public class StopContainerException extends RuntimeException {
      // Regular Colors
    /**
     * ANSI color code for resetting text color.
     */
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * ANSI color code for red text.
     */
    private static final String ANSI_RED = "\u001B[31m";


    /**
     * Default constructor.
     */
    public StopContainerException() {

    }

    /**
     * Constructor with message.
     * @param message the message to be displayed.
     */
    public StopContainerException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }

}

package exceptions;

/**
 * ok
 */

public class RemoveDockerObjectException extends RuntimeException {
    
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
     * ok
     * @param message ok
     */
    public RemoveDockerObjectException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }

    /**
     * Default Constructor
     */
    public RemoveDockerObjectException() {
        super();
    }



}

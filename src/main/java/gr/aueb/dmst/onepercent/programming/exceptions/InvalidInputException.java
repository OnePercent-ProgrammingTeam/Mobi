package gr.aueb.dmst.onepercent.programming.exceptions;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

/**
 * A customized exception, thrown when the user provides an invalid input that
 * cannot be handled by the program. 
 * 
 * It is used in the CLI version to indicate that the user has provided an invalid input
 * (not one of the recommended ones) {@link gr.aueb.dmst.onepercent.programming.cli.MenuThreadCLI}.
 * 
 * Extends UnsupportedOperationException, so it is an unchecked exception.
 */
public class InvalidInputException extends UnsupportedOperationException  {
    
    /** Default constructor. */
    public InvalidInputException() { }

    /**
     * Constructor to display the message related to the exception.
     * @param message the message to be displayed.
     */
    public InvalidInputException(String message) {
        super(RED.concat(message).concat(RESET));
    }
}

package gr.aueb.dmst.onepercent.programming.exceptions;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

/**
 * A customized exception, thrown when a text field is empty.
 * It is used in the GUI version of the application when user
 * tries to search for an image without providing any input.
 * {@link gr.aueb.dmst.onepercent.programming.graphics.SearchController}
 * Extends RuntimeException, so it is an unchecked exception.
 */
public class EmptyFieldError extends RuntimeException {
    
    /** Default message to be displayed when the exception is thrown.*/
    private static final String DEFAULT_MESSAGE = "Please, provide a value for this field.";

    /** Default constructor, used to display the default message. */
    public EmptyFieldError() {
        super(RED.concat(DEFAULT_MESSAGE).concat(RESET));
    }

    /**
     * Constructor to display a custom message.
     * @param customMessage the message to be displayed.
     */
    public EmptyFieldError(String customMessage) {
        super(RED + customMessage + RESET);
    }

    /**
     * Returns the color code for red text.
     * @return red hexadecimal color code.
     */
    public String getColorCode() {
        return  RED;
    }

    /**
     * Returns the default message to be displayed.
     * @return the default message.
     */
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }
}

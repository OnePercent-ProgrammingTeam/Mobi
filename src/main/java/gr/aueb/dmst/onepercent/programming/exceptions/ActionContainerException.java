package gr.aueb.dmst.onepercent.programming.exceptions;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

/** 
 * A customized exception, thrown when:
 * 1. an inappropiate action is performed on a container. 
 * 2. a server error occurs while trying to perform an action on a container.
 * Extends RuntimeException, so it is an unchecked exception.
 * <p>
 * Inappropriate actions are considered to be:
 * 1. starting/stopping a container that is already running/stopped.
 * 2. trying to execute a command on a container that cannot be found.
 * <p>
 * It used only in the CLI version of the application.
 * {@link gr.aueb.dmst.onepercent.programming.cli} 
 * In the GUI version, such exceptions are controlled by a toggle button. 
 * {@link gr.aueb.dmst.onepercent.programming.graphics.ContainersPageController}
 */
public class ActionContainerException extends RuntimeException {

    /** Default constructor. */
    public ActionContainerException() { }

    /**
     * Constructor to display the message related to the exception.
     * @param message the message to be displayed.
     */
    public ActionContainerException(String message) {
        super(RED.concat(message).concat(RESET));
    }

    /** Returns the error message. */
    @Override
    public String getMessage() {
        return RED + super.getMessage() + RESET;
    }

}

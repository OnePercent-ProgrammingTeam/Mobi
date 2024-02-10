package gr.aueb.dmst.onepercent.programming.exceptions;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

/**
 * A customized exception, thrown when the user tries to remove a Docker object.
 * Extends RuntimeException, so it is an unchecked exception.
 * It is used in the CLI version of the application. 
 * {@link gr.aueb.dmst.onepercent.programming.cli.ManagerHttpCLI}
 * It occurs based on the HTTP response status code, returned from the Docker Engine API. 
 * Consequently, it is thrown due to:
 * 1. Bad Parameter (400)
 * 2. No such object (404)
 * 3. Conflict (409)
 * 4. Server Error (500)
 */
public class RemoveDockerObjectException extends RuntimeException {

    /** Default Constructor. */
    public RemoveDockerObjectException() {
        super();
    }

    /**
     * Constructor to display the message related to the exception.
     * @param message the message to be displayed.
     */
    public RemoveDockerObjectException(String message) {
        super(RED.concat(message).concat(RESET));
    }
}

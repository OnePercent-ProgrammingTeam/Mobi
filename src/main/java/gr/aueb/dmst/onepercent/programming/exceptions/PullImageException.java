package gr.aueb.dmst.onepercent.programming.exceptions;

import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RED;
import static gr.aueb.dmst.onepercent.programming.cli.ConsoleUnits.RESET;

/**
 * An customized exception, thrown when the user tries to pull an image that
 * does not exist in the registry. Extends RuntimeException, so it is an unchecked exception.
 * It is used in the CLI version as well as in the GUI version of the application.
 * 
 * ANSI color codes are used in the CLI version of the application to customize the output
 * of the console (e.g red color represents exception).
 * 
 * TO BE IMPEMENTED:
 * The specified fields:
 * 1. statusCode: 
 *    the status of the HTTP response, when the user tries to pull an image that
 *    does not exist in the registry.
 * 2. statusCodeStyle: 
 *    the CSS style of the status code.
 * 3. style:
 *    the CSS style of the message.
 * are used in the GUI version to customize the appereance of the message, related to the 
 * exception. 
 */
public class PullImageException extends RuntimeException {

    /** The status code of the exception. */
    private int statusCode;

    /** The style of the status code. */
    private String statusCodeStyle;
   
    /** The style of the message. */
    private String style;

    /** The message to be displayed. */
    private String message;
  
    /** Default constructor. */
    public PullImageException() { }

    /**
     * Constructor to display the message related to the exception.
     * @param message the message to be displayed.
     */
    public PullImageException(String message) {
        super(RED.concat(message).concat(RESET));
    }

    /**
     * Constructor to initialize the fields of the exception, related
     * to the appearance of the message and it's content.
     * @param message the message to be displayed.
     * @param style the style of the message.
     * @param statusCode the status code.
     * @param statusCodeStyle the style of the status code.
     */
    public PullImageException(String message, String style,
         int statusCode, String statusCodeStyle) {
        this.message = message;
        this.style = style;
        this.statusCode = statusCode;
        this.statusCodeStyle = statusCodeStyle;
    }

    /** 
     * Returns the message of the exception.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the style of the appearance of the exception (String with CSS).
     * @return the style.
     */
    public String getStyle() {
        return style;
    }

    /**
     * Returns the status code.
     * @return the status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the style of the status code.
     * @return the status code style.
     */
    public String getStatusCodeStyle() {
        return statusCodeStyle;
    }
}

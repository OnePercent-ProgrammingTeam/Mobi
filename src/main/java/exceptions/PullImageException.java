package exceptions;

/**
 * Exception class that extends RuntimeException.
 * It is used to throw an exception when the user tries to pull an image that
 * does not exist in the registry. This class is used in the CLI version of the
 * application, as well as in the GUI version. The GUI version uses the
 * statusCode, statusCodeStyle, style variables to display the status code of the
 * exception in the GUI. The variables containing the word "style" are used to
 * style the message and the status code, in order to be displayed in the GUI.
 * ANSI color codes are used in the CLI version of the application to indicate
 * the status code of the exception (red color represents an error, specifically
 * exception).
 * @see java.lang.RuntimeException
 */
public class PullImageException extends RuntimeException {

    /**
    * ANSI color code for resetting text color.
    */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
    * ANSI color code for red text.
    */
    public static final String ANSI_RED = "\u001B[31m";

    /**ok */
    private String message;
    /**ok */
    private String style;
    /**ok */
    private int statusCode;
    /**ok */
    private String statusCodeStyle;


    /**
     * Default constructor.
     */
    public PullImageException() {

    }

    /**
     * Constructor with message.
     * @param message the message to be displayed.
     */
    public PullImageException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }

    /**
     * Constructor with message and style. Used in the GUI
     * version of the application.
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
     * Getter for message.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for style.
     * @return the style.
     */
    public String getStyle() {
        return style;
    }

    /**
     * Getter for status code.
     * @return the status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Getter for status code style.
     * @return the status code style.
     */
    public String getStatusCodeStyle() {
        return statusCodeStyle;
    }

}

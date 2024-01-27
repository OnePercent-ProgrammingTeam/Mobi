package exceptions;

/**
 * ok
 */
public class EmptyFieldError extends RuntimeException {
    private static final String RED_COLOR_CODE = "#ff0000";
    private static final String DEFAULT_MESSAGE = "Please, provide a value for this field.";

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
     */
    public EmptyFieldError() {
        super(ANSI_RED + DEFAULT_MESSAGE + ANSI_RESET);
    }

    /**
     * ok
     * @param customMessage ok
     */
    public EmptyFieldError(String customMessage) {
        super(ANSI_RED + customMessage + ANSI_RESET);
    }

    /**
     * ok
     * @return ok
     */
    public String getColorCode() {
        return RED_COLOR_CODE;
    }

    /**
     * ok
     * @return ok
     */
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

}

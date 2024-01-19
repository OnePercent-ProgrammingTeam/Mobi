package exceptions;

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

    public EmptyFieldError() {
        super(ANSI_RED + DEFAULT_MESSAGE + ANSI_RESET);
    }

    public EmptyFieldError(String customMessage) {
        super(ANSI_RED + customMessage + ANSI_RESET);
    }

    public String getColorCode() {
        return RED_COLOR_CODE;
    }

    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

}

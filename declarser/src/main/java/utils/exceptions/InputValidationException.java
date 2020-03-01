package utils.exceptions;

public class InputValidationException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while validating the input %s " +
            "before mapping it";

    private final Object value;

    private InputValidationException(Object value, Exception ex) {
        super(String.format(messageFormatter, ex.toString(), value.toString()), ex);
        this.value = value;
    }

    public static InputValidationException of(Object value, Exception ex) {
        return new InputValidationException(value, ex);
    }

    public Object getValue() {
        return value;
    }
}

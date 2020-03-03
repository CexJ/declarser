package utils.exceptions;

public class InputMappingException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while mapping the input %s ";

    private final Object value;

    private InputMappingException(
            final Object value,
            final Exception ex) {
        super(String.format(messageFormatter, ex.toString(), value.toString()), ex);
        this.value = value;
    }

    public static InputMappingException of(
            final Object value,
            final Exception ex) {
        return new InputMappingException(value, ex);
    }

    public Object getValue() {
        return value;
    }
}

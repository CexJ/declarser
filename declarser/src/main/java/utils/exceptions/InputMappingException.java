package utils.exceptions;

public class InputMappingException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while mapping the input %s ";

    private final Object value;

    private InputMappingException(Object value, Exception ex) {
        super(String.format(messageFormatter, ex.toString(), value.toString()), ex);
        this.value = value;
    }

    public static InputMappingException of(Object value, Exception ex) {
        return new InputMappingException(value, ex);
    }

    public Object getValue() {
        return value;
    }
}

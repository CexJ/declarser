package utils.exceptions;

public class MappingInputValidationException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while validating the input %s " +
            "before mapping it";

    private final Object value;

    private MappingInputValidationException(Object value, Exception ex) {
        super(String.format(messageFormatter, ex.toString(), value.toString()), ex);
        this.value = value;
    }

    public static MappingInputValidationException of(Object value, Exception ex) {
        return new MappingInputValidationException(value, ex);
    }

    public Object getValue() {
        return value;
    }
}

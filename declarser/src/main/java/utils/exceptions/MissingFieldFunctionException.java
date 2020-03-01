package utils.exceptions;

public class MissingFieldFunctionException extends Exception {

    public final static String messageFormatter =
            "Missing function for the key: %s";

    private final Object key;

    private MissingFieldFunctionException(Object key) {
        super(String.format(messageFormatter, key.toString()));
        this.key = key;
    }

    public static MissingFieldFunctionException of(Object key) {
        return new MissingFieldFunctionException(key);
    }
}

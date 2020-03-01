package utils.exceptions;

public class TypingFieldException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while typing the field of key %s " +
            "from the value %s";

    private final Object key;
    private final Object value;

    private TypingFieldException(Object key, Object value, Exception ex) {
        super(String.format(messageFormatter, ex.toString(), key.toString(), value.toString()), ex);
        this.key = key;
        this.value = value;
    }

    public static TypingFieldException of(Object key, Object value, Exception ex) {
        return new TypingFieldException(key, value, ex);
    }
}

package kernel.stages.stage02_totypedmap.impl.exceptions;

public class TypingFieldException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while typing the field of key %s " +
            "from the value %s";

    private final Object key;
    private final Object value;

    private TypingFieldException(
            final Object key,
            final Object value,
            final Exception ex) {
        super(String.format(messageFormatter, ex.toString(), key.toString(), value.toString()), ex);
        this.key = key;
        this.value = value;
    }

    public static TypingFieldException of(
            final Object key,
            final Object value,
            final Exception ex) {
        return new TypingFieldException(key, value, ex);
    }
}

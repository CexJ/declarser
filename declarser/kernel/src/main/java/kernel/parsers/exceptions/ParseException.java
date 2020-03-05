package kernel.parsers.exceptions;

public class ParseException extends Exception {

    public final static String messageFormatter =
            "I cannot parse: %s into a %s";


    private final Object value;
    private final Class<?> clazz;

    private ParseException(
            final Object value,
            final Class<?> clazz,
            final Exception cause) {
        super(String.format(messageFormatter, value, clazz));
        this.value = value;
        this.clazz = clazz;
    }

    public static ParseException of(
            final Object value,
            final Class<?> clazz,
            final Exception cause){
        return new ParseException(value, clazz, cause);
    }
}

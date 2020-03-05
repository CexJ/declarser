package kernel.parsers.exceptions;

public class ParserException extends Exception {

    public final static String messageFormatter =
            "I cannot parse: %s into a %s because %s";


    private final Object value;
    private final Class<?> clazz;

    private ParserException(
            final Object value,
            final Class<?> clazz,
            final Exception cause) {
        super(String.format(messageFormatter, value.toString(), clazz.toString(), cause.toString()), cause);
        this.value = value;
        this.clazz = clazz;
    }

    public static ParserException of(
            final Object value,
            final Class<?> clazz,
            final Exception cause){
        return new ParserException(value, clazz, cause);
    }
}
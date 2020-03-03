package utils.exceptions;

public class DeclarserException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while parsing the following input: %s";

    private Object input;

    public DeclarserException( Object input, Exception cause) {
        super(String.format(messageFormatter, cause.toString(), input.toString()), cause);
        this.input = input;
    }

    public static DeclarserException of(Object input, Exception ex) {
        return new DeclarserException(input,ex);
    }
}

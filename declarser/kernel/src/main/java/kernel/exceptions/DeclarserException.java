package kernel.exceptions;

public class DeclarserException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while parsing the following input: %s";

    private Object input;

    public DeclarserException(
            final Object input,
            final Exception cause) {
        super(String.format(messageFormatter, cause.toString(), input.toString()), cause);
        this.input = input;
    }

    public static DeclarserException of(
            final Object input,
            final Exception ex) {
        return new DeclarserException(input,ex);
    }

    public Object getInput() {
        return input;
    }
}

package io.github.cexj.declarser.kernel.exceptions;

public final class DeclarserException extends Exception {

    public final static String messageFormatter =
            "%s while parsing the following input: %s";

    private final Object input;

    public DeclarserException(
            final Object input,
            final Exception cause) {
        super(String.format(messageFormatter, cause.getMessage(), input.toString()), cause);
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

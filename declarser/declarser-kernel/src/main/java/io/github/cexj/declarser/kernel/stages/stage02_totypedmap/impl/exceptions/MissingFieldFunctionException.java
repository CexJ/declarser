package io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.exceptions;

public final class MissingFieldFunctionException extends Exception {

    public final static String messageFormatter =
            "Missing function for the key: %s";

    private final Object key;

    private MissingFieldFunctionException(
            final Object key) {
        super(String.format(messageFormatter, key.toString()));
        this.key = key;
    }

    public static MissingFieldFunctionException of(
            final Object key) {
        return new MissingFieldFunctionException(key);
    }
}

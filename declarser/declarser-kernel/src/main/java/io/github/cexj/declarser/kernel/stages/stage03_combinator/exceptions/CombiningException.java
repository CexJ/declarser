package io.github.cexj.declarser.kernel.stages.stage03_combinator.exceptions;


public final class CombiningException extends Exception {

    public final static String messageFormatter =
            "%s while combining the following key: %s";

    private final Object key;

    private CombiningException(
            final Object key,
            final Exception cause) {
        super(String.format(messageFormatter, cause.getMessage(), key.toString()), cause);
        this.key = key;
    }

    public static CombiningException of(
            final Object label,
            final Exception exception){
        return new CombiningException(label, exception);
    }

    public Object getKey() {
        return key;
    }

}

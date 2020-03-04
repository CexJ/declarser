package kernel.exceptions;


public final class CombiningException extends Exception {

    public final static String messageFormatter =
            "I encountered the following exception: %s, " +
            "while combining the following key: %s";

    private final Object key;

    private CombiningException(
            final Object key,
            final Exception cause) {
        super(String.format(messageFormatter, cause.toString(), key.toString()), cause);
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

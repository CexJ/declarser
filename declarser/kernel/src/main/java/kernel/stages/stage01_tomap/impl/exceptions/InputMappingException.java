package kernel.stages.stage01_tomap.impl.exceptions;

public final class InputMappingException extends Exception {

    public final static String messageFormatter =
            "%s while mapping the input %s ";

    private final Object value;

    private InputMappingException(
            final Object value,
            final Exception ex) {
        super(String.format(messageFormatter, ex.getMessage(), value.toString()), ex);
        this.value = value;
    }

    public static InputMappingException of(
            final Object value,
            final Exception ex) {
        return new InputMappingException(value, ex);
    }

    public Object getValue() {
        return value;
    }
}

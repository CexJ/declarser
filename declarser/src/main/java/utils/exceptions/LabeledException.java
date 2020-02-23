package utils.exceptions;

import java.util.Collection;

public final class LabeledException extends Exception {

    private final Object label;
    private final Exception exception;


    private LabeledException(final Object label, final Exception exception) {
        this.label = label;
        this.exception = exception;
    }

    public static  LabeledException of(final Object label, final Exception exception){
        return new LabeledException(label, exception);
    }

    public Object getLabel() {
        return label;
    }

    public Exception getException() {
        return exception;
    }
}

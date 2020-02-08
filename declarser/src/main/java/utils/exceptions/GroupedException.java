package utils.exceptions;

import java.util.List;

public final class GroupedException extends Exception {

    private final List<? extends Exception> exceptions;

    private GroupedException(final List<? extends Exception> exceptions) {
        this.exceptions = exceptions;
    }

    public static GroupedException of(final List<? extends Exception> exceptions){
        return new GroupedException(exceptions);
    }
}

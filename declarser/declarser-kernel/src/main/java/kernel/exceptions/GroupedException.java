package kernel.exceptions;

import java.util.Collection;

public final class GroupedException extends Exception {

    private final Collection<? extends Exception> exceptions;

    private GroupedException(
            final Collection<? extends Exception> exceptions) {
        this.exceptions = exceptions;
    }

    public static GroupedException of(
            final Collection<? extends Exception> exceptions){
        return new GroupedException(exceptions);
    }

    public Collection<? extends Exception> getExceptions() {
        return exceptions;
    }
}

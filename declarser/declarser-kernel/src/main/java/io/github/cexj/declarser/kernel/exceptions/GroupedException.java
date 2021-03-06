package io.github.cexj.declarser.kernel.exceptions;

import java.util.List;
import java.util.Objects;

public final class GroupedException extends Exception {


    private final List<? extends Exception> exceptions;

    private GroupedException(
            final List<? extends Exception> exceptions) {
        super(exceptions.stream()
                .map(Exception::getMessage)
                .filter(Objects::nonNull)
                .reduce((i,j) -> ""+i+", "+j)
                .orElse(""));
        this.exceptions = exceptions;
    }

    public static GroupedException of(
            final List<? extends Exception> exceptions){
        return new GroupedException(exceptions);
    }

    public List<? extends Exception> getExceptions() {
        return exceptions;
    }


}

package kernel.exceptions;

import java.util.Collection;
import java.util.stream.Collectors;

public class FunctionTypeException extends Exception {

    public final static String messageFormatter =
            "I was expecting that the list %s was contained into %s but I find these elements: %s";

    @SuppressWarnings("rawtypes")
    private final Collection first;
    @SuppressWarnings("rawtypes")
    private final Collection second;

    public <T> FunctionTypeException(Collection<T> first, Collection<T> second, Collection<T> delta) {
        super(String.format(messageFormatter, first, second, delta));
        this.first = first;
        this.second = second;

    }

    public static <T> FunctionTypeException of(Collection<T> first, Collection<T> second) {
        final var delta = first.stream()
                .filter(f -> ! second.contains(f))
                .collect(Collectors.toSet());
        return new FunctionTypeException(first, second, delta);
    }

    @SuppressWarnings("rawtypes")
    public Collection getFirst() {
        return first;
    }

    @SuppressWarnings("rawtypes")
    public Collection getSecond() {
        return second;
    }
}

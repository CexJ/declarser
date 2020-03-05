package kernel.parsers;

import kernel.tryapi.Try;

import java.util.function.Function;

public interface Parser<T,U> extends Function<T, Try<?>> {

    @Override
    Try<U> apply(T s);
}

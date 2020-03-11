package kernel;

import kernel.tryapi.Try;

public interface Declarser<I,K,V,O> {

    Try<O> parse(final I input);
}

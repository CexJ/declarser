package declarser;

import utils.tryapi.Try;

public interface Declarser<I,O> {

	Try<O> parse(I input);
}

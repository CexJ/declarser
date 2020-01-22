package declarser;

import java.util.List;

import utils.eitherapi.Either;

public interface Declarser<I,O> {

	Either<List<Exception>, O> parse(I input);
}

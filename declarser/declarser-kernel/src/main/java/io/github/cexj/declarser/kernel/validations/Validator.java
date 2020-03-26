package io.github.cexj.declarser.kernel.validations;

import java.util.Optional;
import java.util.function.Function;

public interface Validator<T> extends Function<T, Optional<? extends Exception>> {
}

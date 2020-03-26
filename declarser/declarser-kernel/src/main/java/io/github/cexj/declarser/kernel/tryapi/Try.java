package io.github.cexj.declarser.kernel.tryapi;

import io.github.cexj.declarser.kernel.validations.Validator;
import io.github.cexj.declarser.kernel.enums.Unit;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.cexj.declarser.kernel.enums.Unit.*;

public interface Try<T> {

	static <T> Try<T> success(
			final T value){
		return Success.of(value);
	}
	
	static <T> Try<T> fail(
			final Exception exception){
		return Failure.of(exception);
	}
	
	static <T> Try<T> call(
			final Callable<T> callable){
		try {
			return Success.of(callable.call());
		}catch(Exception exception) {
			return Failure.of(exception);
		}
	}

	static Try<Unit> run(
			final DangerousRunnable runnable){
		try {
			runnable.run();
			return Success.of(UNIT);
		}catch(Exception exception) {
			return Failure.of(exception);
		}
	}

	boolean isSuccess();
	boolean isFailure();
	Try<T> continueIf(final Validator<T> validator);
	Try<T> enrichException(final Function<Exception, ? extends Exception> enricher);
	<U> Try<U> map(final Function<T,? extends U> map);
	<U> Try<U> flatMap(final Function<T,Try<U>> map);
	Try<T> filter(final Predicate<T> predicate);
	Try<T> or(final Try<T> alternative);
	T getOrElse(final T defaultValue);
	T getValue();
	Exception getException();
	void ifPresent(final Consumer<T> consumer);
}

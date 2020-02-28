package utils.tryapi;

import utils.exceptions.DangerousExecutable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Try<T> {

	static <T> Try<T> success(T value){
		return Success.of(value);
	}
	
	static <T> Try<T> fail(Exception exception){
		return Failure.of(exception);
	}
	
	static <T> Try<T> go(DangerousExecutable<T> supplier){
		try {
			return Success.of(supplier.get());
		}catch(Exception exception) {
			return Failure.of(exception);
		}
	}

	boolean isSuccess();
	boolean isFailure();
	Try<T> continueIf(Function<T, Optional<? extends Exception>> validator);
	<U> Try<U> map(Function<T,? extends U> map);
	<U> Try<U> flatMap(Function<T,Try<U>> map);
	Try<T> filter(Predicate<T> predicate);
	Try<T> or(Try<T> alternative);
	T getOrElse(T defaultValue);
	T getValue();
	Exception getException();
	void ifPresent(Consumer<T> consumer);
}

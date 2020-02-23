package utils.tryapi;

import utils.exceptions.ExceptionalSupplier;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Try<T> {

	static <T> Try<T> success(T value){
		return Success.of(value);
	}
	
	static <T> Failure<T> fail(Exception exception){
		return Failure.of(exception);
	}
	
	static <T> Try<T> go(ExceptionalSupplier<T> supplier){
		try {
			return Success.of(supplier.get());
		}catch(Exception exception) {
			return Failure.of(exception);
		}
	}

	boolean isSuccess();
	boolean isFailure();
	public Try<T> continueIf(Function<T, Optional<? extends Exception>> validator);
	<U> Try<U> map(Function<T,? extends U> map);
	<U> Try<U> flatMap(Function<T,Try<U>> map);
	Try<T> filter(Predicate<T> predicate);
	T getOrElse(T defaultValue);
	T getValue();
	Exception getException();
}

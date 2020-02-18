package utils.tryapi;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public class Failure<T> implements Try<T> {

	private final Exception exception;
	
	private Failure(Exception exception) {
		this.exception = exception;
	}
	
	
	static <T> Failure<T> of(Exception exception) {
		return new Failure<>(exception);
	}


	@Override
	public boolean isSuccess() {
		return false;
	}


	@Override
	public boolean isFailure() {
		return true;
	}


	@Override
	public <U> Try<U> map(Function<T, ? extends U> map) {
		return of(exception);
	}


	@Override
	public <U> Try<U> flatMap(Function<T, Try<U>> map) {
		return of(exception);
	}


	@Override
	public Try<T> filter(Predicate<T> predicate) {
		return this;
	}


	@Override
	public T getOrElse(T defaultValue) {
		return defaultValue;
	}


	@Override
	public T getValue() {
		throw new NoSuchElementException();
	}


	@Override
	public Exception getException() {
		return exception;
	}


	
}

package utils.tryapi;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Success<T> implements Try<T> {

	private final T value;
	
	private Success(T value) {
		this.value = value;
	}
	
	static <T> Success<T> of(T value) {
		return new Success<>(value);
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public boolean isFailure() {
		return false;
	}

	@Override
	public Try<T> continueIf(Function<T, Optional<? extends Exception>> validator) {
		return validator.apply(value)
				.map(Try::<T>fail)
				.orElse(this);
	}

	@Override
	public <U> Try<U> map(Function<T, ? extends U> map) {
		try {
			return Success.of(map.apply(value));
		} catch(Exception ex) {
			return Failure.of(ex);
		}
	}

	@Override
	public <U> Try<U> flatMap(Function<T, Try<U>> map) {
		return map.apply(value);
	}

	@Override
	public Try<T> filter(Predicate<T> predicate) {
		if(predicate.test(value)) {
			return this;
		} else {
			return Failure.of(new Exception());
		}
	}

	@Override
	public T getOrElse(T defaultValue) {
		return value;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Exception getException() {
		throw new NoSuchElementException();
	}

	@Override
	public void ifPresent(Consumer<T> consumer) {
		consumer.accept(value);
	}

}

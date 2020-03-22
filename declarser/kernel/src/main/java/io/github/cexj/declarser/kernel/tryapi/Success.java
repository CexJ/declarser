package io.github.cexj.declarser.kernel.tryapi;

import io.github.cexj.declarser.kernel.validations.Validator;
import io.github.cexj.declarser.kernel.exceptions.FilterFailException;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

final class Success<T> implements Try<T> {

	private final T value;
	
	private Success(
			final T value) {
		this.value = value;
	}
	
	static <T> Success<T> of(
			final T value) {
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
	public Try<T> continueIf(
			final Validator<T> validator) {
		return validator.apply(value)
				.map(Try::<T>fail)
				.orElse(this);
	}

	@Override
	public Try<T> enrichException(
			final Function<Exception, ? extends Exception> enricher) {
		return this;
	}

	@Override
	public <U> Try<U> map(
			final Function<T, ? extends U> map) {
		try {
			return Success.of(map.apply(value));
		} catch(Exception ex) {
			return Failure.of(ex);
		}
	}

	@Override
	public <U> Try<U> flatMap(
			final Function<T, Try<U>> map) {
		return map.apply(value);
	}

	@Override
	public Try<T> filter(
			final Predicate<T> predicate) {
		if(predicate.test(value)) {
			return this;
		} else {
			return Failure.of(new FilterFailException());
		}
	}

	@Override
	public Try<T> or(
			final Try<T> alternative) {
		return this;
	}

	@Override
	public T getOrElse(
			final T defaultValue) {
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
	public void ifPresent(
			final Consumer<T> consumer) {
		consumer.accept(value);
	}

}

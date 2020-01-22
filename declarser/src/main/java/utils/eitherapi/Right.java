package utils.eitherapi;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Right<L,R> implements Either<L,R> {

	private final R value;
	
	public Right(R value) {
		super();
		this.value = value;
	}

	static <L, R> Right<L, R> of(R value) {
		return new Right<L, R>(value);
	}

	@Override
	public boolean isLeft() {
		return false;
	}

	@Override
	public boolean isRight() {
		return true;
	}

	@Override
	public <U> Either<U, R> mapLeft(Function<L, ? extends U> map) {
		return of(value);
	}

	@Override
	public <V> Either<L, V> mapRight(Function<R, ? extends V> map) {
		return of(map.apply(value));
	}

	@Override
	public <U> Either<U, R> flatMapLeft(Function<L, Either<U, R>> map) {
		return of(value);
	}

	@Override
	public <V> Either<L, V> flatMapRight(Function<R, Either<L, V>> map) {
		return map.apply(value);
	}

	@Override
	public L leftOrElse(L defaultValue) {
		return defaultValue;
	}

	@Override
	public R rightOrElse(R defaultValue) {
		return value;
	}

	@Override
	public L leftOrThrow(Supplier<? extends Exception> supplier) throws Exception {
		throw supplier.get();
	}

	@Override
	public R rightOrThrow(Supplier<? extends Exception> supplier) throws Exception {
		return value;
	}

	@Override
	public L getLeft() {
		throw new NoSuchElementException();
	}

	@Override
	public R getRight() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Right other = (Right) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Right [" + value + "]";
	}
	
	
	
}

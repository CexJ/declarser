package utils.tryapi;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Try<T> {

	static <T> Try<T> success(T value){
		return Success.of(value);
	}
	
	static <E extends Exception> Failure<?,E> fail(E exception){
		return Failure.of(exception);
	}
	
	static <T> Try<T> go(Supplier<T> supplier){
		try {
			return Success.of(supplier.get());
		}catch(Exception exception) {
			return Failure.of(exception);
		}
	}
	
	boolean isSuccess();
	boolean isFailure();
	<U> Try<U> map(Function<T,? extends U> map);
	<U> Try<U> flatMap(Function<T,Try<U>> map);
	Try<T> filter(Predicate<T> predicate);
	T getOrElse(T defaultValue);
	T getValue();
	Exception getException();
}

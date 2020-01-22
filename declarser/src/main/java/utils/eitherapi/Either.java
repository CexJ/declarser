package utils.eitherapi;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Either<L, R> {

	static <L,R> Left<L,R> left(L value){
		return Left.of(value);
	}
	
	static <L,R> Right<L,R> right(R value){
		return Right.of(value);
	}
	
	boolean isLeft();
	boolean isRight();
	<U> Either<U, R> mapLeft(Function<L,? extends U> map);
	<V> Either<L, V> mapRight(Function<R,? extends V> map);
	<U> Either<U,R> flatMapLeft(Function<L,Either<U, R>> map);
	<V> Either<L,V> flatMapRight(Function<R,Either<L,V>> map);
	L leftOrElse(L defaultValue);
	R rightOrElse(R defaultValue);
	L leftOrThrow(Supplier<? extends Exception> supplier) throws Exception;
	R rightOrThrow(Supplier<? extends Exception> supplier) throws Exception;	
	L getLeft();
	R getRight();
}

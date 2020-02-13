package utils.exceptions;

public interface ExceptionalSupplier<T> {

    T get() throws Exception;
}

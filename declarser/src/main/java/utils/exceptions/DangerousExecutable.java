package utils.exceptions;

public interface DangerousExecutable<T> {

    T get() throws Exception;
}

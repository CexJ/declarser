package kernel.tryapi;

public interface DangerousSupplier<T> {

    T get() throws Exception;
}

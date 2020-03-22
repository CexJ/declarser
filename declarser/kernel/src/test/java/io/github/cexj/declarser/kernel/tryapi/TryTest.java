package io.github.cexj.declarser.kernel.tryapi;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TryTest {

    private static final class TypeT{}

    /*
     * GIVEN a value T of type TypeT
     *  AND a Callable C that produce T
     * WHEN the method go is invoked with C
     * THEN the result is a success
     *  AND the value is T
     */
    @Test
    public void go_without_exception_is_success(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Callable C that produce T
        final Callable<TypeT> callable = () -> value;
        // WHEN the method go is invoked
        final var result =  Try.call(callable);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN an Exception E
     *  AND a Callable C that thrown E
     * WHEN the method go is invoked with C
     * THEN the result is a success
     *  AND the exception is E
     */
    @Test
    public void go_with_exception_is_failure(){
        // GIVEN a value T of type TypeT
        final var exception = new Exception("Callable exception");
        // AND a Callable C that produce T
        final Callable<TypeT> callable = () -> {throw exception;};
        // WHEN the method go is invoked with C
        final var result =  Try.call(callable);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }
}

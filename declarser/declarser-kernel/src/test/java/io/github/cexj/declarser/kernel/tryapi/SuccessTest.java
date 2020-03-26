package io.github.cexj.declarser.kernel.tryapi;

import io.github.cexj.declarser.kernel.validations.Validator;
import io.github.cexj.declarser.kernel.exceptions.FilterFailException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SuccessTest {

    private static final class TypeT{}
    private static final class TypeU{}

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     * WHEN the method isSuccess is invoked
     * THEN the result is TRUE
     */
    @Test
    public void success_is_success(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // WHEN the method isSuccess is invoked
        final var result = success.isSuccess();
        // THEN the result is TRUE
        assertTrue(result);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     * WHEN the method isFailure is invoked
     * THEN the result is FALSE
     */
    @Test
    public void success_is_not_failure(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // WHEN the method isFailure is invoked
        final var result = success.isFailure();
        // THEN the result is FALSE
        assertFalse(result);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a validator V valid for T
     * WHEN the method continueIf is invoked with V
     * THEN the result is a success
     *  AND the value is T
     */
    @Test
    public void success_if_valid_continue_with_success(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a validator V that pass with T
        final Validator<TypeT> validator = t -> value.equals(t) ? Optional.empty() : Optional.of(new Exception());
        // WHEN the method continueIf is invoked with V
        final var result = success.continueIf(validator);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND an exception E
     *  AND a validator V that fail for T with E
     * WHEN the method continueIf is invoked with V
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void success_if_invalid_continue_with_failure(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND an exception E
        final var exception = new Exception("Validation exception");
        // AND a validator V that fail for T with E
        final Validator<TypeT> validator = t -> value.equals(t) ? Optional.of(exception) : Optional.empty();
        // WHEN the method continueIf is invoked with V
        final var result = success.continueIf(validator);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     * WHEN the method enrichException is invoked
     * THEN the result is a success
     *  AND the value is T
     */
    @Test
    public void success_does_not_enrich_exception(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // WHEN the method enrichException is invoked
        final var result = success.enrichException(ex -> new Exception());
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a value U of Type TypeU
     *  AND a function F that map T -> U
     * WHEN the method map is invoked with F
     * THEN the result is a success
     *  AND the value is U
     */
    @Test
    public void success_map_to_success(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a value U of Type TypeU
        final var valueU = new TypeU();
        // AND a function M that map T -> U
        final Function<TypeT, TypeU> function = t -> valueU;
        // WHEN the method map is invoked with F
        final var result = success.map(function);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is U
        final var valueResult = result.getValue();
        assertEquals(valueResult, valueU);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND an RuntimeException E
     *  AND a function F that throws E
     * WHEN the method map is invoked with F
     * THEN the result is a failure
     *  AND the Exception is E
     */
    @Test
    public void success_map_runtime_exception_to_failure(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND an RuntimeException E
        final var exception = new RuntimeException("Map exception");
        // a function F that throws E
        final Function<TypeT, TypeU> function = t -> {throw exception;};
        // WHEN the method map is invoked with F
        final var result = success.map(function);
        // THEN the result is a success
        assertTrue(result.isFailure());
        // AND the value is U
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a value U of Type TypeU
     *  AND a function F that map T -> Success(U)
     * WHEN the method flatMap is invoked with F
     * THEN the result is a success
     *  AND the value is U
     */
    @Test
    public void success_flatmap_success_to_success(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a value U of Type TypeU
        final var valueU = new TypeU();
        // AND a function M that map T -> U
        final Function<TypeT, Try<TypeU>> function = t -> Try.success(valueU);
        // WHEN the method map is invoked with F
        final var result = success.flatMap(function);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is U
        final var valueResult = result.getValue();
        assertEquals(valueResult, valueU);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND an exception E
     *  AND a function F that map T -> Failure(E)
     * WHEN the method flatMap is invoked with F
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void success_flatmap_failure_to_failure(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a value U of Type TypeU
        final var exception = new Exception();
        // AND a function M that map T -> U
        final Function<TypeT, Try<TypeU>> function = t -> Try.fail(exception);
        // WHEN the method map is invoked with F
        final var result = success.flatMap(function);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a predicate P that map T -> TRUE
     * WHEN the method filter is invoked with P
     * THEN the result is a success
     *  AND the value is T
     */
    @Test
    public void success_filter_valid_to_success(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a predicate P that map T -> TRUE
        final Predicate<TypeT> predicate = value::equals;
        // WHEN the method filter is invoked with P
        final var result = success.filter(predicate);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a predicate P that map T -> FALSE
     * WHEN the method filter is invoked with P
     * THEN the result is a failure
     *  AND the exception is of the type FilterFailException
     */
    @Test
    public void success_filter_invalid_to_failure(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a predicate P that map T -> TRUE
        final Predicate<TypeT> predicate = t -> !value.equals(t);
        // WHEN the method filter is invoked with P
        final var result = success.filter(predicate);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of the type FilterFailException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult.getClass(), FilterFailException.class);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a value T2 of type TypeT
     *  AND a Success(T2) S2
     * WHEN the method or is invoked with S2
     * THEN the result is a success
     *  AND the value is T
     */
    @Test
    public void success_or_success_does_not_change(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a value T2 of type TypeT
        final var value2 = new TypeT();
        // AND a Success(T2) S2
        final var success2 = Try.success(value2);
        // WHEN the method or is invoked with S2
        final var result = success.or(success2);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND an exception E
     *  AND a Failure(E) F
     * WHEN the method or is invoked with F
     * THEN the result is a failure
     *  AND the exception is of the type FilterFailException
     */
    @Test
    public void success_or_failure_does_not_change(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        //  AND an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final Try<TypeT> failure = Try.fail(exception);
        // WHEN the method or is invoked with S2
        final var result = success.or(failure);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a value T2 of type TypeT
     * WHEN the method getOrElse is invoked with T2
     * THEN the result is T
     */
    @Test
    public void success_getOrElse_the_value(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a value T2 of type TypeT
        final var value2 = new TypeT();
        // WHEN the method getOrElse is invoked with T2
        final var result = success.getOrElse(value2);
        // THEN  the value is T
        assertEquals(result, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     * WHEN the method getValue is invoked
     * THEN the result is T
     */
    @Test
    public void success_getValue_the_value(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // WHEN the method getOrElse is invoked with T2
        final var result = success.getValue();
        // THEN  the value is T
        assertEquals(result, value);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     * WHEN the method getException is invoked
     * THEN an exception of type NoSuchElementException is thrown
     */
    @Test
    public void success_getException_throws_NoSuchElementException(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // WHEN the method getException is invoked
        // THEN an exception of type NoSuchElementException is thrown
        assertThrows(NoSuchElementException.class, success::getException);
    }

    /*
     * GIVEN a value T of type TypeT
     *  AND a Success(T) S
     *  AND a Consumer(T) C
     * WHEN the method ifPresent is invoked with C
     * THEN C is executed with T
     */
    @Test
    public void success_if_present_executes_consumer(){
        // GIVEN a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // AND a Consumer(T) C
        @SuppressWarnings("unchecked") final Consumer<TypeT> consumer = Mockito.mock(Consumer.class);
        // WHEN the method ifPresent is invoked with C
        success.ifPresent(consumer);
        // THEN C is executed with T
        verify(consumer, times(1)).accept(value);
    }

}

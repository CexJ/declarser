package kernel.tryapi;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FailureTest {

    private static final class TypeT{}

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method isSuccess is invoked
     * THEN the result is FALSE
     */
    @Test
    public void failure_is_not_success(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method isSuccess is invoked
        final var result = failure.isSuccess();
        // THEN the result is TRUE
        assertFalse(result);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method isFailure is invoked
     * THEN the result is TRUE
     */
    @Test
    public void failure_is_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method isFailure is invoked
        final var result = failure.isFailure();
        // THEN the result is FALSE
        assertTrue(result);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method continueIf
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void failure_continue_with_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method continueIf is invoked
        final var result = failure.continueIf(t -> Optional.empty());
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     *  AND an exception E2
     *  AND a function F that map E -> E2
     * WHEN the method enrichException is invoked with F
     * THEN the result is a failure
     *  AND the exception is E2
     */
    @Test
    public void success_does_not_enrich_exception(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // AND an exception E2
        final var exception2 = new Exception();
        // AND a function F that map E -> E2
        final UnaryOperator<Exception> function = ex -> exception.equals(ex) ? exception2 : ex;
        // WHEN the method enrichException is invoked
        final var result = failure.enrichException(function);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E2
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception2);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method map is invoked
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void failure_map_to_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method map is invoked with F
        final var result = failure.map(t -> new Object());
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method flatMap is invoked
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void failure_flatmap_to_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method map is invoked
        final var result = failure.flatMap(Try::success);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the value is U
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method filter is invoked
     * THEN the result is a failure
     *  AND the exception is E
     */
    @Test
    public void failure_filter_to_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method filter is invoked
        final var result = failure.filter(t -> true);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is of the type FilterFailException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     *  AND a value T of type TypeT
     *  AND a Success(T) S
     * WHEN the method or is invoked with S
     * THEN the result is a success
     *  AND the value is T
     */
    @Test
    public void failure_or_success_is_success(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final Try<TypeT> failure = Try.fail(exception);
        // AND a value T of type TypeT
        final var value = new TypeT();
        // AND a Success(T) S
        final var success = Try.success(value);
        // WHEN the method or is invoked with S
        final var result = failure.or(success);
        // THEN the result is a success
        assertTrue(result.isSuccess());
        // AND the value is T
        final var valueResult = result.getValue();
        assertEquals(valueResult, value);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     *  AND an exception E2
     *  AND a Failure(E2) F2
     * WHEN the method or is invoked with F2
     * THEN the result is a failure
     *  AND the exception is E2
     */
    @Test
    public void failure_or_failure_is_failure(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final Try<TypeT> failure = Try.fail(exception);
        //  AND an exception E2
        final var exception2 = new Exception();
        // AND a Failure(E2) F2
        final Try<TypeT> failure2 = Try.fail(exception2);
        // WHEN the method or is invoked with F2
        final var result = failure.or(failure2);
        // THEN the result is a failure
        assertTrue(result.isFailure());
        // AND the exception is E2
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult, exception2);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     *  AND a value T of type TypeT
     * WHEN the method getOrElse is invoked with T
     * THEN the result is T
     */
    @Test
    public void failure_getOrElse_the_default(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // AND a value T of type TypeT
        final var value = new TypeT();
        // WHEN the method getOrElse is invoked with T
        final var result = failure.getOrElse(value);
        // THEN  the value is T
        assertEquals(result, value);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method getValue is invoked
     * THEN an exception of type NoSuchElementException is thrown
     */
    @Test
    public void failure_getValue_throws_NoSuchElementException(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method getOrElse is invoked with T2
        // THEN  the value is T
        assertThrows(NoSuchElementException.class, failure::getValue);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     * WHEN the method getException is invoked
     * THEN the result is E
     */
    @Test
    public void success_getException_throws_NoSuchElementException(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final var failure = Try.fail(exception);
        // WHEN the method getException is invoked
        final var result = failure.getException();
        // THEN the result is E
        assertEquals(result, exception);
    }

    /*
     * GIVEN an exception E
     *  AND a Failure(E) F
     *  AND a Consumer(T) C
     * WHEN the method ifPresent is invoked with C
     * THEN C is not executed
     */
    @Test
    public void success_if_present_executes_consumer(){
        // GIVEN an exception E
        final var exception = new Exception();
        // AND a Failure(E) F
        final Try<TypeT> failure = Try.fail(exception);
        // AND a Consumer(T) C
        @SuppressWarnings("unchecked") final Consumer<TypeT> consumer = Mockito.mock(Consumer.class);
        // WHEN the method ifPresent is invoked with C
        failure.ifPresent(consumer);
        // THEN C is executed with T
        verify(consumer, never()).accept(Mockito.any());
    }

}

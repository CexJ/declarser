package kernel.stages.stage02_totypedmap;

import kernel.conf.ParallelizationStrategyEnum;
import org.junit.jupiter.api.Test;
import utils.exceptions.MissingFieldFunctionException;
import utils.exceptions.TypingFieldException;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToTypedMapTest {

    private static final class TypeK{}
    private TypeK k = new TypeK();
    private static final class TypeV{}
    private TypeV v = new TypeV();
    private static final class TypeT{}

    /*
     * GIVEN a map M from TypeK to TypeV with an Entry (K, V)
     *  AND a value T of TypeT
     *  AND a map FM from TypeK to Function: TypeV -> Try<?> with an Entry (K, F -> Success(T))
     *  AND a ToTypedMap created with FM
     * WHEN the typing method is invoked with M
     * THEN the result is a map M that contains an entry (K, Success)
     *  AND the value is T
     */
    @Test
    public void typing_a_valid_field_return_a_map_with_a_success_entry(){
        // GIVEN a map M from TypeK to TypeV with an entry (K, V)
        final var inputMap = new HashMap<TypeK,TypeV>();
        inputMap.put(k, v);
        // AND a value T of TypeT
        final var t = new TypeT();
        // AND a map FM from TypeK to Function: TypeV -> Try<?> with an Entry (K, F -> Success(T))
        final var functionMap = new HashMap<TypeK, Function<TypeV, Try<?>>>();
        functionMap.put(k, v -> Try.success(t));
        // AND a ToTypedMap created with FM
        final var toTypedMap = ToTypedMapImpl.of(functionMap, ParallelizationStrategyEnum.SEQUENTIAL);
        // WHEN the typing method is invoked with M
        final var result = toTypedMap.typing(inputMap);
        // THEN the result is a map M that contains an entry (K, Success)
        final var resultK = result.get(k);
        assertTrue(resultK.isSuccess());
        // AND the value is T
        assertEquals(resultK.getValue(), t);
    }


    /*
     * GIVEN a map M from TypeK to TypeV with an Entry (K, V)
     *  AND an Exception E
     *  AND a map FM from TypeK to Function: TypeV -> Try<?> with an Entry (K, F -> Failure(E))
     *  AND a ToTypedMap created with FM
     * WHEN the typing method is invoked with M
     * THEN the result is a map M that contains an entry (K, Failure)
     *  AND the Exception is of the type TypingFieldException
     *  AND the cause is E
     *  AND the message is formatted with K, V, and E
     */
    @Test
    public void typing_invalid_field_return_a_map_with_a_failure_entry(){
        // GIVEN a map M from TypeK to TypeV with an entry (K, V)
        final var inputMap = new HashMap<TypeK,TypeV>();
        inputMap.put(k, v);
        // AND an Exception E
        final var exception = new Exception("Typing exception");
        // AND a map FM from TypeK to Function: TypeV -> Try<?> with an entry (K, V -> Failure(E))
        final var functionMap = new HashMap<TypeK, Function<TypeV, Try<?>>>();
        functionMap.put(k, v -> Try.fail(exception));
        // AND a ToTypedMap created with FM
        final var toTypedMap = ToTypedMapImpl.of(functionMap, ParallelizationStrategyEnum.SEQUENTIAL);
        // WHEN the typing method is invoked with M
        final var result = toTypedMap.typing(inputMap);
        // THEN the result is a map M that contains an entry (K, Failure)
        final var resultK = result.get(k);
        assertTrue(resultK.isFailure());
        // AND the Exception is of the type TypingFieldException
        final var exceptionK = resultK.getException();
        assertEquals(exceptionK.getClass(), TypingFieldException.class);
        // AND the cause is E
        assertEquals(exceptionK.getCause(), exception);
        // AND the message is formatted with K, V, and E
        assertEquals(exceptionK.getMessage(), String.format(TypingFieldException.messageFormatter, exception.toString(), k.toString(), v.toString()));
    }

    /*
     * GIVEN a map M from TypeK to TypeV with an Entry (K, V)
     *  AND a map FM from TypeK to Function: TypeV -> Try<?> without an Entry for K
     *  AND a ToTypedMap created with FM
     * WHEN the typing method is invoked with M
     * THEN the result is a map M that contains an entry (K, Failure)
     *  AND the Exception is of the type TypingFieldException
     *  AND the cause is of type MissingFieldFunctionException
     *  AND the message of the cause is formatted with K
     */
    @Test
    public void missing_field_function_return_a_map_with_a_failure_entry(){
        // GIVEN a map M from TypeK to TypeV with an entry (k, v)
        final var inputMap = new HashMap<TypeK,TypeV>();
        inputMap.put(k, v);
        // AND a map FM from TypeK to Function: TypeV -> Try<?> without an Entry for K
        final var functionMap = new HashMap<TypeK, Function<TypeV, Try<?>>>();
        // AND a ToTypedMap created with FM
        final var toTypedMap = ToTypedMapImpl.of(functionMap, ParallelizationStrategyEnum.SEQUENTIAL);
        // WHEN the typing method is invoked with M
        final var result = toTypedMap.typing(inputMap);
        // THEN the result is a map M that contains an entry (K, Failure)
        final var resultK = result.get(k);
        assertTrue(resultK.isFailure());
        // AND the Exception is of the type TypingFieldException
        final var exceptionK = resultK.getException();
        assertEquals(exceptionK.getClass(), TypingFieldException.class);
        // AND the cause is of type MissingFieldFunctionException
        assertEquals(exceptionK.getCause().getClass(), MissingFieldFunctionException.class);
        // AND the message of the cause is formatted with K
        assertEquals(exceptionK.getCause().getMessage(), String.format(MissingFieldFunctionException.messageFormatter, k.toString()));
    }



}



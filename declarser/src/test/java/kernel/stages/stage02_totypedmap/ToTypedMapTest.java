package kernel.stages.stage02_totypedmap;

import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage01_tomap.ToMap;
import kernel.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.exceptions.MappingInputValidationException;
import utils.exceptions.TypingFieldException;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToTypedMapTest {

    private final class TypeK{}
    private TypeK k = new TypeK();
    private final class TypeV{}
    private TypeV v = new TypeV();

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
        // GIVEN a map M from TypeK to TypeV with an entry (k, v)
        final var inputMap = new HashMap<TypeK,TypeV>();
        inputMap.put(k, v);
        // AND an Exception E
        final var exception = new Exception("Typing exception");
        // AND a map FM from TypeK to Function: TypeV -> Try<?> with an entry (k, v -> Failure(E))
        final var functionMap = new HashMap<TypeK, Function<TypeV, Try<?>>>();
        functionMap.put(k, v -> Try.fail(exception));
        // AND a ToTypedMap created with FM
        final var toTypedMap = ToTypedMap.of(functionMap, ParallelizationStrategyEnum.SEQUENTIAL);
        // WHEN the typing method is invoked with M
        final var result = toTypedMap.typing(inputMap);
        // THEN the result is a map M that contains an entry (k, Failure)
        final var resultK = result.get(k);
        assertTrue(resultK.isFailure());
        // AND the Exception is of the type TypingFieldException
        final var exceptionK = resultK.getException();
        assertEquals(exceptionK.getClass(), TypingFieldException.class);
        // AND the cause is E
        assertEquals(exceptionK.getCause(), exception);
        // AND the message is formatted with k, v, and E
        assertEquals(exceptionK.getMessage(), String.format(TypingFieldException.messageFormatter, exception.toString(), k.toString(), v.toString()));
    }

    /*
     * GIVEN a map M from TypeK to TypeV with an Entry (K, V)
     *  AND a map FM from TypeK to Function: TypeV -> Try<?> without an Entry for K
     *  AND a ToTypedMap created with FM
     * WHEN the typing method is invoked with M
     * THEN the result is a map M that contains an entry (K, Failure)
     *  AND the Exception is of the type MissingFieldFunctionException
     *  AND the cause is E
     *  AND the message is formatted with K, V, and E
     */
    @Test
    public void missing_field_function_return_a_map_with_a_failure_entry(){
        // GIVEN a map M from TypeK to TypeV with an entry (k, v)
        final var inputMap = new HashMap<TypeK,TypeV>();
        inputMap.put(k, v);
        // AND an Exception E
        final var exception = new Exception("Typing exception");
        // AND a map FM from TypeK to Function: TypeV -> Try<?> with an entry (k, v -> Failure(E))
        final var functionMap = new HashMap<TypeK, Function<TypeV, Try<?>>>();
        functionMap.put(k, v -> Try.fail(exception));
        // AND a ToTypedMap created with FM
        final var toTypedMap = ToTypedMap.of(functionMap, ParallelizationStrategyEnum.SEQUENTIAL);
        // WHEN the typing method is invoked with M
        final var result = toTypedMap.typing(inputMap);
        // THEN the result is a map M that contains an entry (k, Failure)
        final var resultK = result.get(k);
        assertTrue(resultK.isFailure());
        // AND the Exception is of the type TypingFieldException
        final var exceptionK = resultK.getException();
        assertEquals(exceptionK.getClass(), TypingFieldException.class);
        // AND the cause is E
        assertEquals(exceptionK.getCause(), exception);
        // AND the message is formatted with k, v, and E
        assertEquals(exceptionK.getMessage(), String.format(TypingFieldException.messageFormatter, exception.toString(), k.toString(), v.toString()));
    }



}



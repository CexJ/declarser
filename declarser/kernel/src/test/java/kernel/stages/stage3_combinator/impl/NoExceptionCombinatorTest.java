package kernel.stages.stage3_combinator.impl;

import kernel.enums.ParallelizationStrategyEnum;
import kernel.stages.stage03_combinator.exceptions.CombiningException;
import kernel.exceptions.GroupedException;
import kernel.stages.stage03_combinator.impl.NoExceptionCombinator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import kernel.tryapi.Try;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoExceptionCombinatorTest {

    private static final class TypeK{}
    private static final class TypeT{}

    /*
     * GIVEN a map S containing entry of type (TypeK, Success)
     * WHEN the combine method is invoked with S
     * THEN the result is a Success
     *  AND value is a map with all and only the S keys
     *  AND the values are the same of S
     */
    @ParameterizedTest
    @ValueSource(strings = { "SEQUENTIAL", "PARALLEL"})
    public void when_combine_success_return_success(String name){
        final NoExceptionCombinator<TypeK> allExceptionCombinator = NoExceptionCombinator.of(ParallelizationStrategyEnum.valueOf(name));

        // GIVEN a map S containing entry of type (TypeK, Success)
        final var sucessMap = new HashMap<TypeK, Try<?>>();
        sucessMap.put(new TypeK(), Try.success(new TypeT()));
        // WHEN the combine method is invoked with S
        final var result = allExceptionCombinator.combining(sucessMap);
        // THEN the result is a Success
        assertTrue(result.isSuccess());
        //  AND value is a map with all and only the S keys
        final var valueResult = result.getValue();
        final var keyResultSet = valueResult.keySet();
        final var keySucess = sucessMap.keySet();
        assertEquals(keyResultSet, keySucess);
        //  AND the values are the same of S
        valueResult.forEach((k,v) ->
                assertEquals(v, sucessMap.get(k).getValue()));
    }


    /*
     * GIVEN a map S containing entry of type (TypeK, Success)
     *  AND a map F containing entry of type (TypeK, Failure) disjointed from S
     *  LET M be the union of the S and F
     * WHEN the combine method is invoked with M
     * THEN the result is a Failure
     *  AND Exception is of the type GroupedException
     *  AND all exceptions are of the type CombiningException
     *  AND all exceptions have a key of F and for all key of F there is an exception
     *  AND the exceptions are the same of F
     */
    @ParameterizedTest
    @ValueSource(strings = { "SEQUENTIAL", "PARALLEL"})
    public void when_combine_success_and_failure_return_failure(String name){
        final NoExceptionCombinator<TypeK> allExceptionCombinator = NoExceptionCombinator.of(ParallelizationStrategyEnum.valueOf(name));

        // GIVEN a map S containing entry of type (TypeK, Success)
        final var sucessMap = new HashMap<TypeK, Try<?>>();
        sucessMap.put(new TypeK(), Try.success(new TypeT()));
        //  AND a map F containing entry of type (TypeK, Failure) disjointed from S
        final var failureMap = new HashMap<TypeK, Try<?>>();
        failureMap.put(new TypeK(), Try.fail(new Exception()));
        //  LET M be the union of the S and F
        final HashMap<TypeK, Try<?>> map = new HashMap<>();
        map.putAll(sucessMap);
        map.putAll(failureMap);
        // WHEN the combine method is invoked with M
        final var result = allExceptionCombinator.combining(map);
        // THEN the result is a Failure
        assertTrue(result.isFailure());
        //  AND Exception is of the type GroupedException
        final var exceptionResult = result.getException();
        assertEquals(exceptionResult.getClass(), GroupedException.class);
        //  AND all exceptions are of the type CombiningException
        final var exceptionList = ((GroupedException) exceptionResult).getExceptions();
        exceptionList.forEach( ex ->
                assertEquals(ex.getClass(), CombiningException.class));
        //  AND all exceptions have a key of F and for all key of F there is an exception
        final var combiningExceptionList = exceptionList.stream().map(ex -> ((CombiningException) ex)).collect(Collectors.toList());
        final var resultKeySet = combiningExceptionList.stream().map(CombiningException::getKey).collect(Collectors.toSet());
        final var failtKeySet = new HashSet<>(failureMap.keySet());
        assertEquals(resultKeySet, failtKeySet);
        //  AND the exceptions are the same of F
        //noinspection SuspiciousMethodCalls
        combiningExceptionList.forEach( ex ->
                assertEquals(ex.getCause(), failureMap.get(ex.getKey()).getException())
        );
    }
}

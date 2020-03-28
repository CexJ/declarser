package io.github.cexj.declarser.kernel.stages.stage3_combinator.impl;

import io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum;
import io.github.cexj.declarser.kernel.stages.stage03_combinator.Combinator;
import io.github.cexj.declarser.kernel.tryapi.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllExceptionCombinatorTest {

    private static final class TypeK{}
    private static final class TypeT{}


    /*
     * GIVEN a map S containing entry of type (TypeK, Success)
     *  AND a map F containing entry of type (TypeK, Failure) disjointed from S
     *  LET M be the union of the S and F
     * WHEN the combine method is invoked with M
     * THEN the result is a Success
     *  AND the value contains all and only the S keys
     *  AND foreach key the value is the value corresponding to S map
     */
    @ParameterizedTest
    @ValueSource(strings = { "SEQUENTIAL", "PARALLEL"})
    public void when_combine_success_and_failure_return_success(String name){
        final Combinator<TypeK> allExceptionCombinator = Combinator.allException(ParallelizationStrategyEnum.valueOf(name));

        // GIVEN a map S containing entry of type (TypeK, Success)
        final var sucessMap = new HashMap<TypeK, Try<Object>>();
        sucessMap.put(new TypeK(), Try.success(new TypeT()));
        // AND a map F containing entry of type (TypeK, Failure) disjointed from S
        final var failureMap = new HashMap<TypeK, Try<Object>>();
        failureMap.put(new TypeK(), Try.fail(new Exception()));
        // LET M be the union of the S and F
        final HashMap<TypeK, Try<?>> map = new HashMap<>();
        map.putAll(sucessMap);
        map.putAll(failureMap);
        // WHEN the combine method is invoked with M
        final var result = allExceptionCombinator.combining(map);
        // THEN the result is a Success
        Assertions.assertTrue(result.isSuccess());
        // AND the value contains all and only the S keys
        final var valueResult = result.getValue();
        final var keyResult = valueResult.keySet();
        final var keySuccess = sucessMap.keySet();
        assertEquals(keyResult, keySuccess);
        // AND foreach key the value is the value corresponding to S map
        keyResult.forEach( k ->
                Assertions.assertEquals(valueResult.get(k), sucessMap.get(k).getValue())
        );
    }
}

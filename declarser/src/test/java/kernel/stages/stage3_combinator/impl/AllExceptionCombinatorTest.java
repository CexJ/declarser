package kernel.stages.stage3_combinator.impl;

import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage03_combinator.impl.AllExceptionCombinator;
import org.junit.jupiter.api.Test;
import utils.tryapi.Try;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllExceptionCombinatorTest {

    private static final class TypeK{}
    private static final class TypeT{}
    private AllExceptionCombinator<TypeK> allExceptionCombinator =
            AllExceptionCombinator.of(ParallelizationStrategyEnum.SEQUENTIAL);


    /*
     * GIVEN a map S containing entry of type (TypeK, Success)
     *  AND a map F containing entry of type (TypeK, Failure) disjointed from S
     *  LET M be the union of the S and F
     * WHEN the combine method is invoked with M
     * THEN the result is a Success
     *  AND the value contains all and only the S keys
     *  AND foreach key the value is the value corresponding to S map
     */
    @Test
    public void when_combine_success_and_failure_return_success(){
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
        assertTrue(result.isSuccess());
        // AND the value contains all and only the S keys
        final var valueResult = result.getValue();
        final var keyResult = valueResult.keySet();
        final var keySuccess = sucessMap.keySet();
        assertEquals(keyResult, keySuccess);
        // AND foreach key the value is the value corresponding to S map
        keyResult.forEach( k ->
                assertEquals(valueResult.get(k), sucessMap.get(k).getValue())
        );
    }
}

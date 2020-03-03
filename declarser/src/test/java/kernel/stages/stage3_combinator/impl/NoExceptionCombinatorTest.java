package kernel.stages.stage3_combinator.impl;

import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage03_combinator.impl.NoExceptionCombinator;
import org.junit.jupiter.api.Test;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoExceptionCombinatorTest {

    private static final class TypeK{}
    private static final class TypeT{}
    private NoExceptionCombinator<TypeK> allExceptionCombinator =
            NoExceptionCombinator.of(ParallelizationStrategyEnum.SEQUENTIAL);


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
    @Test
    public void when_combine_success_and_failure_return_failure(){
        // GIVEN a map S containing entry of type (TypeK, Success)
        //  AND a map F containing entry of type (TypeK, Failure) disjointed from S
        //  LET M be the union of the S and F
        // WHEN the combine method is invoked with M
        // THEN the result is a Failure
        //  AND Exception is of the type GroupedException
        //  AND all exceptions are of the type CombiningException
        //  AND all exceptions have a key of F and for all key of F there is an exception
        //  AND the exceptions are the same of F
    }
}

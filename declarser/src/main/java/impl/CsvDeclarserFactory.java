package impl;

import impl.stages.annotations.type.CsvType;
import impl.stages.stage01_tomap.destructors.CsvDestructor;
import impl.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import impl.stages.stage03_combinator.combinators.NoExceptionCombinator;
import impl.stages.stage04_toobject.CsvFieldMapFactory;
import impl.stages.stage04_toobject.restructors.ReflectionRestructor;
import kernel.Declarser;
import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage01_tomap.ToMap;
import kernel.stages.stage02_totypedmap.ToTypedMap;
import kernel.stages.stage04_toobject.ToObject;
import utils.tryapi.Try;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


public class CsvDeclarserFactory<O> {

    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final Supplier<O> outputSupplier;
    private final CsvFunctionMapFactory mapFunctionFactory;
    private final CsvFieldMapFactory mapFieldFactory;

    private CsvDeclarserFactory(final ParallelizationStrategyEnum parallelizationStrategy,
                                final Supplier<O> outputSupplier,
                                final Map<Class<? extends Function<String, Try<?>>>,
                                        Function<String[], Function<String, Try<?>>>> customConstructorMap,
                                final CsvFieldMapFactory mapFieldFactory) {
        this.parallelizationStrategy = parallelizationStrategy;
        this.outputSupplier = outputSupplier;
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(customConstructorMap);
        this.mapFieldFactory = mapFieldFactory;
    }

    public Try<Declarser<String, Integer, String, O>> apply(final Class<O> clazz, final String[] params) {

        final Try<CsvType> typeAnn = null;
        final Try<String> cellSeparator = typeAnn.map(CsvType::cellSeparator);
        final Try<Function<String, Optional<? extends Exception>>> preValidator = null;
        final Try<Function<O, Optional<? extends Exception>>> postValidator = null;

        final var mapFunction = mapFunctionFactory.getMap(clazz);
        final var mapFileds = mapFieldFactory.getMap(clazz);

        final var destructor = cellSeparator.map(CsvDestructor::of);
        final var toMap = preValidator.flatMap( pv  ->
                          destructor.map(       des ->
                                                       ToMap.of(pv, des)));

        final var toTypedMap = mapFunction.map(mf ->
                          ToTypedMap.of(mf, parallelizationStrategy));

        final var combinator = NoExceptionCombinator.<Integer>of(parallelizationStrategy);

        final var restructor = ReflectionRestructor.of(clazz, outputSupplier, mapFileds);
        final var toObject = postValidator.map( pv -> ToObject.of(pv,restructor));

        return toMap.flatMap(      tm  ->
               toTypedMap.flatMap( ttm ->
               toObject.map(       to  ->
                                          Declarser.of(tm, ttm, combinator, to)))) ;
    }


}

package impl;

import impl.stages.annotations.type.CsvMapValidation;
import impl.stages.annotations.type.CsvPostValidation;
import impl.stages.annotations.type.CsvPreValidation;
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
import kernel.validator.Validator;
import utils.tryapi.Try;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static utils.constants.Constants.EMPTY;

public class CsvDeclarserFactory<O> {

    private final String cellSeparator;
    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final Supplier<O> outputSupplier;
    private final CsvFunctionMapFactory mapFunctionFactory;
    private final CsvFieldMapFactory mapFieldFactory;

    private CsvDeclarserFactory(final String cellSeparator,
                                final ParallelizationStrategyEnum parallelizationStrategy,
                                final Supplier<O> outputSupplier,
                                final Map<Class<? extends Function<String, Try<?>>>,
                                        Function<String[], Function<String, Try<?>>>> customConstructorMap,
                                final CsvFieldMapFactory mapFieldFactory) {
        this.cellSeparator = cellSeparator;
        this.parallelizationStrategy = parallelizationStrategy;
        this.outputSupplier = outputSupplier;
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(customConstructorMap);
        this.mapFieldFactory = mapFieldFactory;
    }

    public Try<Declarser<String, Integer, String, O>> apply(final Class<O> clazz, final String[] params) {
        final var preValidator = getPreValidator(clazz);
        final var mapValidator = getMapValidator(clazz);
        final var postValidator = getPostValidator(clazz);

        final var mapFunction = mapFunctionFactory.getMap(clazz);
        final var mapFileds = mapFieldFactory.getMap(clazz);

        final var destructor = CsvDestructor.of(cellSeparator);
        final var toMap = preValidator.flatMap( pv ->
                          mapValidator.map( mv ->
                                  ToMap.of(pv, mv, destructor)));

        final var toTypedMap = mapFunction.map(mf ->
                          ToTypedMap.of(mf, parallelizationStrategy));

        final var combinator = NoExceptionCombinator.<Integer>of(parallelizationStrategy);

        final var restructor = ReflectionRestructor.of(clazz, outputSupplier, mapFileds);
        final var toObject = postValidator.map( pv ->
                          ToObject.of(pv,restructor));

        return toMap.flatMap(      tm  ->
               toTypedMap.flatMap( ttm ->
               toObject.map(       to  ->
                               Declarser.of(tm, ttm, combinator, to)))) ;
    }

    private Try<? extends Validator<String>> getPreValidator(final Class<O> clazz) {
        return getValidator(clazz, CsvPreValidation.class, CsvPreValidation::preVallidation);
    }

    private Try<? extends Validator<Map<Integer, String>>> getMapValidator(final Class<O> clazz) {
        return getValidator(clazz, CsvMapValidation.class, CsvMapValidation::mapVallidation);
    }

    private Try<? extends Validator<O>> getPostValidator(final Class<O> clazz) {
        return getValidator(clazz, CsvPostValidation.class, ann -> (Class<Validator<O>>) ann.postVallidation());
    }


    private <A extends Annotation,V> Try<V> getValidator(final Class<O> clazz,
                                                            final Class<A> annotationClazz,
                                                            final Function<A, Class<V> > getValue) {
        return Optional.ofNullable(clazz.getAnnotation(annotationClazz))
                .map(getValue)
                .map(cl -> Try.go(() -> cl.getConstructor(EMPTY).newInstance()))
                .orElse(Try.success((V) Validator.ok));

    }

}

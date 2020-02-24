package impl;

import impl.stages.annotations.type.CsvType;
import impl.stages.stage01_tomap.destructors.CsvDestructor;
import impl.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import impl.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactoryConst;
import impl.stages.stage03_combinator.combinators.NoExceptionCombinator;
import impl.stages.stage04_toobject.CsvFieldMapFactory;
import impl.stages.stage04_toobject.restructors.ReflectionRestructor;
import impl.validation.CsvValidationConst;
import impl.validation.CsvValidatorsFactory;
import impl.validation.ValidatorAnnImpl;
import kernel.Declarser;
import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage01_tomap.ToMap;
import kernel.stages.stage02_totypedmap.ToTypedMap;
import kernel.stages.stage04_toobject.ToObject;
import kernel.validation.Validator;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CsvDeclarserFactory<O> {

    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final CsvValidatorsFactory<String> csvPreValidatorsFactory;
    private final CsvValidatorsFactory<Object> csvPostValidatorsFactory;
    private final CsvFunctionMapFactory mapFunctionFactory;
    private final CsvFieldMapFactory mapFieldFactory;

    private CsvDeclarserFactory(final ParallelizationStrategyEnum parallelizationStrategy,
                                final Map<Class<? extends Validator<String>>,
                                        Function<String[], Validator<String>>> customPreValidatorsMap,
                                final Map<Class<? extends Validator<Object>>,
                                        Function<String[], Validator<Object>>> customPostValidatorsMap,
                                final Map<Class<? extends Function<String, Try<?>>>,
                                        Function<String[], Function<String, Try<?>>>> customConstructorMap,
                                final CsvFieldMapFactory mapFieldFactory) {
        this.parallelizationStrategy = parallelizationStrategy;
        this.csvPreValidatorsFactory = CsvValidatorsFactory.pre(CsvValidationConst.prevalidatorClassMap, customPreValidatorsMap);
        this.csvPostValidatorsFactory = CsvValidatorsFactory.of(customPostValidatorsMap);
        Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> classFunctionMap =
                new HashMap<>(CsvFunctionMapFactoryConst.sharedFunctionClassMap);
        classFunctionMap.putAll(customConstructorMap);
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(
                csvPreValidatorsFactory,
                csvPostValidatorsFactory,
                classFunctionMap);
        this.mapFieldFactory = mapFieldFactory;
    }

    public Try<Declarser<String, Integer, String, O>> apply(final Class<O> clazz) {

        final var typeAnn = Try.go(() -> clazz.getAnnotation(CsvType.class));
        final var cellSeparator = typeAnn.map(CsvType::cellSeparator);

        final var preValidator = typeAnn.map(ann -> Stream.of(ann.csvPreValidations().preValidations())
                .map(pre -> ValidatorAnnImpl.pre(pre.validator(),pre.params()))
                .collect(Collectors.toList()))
                .flatMap(csvPreValidatorsFactory::function);

        final var postValidator = typeAnn.map(ann -> Stream.of(ann.csvPostValidations().validations())
                        .map(post -> ValidatorAnnImpl.of(post.validator(),post.params()))
                        .collect(Collectors.toList())).flatMap(csvPostValidatorsFactory::function);

        final var mapFunction = mapFunctionFactory.getMap(clazz);
        final var mapFileds = mapFieldFactory.getMap(clazz);

        final var destructor = cellSeparator.map(CsvDestructor::of);
        final var toMap = preValidator.flatMap( pv  ->
                          destructor.map(       des -> ToMap.of(pv, des)));

        final var toTypedMap = mapFunction.map( mf ->
                          ToTypedMap.of( mf, parallelizationStrategy));

        final var combinator = NoExceptionCombinator.<Integer>of(parallelizationStrategy);

        final var restructor = ReflectionRestructor.of(clazz, mapFileds);
        final var toObject = postValidator.map( pv ->
                ToObject.of(pv,restructor));

        return toMap.flatMap(      tm  ->
               toTypedMap.flatMap( ttm ->
               toObject.map(       to  -> Declarser.of(tm, ttm, combinator, to)))) ;
    }


}

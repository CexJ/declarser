package impl;

import impl.stages.annotations.validations.pre.CsvPreValidations;
import impl.stages.stage01_tomap.destructors.CsvDestructor;
import impl.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import impl.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactoryConst;
import impl.stages.stage03_combinator.combinators.NoExceptionCombinator;
import impl.stages.stage04_toobject.CsvFieldMapFactory;
import impl.stages.stage04_toobject.restructors.ReflectionRestructor;
import impl.validation.CsvValidationConst;
import impl.validation.CsvPreValidatorsFactory;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CsvDeclarserFactory<O> {

    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final CsvPreValidatorsFactory<String> csvPreValidatorsFactory;
    private final CsvFunctionMapFactory mapFunctionFactory;
    private final CsvFieldMapFactory mapFieldFactory;

    private CsvDeclarserFactory(final ParallelizationStrategyEnum parallelizationStrategy,
                                final Map<Class<? extends Validator<String>>,
                                        Function<String[], Validator<String>>> customPreValidatorsMap,
                                final Map<Class<? extends Validator<?>>,
                                        Function<String[], Validator<?>>> customPostValidatorsMap,
                                final Map<Class<? extends Function<String, Try<?>>>,
                                        Function<String[], Function<String, Try<?>>>> customConstructorMap,
                                final CsvFieldMapFactory mapFieldFactory) {
        this.parallelizationStrategy = parallelizationStrategy;
        this.csvPreValidatorsFactory = CsvPreValidatorsFactory.of(CsvValidationConst.prevalidatorClassMap, customPreValidatorsMap);
        Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> classFunctionMap =
                new HashMap<>(CsvFunctionMapFactoryConst.sharedFunctionClassMap);
        classFunctionMap.putAll(customConstructorMap);
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(
                csvPreValidatorsFactory,
                classFunctionMap);
        this.mapFieldFactory = mapFieldFactory;
    }

    public Try<Declarser<String, Integer, String, O>> apply(final Class<O> clazz,
                                                            final Validator<O> postValidator,
                                                            final String cellSeparator) {

        final var preValidator = Optional.ofNullable(clazz.getAnnotation(CsvPreValidations.class))
                .map(ann -> Stream.of(ann.validations())
                .map(pre -> ValidatorAnnImpl.pre(pre.validator(),pre.params()))
                .collect(Collectors.toList()))
                .map(csvPreValidatorsFactory::function)
                .orElse(Try.success(s -> Optional.empty()));

        final var mapFunction = mapFunctionFactory.getMap(clazz);
        final var mapFileds = mapFieldFactory.getMap(clazz);

        final var destructor = CsvDestructor.of(cellSeparator);
        final var toMap = preValidator.map(pv -> ToMap.of(pv, destructor));

        final var toTypedMap = mapFunction.map( mf ->
                          ToTypedMap.of( mf, parallelizationStrategy));

        final var combinator = NoExceptionCombinator.<Integer>of(parallelizationStrategy);

        final var restructor = ReflectionRestructor.of(clazz, mapFileds);
        final var toObject = ToObject.of(postValidator,restructor);

        return toMap.flatMap(      tm  ->
               toTypedMap.map(     ttm -> Declarser.of(tm, ttm, combinator, toObject))) ;
    }


}

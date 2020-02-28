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
import kernel.Declarser;
import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage01_tomap.ToMap;
import kernel.stages.stage02_totypedmap.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.ToObject;
import kernel.validation.Validator;
import utils.tryapi.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CsvDeclarserFactory {

    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final CsvPreValidatorsFactory csvPreValidatorsFactory;
    private final CsvFunctionMapFactory mapFunctionFactory;

    private CsvDeclarserFactory(final ParallelizationStrategyEnum parallelizationStrategy,
                                final Map<Class<? extends Validator<String>>,
                                        Function<String[], Validator<String>>> customPreValidatorsMap,
                                final Map<Class<? extends Function<String, Try<?>>>,
                                        Function<String[], Function<String, Try<?>>>> customConstructorMap) {
        this.parallelizationStrategy = parallelizationStrategy;
        this.csvPreValidatorsFactory = CsvPreValidatorsFactory.of(CsvValidationConst.prevalidatorClassMap, customPreValidatorsMap);
        Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> classFunctionMap =
                new HashMap<>(CsvFunctionMapFactoryConst.sharedFunctionClassMap);
        classFunctionMap.putAll(customConstructorMap);
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(
                this,
                csvPreValidatorsFactory,
                classFunctionMap);

    }

    public <O> Try<Declarser<String, Integer, String, O>> declarserOf(final Class<O> clazz,
                                                                      final String cellSeparator) {
        return declarserOf(clazz, o -> Optional.empty(), cellSeparator);
    }

    public <O> Try<Declarser<String, Integer, String, O>> declarserOf(final Class<O> clazz,
                                                                      final Validator<O> postValidator,
                                                                      final String cellSeparator) {

        return stage1(clazz, cellSeparator).flatMap( toMap      ->
               stage2(clazz).flatMap(                toTypedMap ->
               stage3().flatMap(                     combinator ->
               stage4(clazz, postValidator).map(     toObject   ->

                       Declarser.of(toMap, toTypedMap, combinator, toObject))))) ;
    }

    private <O> Try<ToMap<String, Integer, String>> stage1(Class<O> clazz, String cellSeparator) {
        final var preValidator = preValidator(clazz);
        final var destructor = CsvDestructor.of(cellSeparator);
        return preValidator.map( pv ->
                ToMap.of(pv, destructor));
    }

    private <O> Try<ToTypedMap<Integer, String>> stage2(Class<O> clazz) {
        final var mapFunction = mapFunctionFactory.mapColumnToTransformer(clazz);
        return mapFunction.map( mf ->
                ToTypedMap.of( mf, parallelizationStrategy));
    }

    private Try<Combinator<Integer>> stage3() {
        return Try.success(NoExceptionCombinator.<Integer>of(parallelizationStrategy));
    }

    private <O> Try<ToObject<Integer, O>> stage4(Class<O> clazz, Validator<O> postValidator) {
        final var mapFileds = CsvFieldMapFactory.mapFieldNameColumn(clazz);
        final var restructor = ReflectionRestructor.of(clazz, mapFileds);
        return Try.success(
                ToObject.of(postValidator,restructor));
    }

    private <O> Try<Validator<String>> preValidator(Class<O> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(CsvPreValidations.class))
                .map(ann -> Stream.of(ann.validations())
                        .collect(Collectors.toList()))
                .map(csvPreValidatorsFactory::function)
                .orElse(Try.success(s -> Optional.empty()));
    }

    public static Builder builder(){
        return new Builder();
    }

    public static CsvDeclarserFactory defaultFactory(){
        return new Builder().build();
    }

    public static class Builder {

        private Builder(){}

        private ParallelizationStrategyEnum parallelizationStrategy = ParallelizationStrategyEnum.SEQUENTIAL;
        private Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customPreValidatorsMap = new HashMap<>();
        private Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> customConstructorMap =  new HashMap<>();

        public Builder withParallelizationStrategy(ParallelizationStrategyEnum parallelizationStrategy) {
            if(parallelizationStrategy != null) this.parallelizationStrategy = parallelizationStrategy;
            return this;
        }

        public Builder withCustomPreValidatorsMap(Map<Class<? extends Validator<String>>,
                                                      Function<String[], Validator<String>>> customPreValidatorsMap) {
            if(customPreValidatorsMap != null) this.customPreValidatorsMap = customPreValidatorsMap;
            return this;
        }

        public Builder withCustomConstructorMap(Map<Class<? extends Function<String, Try<?>>>,
                                                    Function<String[], Function<String, Try<?>>>> customConstructorMap) {
            if(customConstructorMap != null) this.customConstructorMap = customConstructorMap;
            return this;
        }

        public CsvDeclarserFactory build(){
            return new CsvDeclarserFactory(parallelizationStrategy, customPreValidatorsMap, customConstructorMap);
        }
    }
}

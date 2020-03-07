package csv;

import csv.stages.annotations.validations.pre.CsvPreValidations;
import csv.stages.stage01_tomap.destructors.CsvDestructor;
import csv.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import csv.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactoryConst;
import kernel.stages.stage03_combinator.impl.NoExceptionCombinator;
import csv.stages.stage04_toobject.CsvFieldMapFactory;
import csv.stages.stage04_toobject.restructors.ReflectionRestructor;
import csv.validation.CsvValidationConst;
import csv.validation.CsvPreValidatorsFactory;
import kernel.Declarser;
import kernel.enums.ParallelizationStrategyEnum;
import kernel.stages.stage01_tomap.impl.impl.ToMapImpl;
import kernel.stages.stage02_totypedmap.impl.impl.ToTypedMapImpl;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.impl.impl.ToObjectImpl;
import kernel.validations.Validator;
import kernel.tryapi.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class CsvDeclarserFactory {

    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final CsvPreValidatorsFactory csvPreValidatorsFactory;
    private final CsvFunctionMapFactory mapFunctionFactory;

    private CsvDeclarserFactory(
            final ParallelizationStrategyEnum parallelizationStrategy,
            final Map<Class<? extends Validator<String>>,
                    Function<String[], Validator<String>>> customPreValidatorsMap,
            final Map<Class<? extends Function<String, Try<?>>>,
                    Function<String[], Function<String, Try<?>>>> customConstructorMap) {
        this.parallelizationStrategy = parallelizationStrategy;
        this.csvPreValidatorsFactory = CsvPreValidatorsFactory.of(CsvValidationConst.prevalidatorClassMap, customPreValidatorsMap);
        final var classFunctionMap = new HashMap<>(CsvFunctionMapFactoryConst.sharedFunctionClassMap);
        classFunctionMap.putAll(customConstructorMap);
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(
                this,
                csvPreValidatorsFactory,
                classFunctionMap);

    }

    public <O> Try<Declarser<String, Integer, String, O>> declarserOf(
            final Class<O> clazz,
            final String cellSeparator) {
        return declarserOf(clazz, o -> Optional.empty(), cellSeparator);
    }

    public <O> Try<Declarser<String, Integer, String, O>> declarserOf(
            final Class<O> clazz,
            final Validator<O> postValidator,
            final String cellSeparator) {
        return stage1(clazz, cellSeparator).flatMap( toMap      ->
               stage2(clazz).flatMap(                toTypedMap ->
               stage3().flatMap(                     combinator ->
               stage4(clazz, postValidator).map(     toObject   ->

                       Declarser.of(toMap, toTypedMap, combinator, toObject))))) ;
    }

    private <O> Try<ToMapImpl<String, Integer, String>> stage1(
            final Class<O> clazz,
            final String cellSeparator) {
        final var preValidator = preValidator(clazz);
        final var destructor = CsvDestructor.of(cellSeparator);
        return preValidator.map( pv ->
                ToMapImpl.of(pv, destructor));
    }

    private <O> Try<ToTypedMapImpl<Integer, String>> stage2(
            final Class<O> clazz) {
        final var mapFunction = mapFunctionFactory.mapColumnToTransformer(clazz);
        return mapFunction.map( mf ->
                ToTypedMapImpl.of( mf, parallelizationStrategy));
    }

    private Try<Combinator<Integer>> stage3() {
        return Try.success(NoExceptionCombinator.of(parallelizationStrategy));
    }

    private <O> Try<ToObjectImpl<Integer, O>> stage4(
            final Class<O> clazz,
            final Validator<O> postValidator) {
        final var mapFileds = CsvFieldMapFactory.mapFieldNameColumn(clazz);
        final var restructor = ReflectionRestructor.of(clazz, mapFileds);
        return Try.success(
                ToObjectImpl.of(postValidator,restructor));
    }

    private <O> Try<Validator<String>> preValidator(
            final Class<O> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(CsvPreValidations.class))
                .map(ann -> Stream.of(ann.value())
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

    public static final class Builder {

        private Builder(){}

        private ParallelizationStrategyEnum parallelizationStrategy = ParallelizationStrategyEnum.SEQUENTIAL;
        private Map<Class<? extends Validator<String>>,
                Function<String[], Validator<String>>> customPreValidatorsMap = new HashMap<>();
        private Map<Class<? extends Function<String, Try<?>>>,
                Function<String[], Function<String, Try<?>>>> customConstructorMap =  new HashMap<>();

        public Builder withParallelizationStrategy(
                final ParallelizationStrategyEnum parallelizationStrategy) {
            if(parallelizationStrategy != null) this.parallelizationStrategy = parallelizationStrategy;
            return this;
        }

        public Builder withCustomPreValidatorsMap(
                final Map<Class<? extends Validator<String>>,
                        Function<String[], Validator<String>>> customPreValidatorsMap) {
            if(customPreValidatorsMap != null) this.customPreValidatorsMap = customPreValidatorsMap;
            return this;
        }

        public Builder withCustomConstructorMap(
                final Map<Class<? extends Function<String, Try<?>>>,
                        Function<String[], Function<String, Try<?>>>> customConstructorMap) {
            if(customConstructorMap != null) this.customConstructorMap = customConstructorMap;
            return this;
        }

        public CsvDeclarserFactory build(){
            return new CsvDeclarserFactory(parallelizationStrategy, customPreValidatorsMap, customConstructorMap);
        }
    }
}

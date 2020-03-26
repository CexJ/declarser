package io.github.cexj.declarser.csv;

import io.github.cexj.declarser.csv.stages.annotations.prevalidations.CsvPreValidation;
import io.github.cexj.declarser.csv.stages.stage01_tomap.destructors.CsvDestructor;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.consts.CsvFunctionMapFactoryConst;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.CsvFieldComposer;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier.CsvFieldModifier;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator.CsvFieldPrevalidator;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer.CsvFieldTransformer;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.extractor.CsvFieldsExtractor;
import io.github.cexj.declarser.csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import io.github.cexj.declarser.kernel.Declarser;
import io.github.cexj.declarser.kernel.enums.SubsetType;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.ToMap;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.ToTypedMap;
import io.github.cexj.declarser.csv.stages.stage04_toobject.CsvFieldMapFactory;
import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.ToObject;
import io.github.cexj.declarser.csv.validation.consts.CsvValidationConst;
import io.github.cexj.declarser.csv.validation.utils.CsvPreValidatorsFactory;
import io.github.cexj.declarser.kernel.enums.ParallelizationStrategyEnum;
import io.github.cexj.declarser.kernel.stages.stage03_combinator.Combinator;
import io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.restructor.Restructor;
import io.github.cexj.declarser.kernel.validations.Validator;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


final class CsvDeclarserFactoryImp implements CsvDeclarserFactory {

    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final CsvPreValidatorsFactory csvPreValidatorsFactory;
    private final CsvFunctionMapFactory mapFunctionFactory;
    private final SubsetType annotationsSubsetType;
    private final CsvPreValidatorsExtractor preValidatorExtractor;

    private CsvDeclarserFactoryImp(
            final ParallelizationStrategyEnum parallelizationStrategy,
            final Map<Class<? extends Validator<String>>,
                    Function<String[], Validator<String>>> customPreValidatorsMap,
            final Map<Class<? extends Parser<String>>,
                    Function<String[], Parser<String>>> customConstructorMap,
            final SubsetType annotationsSubsetType) {
        this.parallelizationStrategy = parallelizationStrategy;
        this.csvPreValidatorsFactory = CsvPreValidatorsFactory.of(CsvValidationConst.prevalidatorClassMap, customPreValidatorsMap);
        final var classFunctionMap = new HashMap<>(CsvFunctionMapFactoryConst.sharedFunctionClassMap);
        classFunctionMap.putAll(customConstructorMap);

        this.preValidatorExtractor = CsvPreValidatorsExtractor.getInstance();
        final var csvFieldModifier = CsvFieldModifier.getInstance();
        final var csvFieldPrevalidator = CsvFieldPrevalidator.of(
                csvPreValidatorsFactory,
                preValidatorExtractor);
        final var csvFieldTransformer = CsvFieldTransformer.of(
                this,
                classFunctionMap,
                CsvFunctionMapFactoryConst.autoFunctionClassMap);
        final var functionComposer = CsvFieldComposer.of(
                csvFieldModifier,
                csvFieldPrevalidator,
                csvFieldTransformer);

        this.mapFunctionFactory =  CsvFunctionMapFactory.of(
                CsvFieldsExtractor.getInstance(),
                functionComposer);
        this.annotationsSubsetType = annotationsSubsetType;
    }

    static CsvDeclarserFactoryImp of(
            final ParallelizationStrategyEnum parallelizationStrategy,
            final Map<Class<? extends Validator<String>>,
                    Function<String[], Validator<String>>> customPreValidatorsMap,
            final Map<Class<? extends Parser<String>>,
                    Function<String[], Parser<String>>> customConstructorMap,
            final SubsetType annotationsSubsetType) {
        return new CsvDeclarserFactoryImp(
                parallelizationStrategy,
                customPreValidatorsMap,
                customConstructorMap,
                annotationsSubsetType);
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

    private <O> Try<ToMap<String, Integer, String>> stage1(
            final Class<O> clazz,
            final String cellSeparator) {
        final var preValidator = preValidator(clazz);
        final var destructor = CsvDestructor.of(cellSeparator);
        return preValidator.map( pv ->
                ToMap.of(pv, destructor));
    }

    private <O> Try<ToTypedMap<Integer, String>> stage2(
            final Class<O> clazz) {
        final var mapFunction = mapFunctionFactory.mapColumnToTransformer(clazz);
        return mapFunction.map( mf ->
                ToTypedMap.of( mf, annotationsSubsetType, parallelizationStrategy));
    }

    private Try<Combinator<Integer>> stage3() {
        return Try.success(Combinator.noException(parallelizationStrategy));
    }

    private <O> Try<ToObject<Integer, O>> stage4(
            final Class<O> clazz,
            final Validator<O> postValidator) {
        final var mapFields = CsvFieldMapFactory.getInstance().mapFieldNameColumn(clazz);
        final var restructor = mapFields.flatMap( mf ->
                Restructor.reflection(clazz, mf, annotationsSubsetType, SubsetType.CONTAINED));
        return restructor.map(res -> ToObject.of(postValidator,res));
    }

    private <O> Try<Validator<String>> preValidator(
            final Class<O> clazz) {
        return Optional.ofNullable(clazz.getAnnotationsByType(CsvPreValidation.class))
                .map(anns -> Stream.of(anns)
                        .map(preValidatorExtractor::extract)
                        .collect(Collectors.toList()))
                .map(csvPreValidatorsFactory::function)
                .orElse(Try.success(s -> Optional.empty()));
    }
}

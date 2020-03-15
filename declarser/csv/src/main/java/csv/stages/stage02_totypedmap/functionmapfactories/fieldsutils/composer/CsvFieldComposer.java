package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier.CsvFieldModifier;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator.CsvFieldPrevalidator;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer.CsvFieldTransformer;
import csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import csv.validation.utils.factory.CsvPreValidatorsFactory;

import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import kernel.stages.stage02_totypedmap.impl.impl.Transformer;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class CsvFieldComposer implements FieldComposer<Integer, String> {

    private final CsvFieldModifier csvFieldModifier;
    private final CsvFieldTransformer csvFieldTransformer;
    private final CsvFieldPrevalidator csvFieldPrevalidator;

    private CsvFieldComposer(
            final CsvFieldModifier csvFieldModifier,
            final CsvFieldPrevalidator csvFieldPrevalidator,
            final CsvFieldTransformer csvFieldTransformer) {
        this.csvFieldModifier = csvFieldModifier;
        this.csvFieldPrevalidator = csvFieldPrevalidator;
        this.csvFieldTransformer = csvFieldTransformer;
    }

    public static CsvFieldComposer of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        final var csvFieldModifier = CsvFieldModifier.getInstance();
        final var csvFieldPrevalidator = CsvFieldPrevalidator.of(preValidatorFactory, csvPreValidatorsExtractor);
        final var csvFieldTransformer = CsvFieldTransformer.of(
                csvDeclarserFactory,
                functionClassMap,
                autoFunctionClassMap);
        return new CsvFieldComposer(csvFieldModifier, csvFieldPrevalidator, csvFieldTransformer);
    }


    public Try<Transformer<Integer,String>> compute(
            final Field field) {
        final var modifier = csvFieldModifier.compute(field);
        final var preValidator = csvFieldPrevalidator.compute(field);
        final var transformer = csvFieldTransformer.compute(field);

        final var csvColumn = field.getAnnotation(CsvColumn.class);

        final Try<Function<String, Try<?>>> function =
                preValidator.flatMap( pre ->
                transformer.map(      tra ->  functionFrom(pre, tra)));


        return function.flatMap( fun ->
               modifier.map(     mod ->
                       Transformer.of(csvColumn.value(), mod.apply(fun))));
    }

    private Function<String, Try<?>> functionFrom(
            final Validator<String> pre,
            final Function<String, Try<?>> tra) {
        return s -> { final var validationResult = pre.apply(s);
            return validationResult.isEmpty() ? tra.apply(s)
                                              : Try.fail(validationResult.get());
        };
    }


}

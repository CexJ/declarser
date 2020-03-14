package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer;

import csv.CsvDeclarserFactory;
import csv.stages.annotations.fields.CsvColumn;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier.CsvFieldModifier;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer.CsvFieldTransformer;
import csv.validation.utils.CsvPreValidatorsFactory;
import csv.validation.utils.CsvPreValidatorsExtractor;

import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import kernel.stages.stage02_totypedmap.impl.impl.Transformer;
import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class CsvFieldComposer implements FieldComposer<Integer, String> {

    private final CsvFieldModifier csvFieldModifier;
    private final CsvFieldTransformer csvFieldTransformer;

    private CsvFieldComposer(
            final CsvFieldModifier csvFieldModifier,
            final CsvFieldTransformer csvFieldTransformer) {
        this.csvFieldModifier = csvFieldModifier;
        this.csvFieldTransformer = csvFieldTransformer;
    }

    public static CsvFieldComposer of(
            final CsvDeclarserFactory csvDeclarserFactory,
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor,
            final Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> functionClassMap,
            final Map<Class<?>, Function<String, Try<?>>> autoFunctionClassMap) {
        final var csvFieldModifier = CsvFieldModifier.getInstance();
        final var csvFieldTransformer = CsvFieldTransformer.of(
                csvDeclarserFactory,
                preValidatorFactory,
                csvPreValidatorsExtractor,
                functionClassMap,
                autoFunctionClassMap);
        return new CsvFieldComposer(csvFieldModifier, csvFieldTransformer);
    }


    public Try<Transformer<Integer,String>> compute(
            final Field field) {
        final var modifier = csvFieldModifier.compute(field);
        final var transformer = csvFieldTransformer.compute(field);

        final var csvColumn = field.getAnnotation(CsvColumn.class);

        return transformer.flatMap( tra ->
               modifier.map(        mod ->
                       Transformer.of(csvColumn.value(), mod.apply(tra))));
    }



}

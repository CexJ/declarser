package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvColumn;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier.CsvFieldModifier;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator.CsvFieldPrevalidator;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.transformer.CsvFieldTransformer;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer.Transformer;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;

import java.lang.reflect.Field;
import java.util.function.Function;

public final class CsvFieldComposer implements FieldComposer<Integer, String> {

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
            final CsvFieldModifier csvFieldModifier,
            final CsvFieldPrevalidator csvFieldPrevalidator,
            final CsvFieldTransformer csvFieldTransformer){
        return new CsvFieldComposer(csvFieldModifier, csvFieldPrevalidator, csvFieldTransformer);
    }


    public Try<Transformer<Integer,String>> compute(
            final Field field) {
        final var modifier     = csvFieldModifier.compute(field);
        final var preValidator = csvFieldPrevalidator.compute(field);
        final var transformer  = csvFieldTransformer.compute(field);

        final var csvColumn = field.getAnnotation(CsvColumn.class);

        final Try<Parser<String,?>> function =
                preValidator.flatMap( pre ->
                transformer.map(      tra ->  parserFrom(pre, tra)));


        return function.flatMap( fun ->
               modifier.map(     mod ->
                       Transformer.of(csvColumn.value(), mod.apply(fun))));
    }

    @SuppressWarnings({"unchecked"})
    private Parser<String,?> parserFrom(
            final Validator<String> pre,
            final Parser<String,?> tra) {
        return s -> pre.apply(s)
                .map(Try::fail)
                .orElse((Try<Object>) tra.apply(s));
    }


}

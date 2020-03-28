package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import io.github.cexj.declarser.csv.stages.annotations.prevalidations.CsvPreValidation;
import io.github.cexj.declarser.csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import io.github.cexj.declarser.csv.validation.utils.CsvPreValidatorsFactory;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class CsvFieldPrevalidatorImpl implements CsvFieldPrevalidator {

    private final CsvPreValidatorsFactory preValidatorFactory;
    private final CsvPreValidatorsExtractor csvPreValidatorsExtractor;

    private CsvFieldPrevalidatorImpl(
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor) {
        this.preValidatorFactory = preValidatorFactory;
        this.csvPreValidatorsExtractor = csvPreValidatorsExtractor;
    }

    static CsvFieldPrevalidatorImpl of(
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor) {
        return new CsvFieldPrevalidatorImpl(preValidatorFactory, csvPreValidatorsExtractor);
    }


    public Try<Validator<String>> compute(
            final Field field){
        final var annPrevalidators = Optional.ofNullable(field.getAnnotationsByType(CsvPreValidation.class))
                .orElse(new CsvPreValidation[0]);
        return preValidatorFactory.function(Stream.of(annPrevalidators)
                        .map(csvPreValidatorsExtractor::extract)
                        .collect(Collectors.toList()));

    }

}

package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import csv.stages.annotations.validations.pre.CsvPreValidation;
import csv.stages.annotations.validations.pre.CsvPreValidations;
import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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


    public Try<Validator<String>> compute(Field field){
        final var annPrevalidators = Optional.ofNullable(field.getAnnotation(CsvPreValidations.class))
                .map(CsvPreValidations::value)
                .orElse(new CsvPreValidation[0]);
        return preValidatorFactory.function(csvPreValidatorsExtractor.extract(Arrays.asList(annPrevalidators)));

    }

}

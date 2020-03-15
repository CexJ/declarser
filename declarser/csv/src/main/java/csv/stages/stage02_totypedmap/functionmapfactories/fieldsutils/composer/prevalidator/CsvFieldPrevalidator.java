package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import csv.validation.utils.CsvPreValidatorsExtractor;
import csv.validation.utils.CsvPreValidatorsFactory;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.lang.reflect.Field;

public interface CsvFieldPrevalidator {

    static CsvFieldPrevalidator of(
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor csvPreValidatorsExtractor) {
        return CsvFieldPrevalidatorImpl.of(
                preValidatorFactory,
                csvPreValidatorsExtractor);
    }

    Try<Validator<String>> compute(Field field);
}

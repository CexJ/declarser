package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import csv.validation.utils.factory.CsvPreValidatorsFactory;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.lang.reflect.Field;

public interface CsvFieldPrevalidator {

    static CsvFieldPrevalidator of(
            final CsvPreValidatorsFactory preValidatorFactory,
            final CsvPreValidatorsExtractor Impl) {
        return CsvFieldPrevalidatorImpl.of(
                preValidatorFactory,
                Impl);
    }

    Try<Validator<String>> compute(Field field);
}

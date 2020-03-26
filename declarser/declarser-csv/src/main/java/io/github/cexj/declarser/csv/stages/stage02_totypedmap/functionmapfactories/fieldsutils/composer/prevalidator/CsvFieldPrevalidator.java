package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.prevalidator;

import io.github.cexj.declarser.csv.validation.utils.extractor.CsvPreValidatorsExtractor;
import io.github.cexj.declarser.csv.validation.utils.CsvPreValidatorsFactory;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.validations.Validator;

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

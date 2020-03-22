package io.github.cexj.declarser.csv.validation.utils.extractor;

import io.github.cexj.declarser.csv.stages.annotations.prevalidations.CsvPreValidation;
import io.github.cexj.declarser.kernel.validations.prevalidations.PreValidator;

public interface CsvPreValidatorsExtractor {

    static CsvPreValidatorsExtractor getInstance(){
        return CsvPreValidatorsExtractorImpl.getInstance();
    }

    PreValidator<String> extract(
            final CsvPreValidation validatorAnnotation);
}

package csv.validation.utils.extractor;

import csv.stages.annotations.validations.pre.CsvPreValidation;
import kernel.validations.prevalidations.PreValidator;

public interface CsvPreValidatorsExtractor {

    static CsvPreValidatorsExtractor getInstance(){
        return CsvPreValidatorsExtractorImpl.getInstance();
    }

    PreValidator<String> extract(
            CsvPreValidation validatorAnns);
}

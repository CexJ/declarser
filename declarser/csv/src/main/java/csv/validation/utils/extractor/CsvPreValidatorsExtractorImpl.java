package csv.validation.utils.extractor;

import csv.stages.annotations.prevalidations.CsvPreValidation;
import kernel.validations.prevalidations.PreValidator;

final class CsvPreValidatorsExtractorImpl implements CsvPreValidatorsExtractor {

    private CsvPreValidatorsExtractorImpl() {}

    private static class InstanceHolder {
        private static final CsvPreValidatorsExtractorImpl instance = new CsvPreValidatorsExtractorImpl();
    }

    static CsvPreValidatorsExtractorImpl getInstance(){
        return CsvPreValidatorsExtractorImpl.InstanceHolder.instance;
    }

    @Override
    public PreValidator<String> extract(
            final CsvPreValidation validatorAnnotation){
        return PreValidator.of(validatorAnnotation.value(), validatorAnnotation.params());
    }
}

package csv.validation.utils.extractor;

import csv.stages.annotations.validations.pre.CsvPreValidation;
import kernel.validations.prevalidations.PreValidator;

class CsvPreValidatorsExtractorImpl implements CsvPreValidatorsExtractor {

    private CsvPreValidatorsExtractorImpl() {}

    private static class InstanceHolder {
        private static final CsvPreValidatorsExtractorImpl instance = new CsvPreValidatorsExtractorImpl();
    }

    static CsvPreValidatorsExtractorImpl getInstance(){
        return CsvPreValidatorsExtractorImpl.InstanceHolder.instance;
    }

    @Override
    public PreValidator<String> extract(
            final CsvPreValidation validatorAnns){
        return PreValidator.of(validatorAnns.value(), validatorAnns.params());
    }
}

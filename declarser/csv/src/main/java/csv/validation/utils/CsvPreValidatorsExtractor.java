package csv.validation.utils;

import csv.stages.annotations.validations.pre.CsvPreValidation;
import kernel.validations.impl.PreValidator;

import java.util.List;
import java.util.stream.Collectors;

public class CsvPreValidatorsExtractor {

    private CsvPreValidatorsExtractor() {}

    private static class InstanceHolder {
        private static final CsvPreValidatorsExtractor instance = new CsvPreValidatorsExtractor();
    }
    public static CsvPreValidatorsExtractor getInstance(){
        return CsvPreValidatorsExtractor.InstanceHolder.instance;
    }

    public List<PreValidator<String>> extract(
            final List<? extends CsvPreValidation> validatorAnns){
        return validatorAnns.stream()
                .map(ann -> PreValidator.of(ann.value(), ann.params()))
                .collect(Collectors.toList());
    }
}

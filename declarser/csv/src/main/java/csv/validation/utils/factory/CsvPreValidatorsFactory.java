package csv.validation.utils.factory;

import kernel.tryapi.Try;
import kernel.validations.Validator;
import kernel.validations.prevalidations.PreValidator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface CsvPreValidatorsFactory {
    static CsvPreValidatorsFactory of(
            Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> validatorClassMap,
            Map<Class<? extends Validator<String>>, Function<String[], Validator<String>>> customMap){
        return CsvPreValidatorsFactoryImpl.of(validatorClassMap, customMap);
    }

    Try<Validator<String>> function(
            final List<? extends PreValidator<String>> validatorAnns);
}

package csv;

import kernel.Declarser;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.util.Optional;

public interface CsvDeclarserFactory {

    default <O> Try<Declarser<String, Integer, String, O>> declarserOf(
            final Class<O> clazz,
            final String cellSeparator){
        return declarserOf(clazz, o -> Optional.empty(), cellSeparator);
    }

    <O> Try<Declarser<String, Integer, String, O>> declarserOf(
            final Class<O> clazz,
            final Validator<O> postValidator,
            final String cellSeparator);
}

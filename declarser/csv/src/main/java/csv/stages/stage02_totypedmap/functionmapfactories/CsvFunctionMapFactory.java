package csv.stages.stage02_totypedmap.functionmapfactories;

import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldsExtractor;
import kernel.tryapi.Try;

import java.util.Map;
import java.util.function.Function;

public interface CsvFunctionMapFactory {
    Try<Map<Integer, Function<String, Try<?>>>> mapColumnToTransformer(
            Class<?> clazz);

    static CsvFunctionMapFactory of(
            final FieldsExtractor fieldsExtractor,
            final FieldComposer<Integer, String> functionComposer) {
        return CsvFunctionMapFactoryImpl.of(fieldsExtractor,functionComposer);
    }
}

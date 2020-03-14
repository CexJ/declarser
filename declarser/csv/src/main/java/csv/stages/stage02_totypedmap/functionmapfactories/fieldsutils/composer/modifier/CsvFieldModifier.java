package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier;

import kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface CsvFieldModifier {

    static CsvFieldModifierImpl getInstance() {
        return CsvFieldModifierImpl.getInstance();
    }

    Try<UnaryOperator<Function<String, Try<?>>>> compute(Field field);
}

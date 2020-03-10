package kernel.stages.stage02_totypedmap.impl.fieldsutils;

import kernel.stages.stage02_totypedmap.impl.impl.Transformer;
import kernel.tryapi.Try;

import java.lang.reflect.Field;

public interface FieldComposer<K,T> {
    Try<Transformer<K,T>> computeTransformer(final Field field);
}

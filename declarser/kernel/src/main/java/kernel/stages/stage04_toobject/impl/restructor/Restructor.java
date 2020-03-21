package kernel.stages.stage04_toobject.impl.restructor;

import kernel.enums.SubsetType;
import kernel.tryapi.Try;

import java.util.Map;

public interface Restructor<K,O> {

    static <K,O> Try<Restructor<K,O>> reflection(
            final Class<O> clazz,
            final Map<String,K> mapFields,
            final SubsetType inputMapType,
            final SubsetType fieldMapType){
        return ReflectionRestructorImpl.of(
                clazz,
                mapFields,
                inputMapType,
                fieldMapType);
    }

    Try<O> restruct(final Map<K,?> input);
}

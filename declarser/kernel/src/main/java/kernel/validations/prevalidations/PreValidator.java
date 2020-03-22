package kernel.validations.prevalidations;

import kernel.validations.Validator;

public final class PreValidator<T> {

    private final Class<? extends Validator<T>> clazz;
    private final T[] params;

    private PreValidator(
            final Class<? extends Validator<T>> clazz,
            final T[] params) {
        this.clazz = clazz;
        this.params = params;
    }

    public static <T> PreValidator<T> of(
            final Class<? extends Validator<T>> clazz,
            final T[] params){
        return new PreValidator<>(clazz, params);
    }

    public Class<? extends Validator<T>> getClazz() {
        return clazz;
    }

    public T[] getParams() {
        return params;
    }
}

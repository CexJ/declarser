package kernel.validations.impl;

import kernel.validations.Validator;

public class PreValidator<T> {

    private final Class<? extends Validator<T>> clazz;
    private final String[] params;

    private PreValidator(
            final Class<? extends Validator<T>> clazz,
            final String[] params) {
        this.clazz = clazz;
        this.params = params;
    }

    public static <T> PreValidator<T> of(
            final Class<? extends Validator<T>> clazz,
            final String[] params){
        return new PreValidator<>(clazz, params);
    }

    public Class<? extends Validator<T>> getClazz() {
        return clazz;
    }

    public String[] getParams() {
        return params;
    }
}

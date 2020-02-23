package impl.validation;

import kernel.validation.Validator;

public class ValidatorAnnImpl<T> {

    private final Class<? extends Validator<T>> clazz;
    private final String[] params;

    private ValidatorAnnImpl(Class<? extends Validator<T>> clazz, String[] params) {
        this.clazz = clazz;
        this.params = params;
    }

    public static <T> ValidatorAnnImpl<T> of(Class<? extends Validator<T>> clazz, String[] params) {
        return new ValidatorAnnImpl<>(clazz, params);
    }

    public Class<? extends Validator<T>> getClazz() {
        return clazz;
    }

    public String[] getParams() {
        return params;
    }
}

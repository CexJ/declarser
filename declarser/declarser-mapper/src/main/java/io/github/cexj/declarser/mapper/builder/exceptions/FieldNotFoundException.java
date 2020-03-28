package io.github.cexj.declarser.mapper.builder.exceptions;

public class FieldNotFoundException extends Exception {

    private final String fieldName;
    @SuppressWarnings("rawtypes")
    private final Class toClazz;

    private <O> FieldNotFoundException(String fieldName, Class<O> toClazz) {
        this.fieldName = fieldName;
        this.toClazz = toClazz;
    }

    public static <O> FieldNotFoundException of(
            final String fieldName,
            final Class<O> toClazz) {
        return new FieldNotFoundException(fieldName, toClazz);
    }

    public String getFieldName() {
        return fieldName;
    }

    @SuppressWarnings("rawtypes")
    public Class getToClazz() {
        return toClazz;
    }
}

package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions;

import java.lang.reflect.Field;

public final class MissingTransformerException extends Exception {

    public final static String messageFormatter =
            "Missing transformer for the field: %s";

    private final Field field;

    private MissingTransformerException(
            final Field field) {
        super(String.format(messageFormatter, field.getName()));
        this.field = field;
    }

    public static MissingTransformerException of(
            final Field field) {
        return new MissingTransformerException(field);
    }


    public Field getField() {
        return field;
    }
}

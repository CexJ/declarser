package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions;

import java.lang.reflect.Field;

public final class MissingArrayException extends Exception {
    public final static String messageFormatter =
            "Expecting an array but found: %s for the field %s";

    private final Field field;

    private MissingArrayException(Field field) {
        super(String.format(messageFormatter, field.getType().toString(), field.getName()));
        this.field = field;
    }

    public static MissingArrayException of(Field field) {
        return new MissingArrayException(field);
    }


    public Field getField() {
        return field;
    }
}

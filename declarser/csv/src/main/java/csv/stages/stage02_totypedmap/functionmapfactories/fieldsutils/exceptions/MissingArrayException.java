package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions;

import java.lang.reflect.Field;

public class MissingArrayException extends Exception {
    public final static String messageFormatter =
            "Missing transformer for the field: %s";

    private final Field field;

    private MissingArrayException(Field field) {
        super(String.format(messageFormatter, field.getName()));
        this.field = field;
    }

    public static MissingArrayException of(Field field) {
        return new MissingArrayException(field);
    }


    public Field getField() {
        return field;
    }
}

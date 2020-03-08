package csv.stages.stage04_toobject.exceptions;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class RepeatedCsvColumnIndexException extends Exception{

    public final static String messageFormatter =
            "I find the following repetitions %s " +
             "in columns' indexes of the class %s";

    private final Map<Integer, List<Field>> repetitions;
    @SuppressWarnings("rawtypes")
    private final Class clazz;

    private <T> RepeatedCsvColumnIndexException(
            final Map<Integer, List<Field>> repetitions,
            final Class<T> clazz){
        super(String.format(messageFormatter, repetitions, clazz));
        this.repetitions = repetitions;
        this.clazz = clazz;
    }

    public static <T> RepeatedCsvColumnIndexException of(
            final Map<Integer, List<Field>> repetitions,
            final Class<T> clazz) {
        return new RepeatedCsvColumnIndexException(repetitions, clazz);
    }
}

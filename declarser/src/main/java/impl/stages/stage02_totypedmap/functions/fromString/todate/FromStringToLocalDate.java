package impl.stages.stage02_totypedmap.functions.fromString.todate;

import utils.tryapi.Try;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FromStringToLocalDate implements Function<String, Try<?>> {

    private final String format;

    private static final Map<String, FromStringToLocalDate> instancesMap = new HashMap<>();

    public static synchronized Function<String, Try<?>> getInstance(final String format) {
        if(instancesMap.get(format) == null){
            FromStringToLocalDate newFromStringToLocalDate = new FromStringToLocalDate(format);
            instancesMap.put(format, newFromStringToLocalDate);
        }
        return instancesMap.get(format);
    }

    private FromStringToLocalDate(final String format){
        this.format = format;
    }

    @Override
    public Try<LocalDate> apply(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return Try.go(() -> LocalDate.parse(s));
    }
}

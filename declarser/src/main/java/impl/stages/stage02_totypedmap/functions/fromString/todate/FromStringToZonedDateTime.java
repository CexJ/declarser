package impl.stages.stage02_totypedmap.functions.fromString.todate;

import utils.tryapi.Try;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FromStringToZonedDateTime implements Function<String, Try<?>> {

    private final String format;

    private static final Map<String, FromStringToZonedDateTime> instancesMap = new HashMap<>();

    public static synchronized FromStringToZonedDateTime getInstance(final String format) {
        if(instancesMap.get(format) == null){
            FromStringToZonedDateTime newFromStringToLocalDate = new FromStringToZonedDateTime(format);
            instancesMap.put(format, newFromStringToLocalDate);
        }
        return instancesMap.get(format);
    }

    private FromStringToZonedDateTime(final String format){
        this.format = format;
    }

    @Override
    public Try<ZonedDateTime> apply(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return Try.go(() -> ZonedDateTime.parse(s));
    }
}

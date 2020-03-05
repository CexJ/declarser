package kernel.parsers.fromstring.todate;

import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ZonedDateTimeParser implements Function<String, Try<?>> {

    private final String format;

    private static final Map<String, ZonedDateTimeParser> instancesMap = new HashMap<>();

    public static synchronized ZonedDateTimeParser getInstance(
            final String format) {
        if(instancesMap.get(format) == null){
            ZonedDateTimeParser newFromStringToLocalDate = new ZonedDateTimeParser(format);
            instancesMap.put(format, newFromStringToLocalDate);
        }
        return instancesMap.get(format);
    }

    private ZonedDateTimeParser(
            final String format){
        this.format = format;
    }

    @Override
    public Try<ZonedDateTime> apply(
            final String s) {
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> ZonedDateTime.parse(s,DateTimeFormatter.ofPattern(format)))
                .enrichException(ex -> ParseException.of(s, ZonedDateTime.class, ex));
    }
}

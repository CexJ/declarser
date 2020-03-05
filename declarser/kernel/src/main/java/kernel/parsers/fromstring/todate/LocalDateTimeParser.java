package kernel.parsers.fromstring.todate;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class LocalDateTimeParser implements Function<String, Try<?>> {

    private final String format;

    private static final Map<String, LocalDateTimeParser> instancesMap = new HashMap<>();

    public static synchronized LocalDateTimeParser getInstance(
            final String format) {
        if(instancesMap.get(format) == null){
            LocalDateTimeParser newFromStringToLocalDateTime = new LocalDateTimeParser(format);
            instancesMap.put(format, newFromStringToLocalDateTime);
        }
        return instancesMap.get(format);
    }

    private LocalDateTimeParser(
            final String format){
        this.format = format;
    }

    @Override
    public Try<LocalDateTime> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.go(() -> LocalDateTime.parse(s,DateTimeFormatter.ofPattern(format)))
                .enrichException(ex -> ParserException.of(s, LocalDateTime.class, ex));
    }
}

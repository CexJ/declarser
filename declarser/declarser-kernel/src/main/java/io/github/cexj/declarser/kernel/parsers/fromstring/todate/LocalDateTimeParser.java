package io.github.cexj.declarser.kernel.parsers.fromstring.todate;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class LocalDateTimeParser implements Parser<String> {

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
        else return Try.call(() -> LocalDateTime.parse(s,DateTimeFormatter.ofPattern(format)))
                .enrichException(ex -> ParserException.of(s, LocalDateTime.class, ex));
    }
}

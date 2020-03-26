package io.github.cexj.declarser.kernel.parsers.fromstring.todate;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

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
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> ZonedDateTime.parse(s,DateTimeFormatter.ofPattern(format)))
                .enrichException(ex -> ParserException.of(s, ZonedDateTime.class, ex));
    }
}

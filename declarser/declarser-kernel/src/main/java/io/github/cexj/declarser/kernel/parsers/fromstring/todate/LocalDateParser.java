package io.github.cexj.declarser.kernel.parsers.fromstring.todate;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class LocalDateParser implements Function<String, Try<?>> {

    private final String format;

    private static final Map<String, LocalDateParser> instancesMap = new HashMap<>();

    public static synchronized LocalDateParser getInstance(
            final String format) {
        if(instancesMap.get(format) == null){
            LocalDateParser newFromStringToLocalDate = new LocalDateParser(format);
            instancesMap.put(format, newFromStringToLocalDate);
        }
        return instancesMap.get(format);
    }

    private LocalDateParser(
            final String format){
        this.format = format;
    }

    @Override
    public Try<LocalDate> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> LocalDate.parse(s, DateTimeFormatter.ofPattern(format)))
                .enrichException(ex -> ParserException.of(s,LocalDate.class, ex));
    }
}

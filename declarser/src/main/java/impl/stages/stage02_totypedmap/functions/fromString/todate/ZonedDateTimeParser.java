package impl.stages.stage02_totypedmap.functions.fromString.todate;

import utils.tryapi.Try;

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
        return Try.go(() -> ZonedDateTime.parse(s,DateTimeFormatter.ofPattern(format)));
    }
}

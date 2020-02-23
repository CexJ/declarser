package impl.stages.stage02_totypedmap.functions.fromString.todate;

import utils.tryapi.Try;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FromStringToLocalDateTime implements Function<String, Try<?>> {

    private final String format;

    private static final Map<String, FromStringToLocalDateTime> instancesMap = new HashMap<>();

    public static synchronized FromStringToLocalDateTime getInstance(final String format) {
        if(instancesMap.get(format) == null){
            FromStringToLocalDateTime newFromStringToLocalDateTime = new FromStringToLocalDateTime(format);
            instancesMap.put(format, newFromStringToLocalDateTime);
        }
        return instancesMap.get(format);
    }

    private FromStringToLocalDateTime(final String format){
        this.format = format;
    }

    @Override
    public Try<LocalDateTime> apply(String s) {
        return Try.go(() -> LocalDateTime.parse(s,DateTimeFormatter.ofPattern(format)));
    }
}

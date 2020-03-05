package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.Parser;
import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class LongParser implements Parser<String, Long> {

    private static class InstanceHolder {
        private static final LongParser instance = new LongParser();
    }

    public static LongParser getInstance() {
        return InstanceHolder.instance;
    }

    private LongParser(){}


    @Override
    public Try<Long> apply(
            final String s) {
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> Long.parseLong(s))
                .enrichException(ex -> ParseException.of(s, Long.class, ex));
    }
}

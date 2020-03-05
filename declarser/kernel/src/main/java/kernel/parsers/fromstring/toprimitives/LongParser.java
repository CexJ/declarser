package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class LongParser implements Function<String, Try<?>> {

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
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.go(() -> Long.parseLong(s))
                .enrichException(ex -> ParserException.of(s, Long.class, ex));
    }
}

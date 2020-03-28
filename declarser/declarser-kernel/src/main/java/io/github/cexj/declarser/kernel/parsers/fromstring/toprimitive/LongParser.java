package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

public final class LongParser implements Parser<String,Long> {

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
        else return Try.call(() -> Long.parseLong(s))
                .enrichException(ex -> ParserException.of(s, Long.class, ex));
    }
}

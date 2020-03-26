package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.function.Function;

public final class ShortParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final ShortParser instance = new ShortParser();
    }

    public static ShortParser getInstance() {
        return InstanceHolder.instance;
    }

    private ShortParser(){}

    @Override
    public Try<Short> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> Short.parseShort(s))
                .enrichException(ex -> ParserException.of(s, Short.class, ex));
    }
}

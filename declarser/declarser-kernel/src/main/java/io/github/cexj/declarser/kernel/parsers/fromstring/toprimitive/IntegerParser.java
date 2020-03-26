package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

public final class IntegerParser implements Parser<String> {

    private static class InstanceHolder {
        private static final IntegerParser instance = new IntegerParser();
    }

    public static IntegerParser getInstance() {
        return InstanceHolder.instance;
    }

    private IntegerParser(){}

    @Override
    public Try<Integer> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> Integer.parseInt(s))
                .enrichException(ex -> ParserException.of(s, Integer.class, ex));
    }
}

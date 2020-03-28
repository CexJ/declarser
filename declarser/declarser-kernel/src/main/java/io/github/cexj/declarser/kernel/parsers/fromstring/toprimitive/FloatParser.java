package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

public final class FloatParser implements Parser<String,Float> {

    private static class InstanceHolder {
        private static final FloatParser instance = new FloatParser();
    }

    public static FloatParser getInstance() {
        return InstanceHolder.instance;
    }

    private FloatParser(){}

    @Override
    public Try<Float> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> Float.parseFloat(s))
                .enrichException(ex -> ParserException.of(s, Float.class, ex));
    }
}

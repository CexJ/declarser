package io.github.cexj.declarser.kernel.parsers.fromstring.toprimitive;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;

public final class DoubleParser implements Parser<String> {

    private static class InstanceHolder {
        private static final DoubleParser instance = new DoubleParser();
    }

    public static DoubleParser getInstance() {
        return InstanceHolder.instance;
    }

    private DoubleParser(){}


    @Override
    public Try<Double> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> Double.parseDouble(s))
                .enrichException(ex -> ParserException.of(s, Double.class, ex));
    }
}

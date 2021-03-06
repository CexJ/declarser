package io.github.cexj.declarser.kernel.parsers.fromstring.tonumber;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.math.BigDecimal;

public final class BigDecimalParser implements Parser<String,BigDecimal> {

    private static class InstanceHolder {
        private static final BigDecimalParser instance = new BigDecimalParser();
    }

    public static BigDecimalParser getInstance() {
        return InstanceHolder.instance;
    }

    private BigDecimalParser(){}

    @Override
    public Try<BigDecimal> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> new BigDecimal(s))
                .enrichException(ex -> ParserException.of(s, BigDecimal.class, ex));
    }
}

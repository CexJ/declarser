package io.github.cexj.declarser.kernel.parsers.fromstring.tonumber;

import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.math.BigInteger;
import java.util.function.Function;

public final class BigIntegerParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final BigIntegerParser instance = new BigIntegerParser();
    }

    public static BigIntegerParser getInstance() {
        return InstanceHolder.instance;
    }

    private BigIntegerParser(){}

    @Override
    public Try<BigInteger> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> new BigInteger(s))
                .enrichException(ex -> ParserException.of(s, BigInteger.class, ex));
    }
}
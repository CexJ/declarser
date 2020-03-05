package kernel.parsers.fromstring.tonumber;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

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
        else return Try.go(() -> new BigInteger(s))
                .enrichException(ex -> ParserException.of(s, BigInteger.class, ex));
    }
}

package kernel.parsers.fromstring.tonumber;

import kernel.parsers.Parser;
import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

public final class BigIntegerParser implements Parser<String, BigInteger> {

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
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> new BigInteger(s))
                .enrichException(ex -> ParseException.of(s, BigInteger.class, ex));
    }
}

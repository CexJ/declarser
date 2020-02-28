package impl.stages.stage02_totypedmap.functions.fromString.tonumber;

import utils.tryapi.Try;

import java.math.BigInteger;
import java.util.function.Function;

public class BigIntegerParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final BigIntegerParser instance = new BigIntegerParser();
    }

    public static BigIntegerParser getInstance() {
        return InstanceHolder.instance;
    }

    private BigIntegerParser(){}

    @Override
    public Try<BigInteger> apply(String s) {
        return Try.go(() -> new BigInteger(s));
    }
}

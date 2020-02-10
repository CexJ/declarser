package impl.stages.stage02_totypedmap.functions.fromString.tonumber;

import utils.tryapi.Try;

import java.math.BigInteger;
import java.util.function.Function;

public class FromStringToBigInteger implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToBigInteger instance = new FromStringToBigInteger();
    }

    public static FromStringToBigInteger getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToBigInteger(){}

    @Override
    public Try<BigInteger> apply(String s) {
        return Try.go(() -> new BigInteger(s));
    }
}

package impl.stages.stage02_totypedmap.functions.fromString.tonumber;

import utils.tryapi.Try;

import java.math.BigDecimal;
import java.util.function.Function;

public class FromStringToBigDecimal implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FromStringToBigDecimal instance = new FromStringToBigDecimal();
    }

    public static FromStringToBigDecimal getInstance() {
        return InstanceHolder.instance;
    }

    private FromStringToBigDecimal(){}

    @Override
    public Try<BigDecimal> apply(String s) {
        return Try.go(() -> new BigDecimal(s));
    }
}

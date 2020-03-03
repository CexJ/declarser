package impl.stages.stage02_totypedmap.functions.fromString.tonumber;

import utils.tryapi.Try;

import java.math.BigDecimal;
import java.util.function.Function;

public final class BigDecimalParser implements Function<String, Try<?>> {

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
        return Try.go(() -> new BigDecimal(s));
    }
}

package kernel.parsers.fromstring.tonumber;

import kernel.parsers.Parser;
import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

public final class BigDecimalParser implements Parser<String, BigDecimal> {

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
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> new BigDecimal(s))
                .enrichException(ex -> ParseException.of(s, BigDecimal.class, ex));
    }
}

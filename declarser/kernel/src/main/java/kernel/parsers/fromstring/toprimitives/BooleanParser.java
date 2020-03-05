package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.Parser;
import kernel.parsers.exceptions.ParseException;
import kernel.tryapi.Try;

import java.math.BigInteger;
import java.util.function.Function;

public final class BooleanParser implements Parser<String, Boolean> {

    private static class InstanceHolder {
        private static final BooleanParser instance = new BooleanParser();
    }

    public static BooleanParser getInstance() {
        return InstanceHolder.instance;
    }

    private BooleanParser(){}

    @Override
    public Try<Boolean> apply(
            final String s) {
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> Boolean.parseBoolean(s))
                .enrichException(ex -> ParseException.of(s, Boolean.class, ex));
    }
}

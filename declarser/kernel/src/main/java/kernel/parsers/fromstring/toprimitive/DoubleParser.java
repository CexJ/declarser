package kernel.parsers.fromstring.toprimitive;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class DoubleParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final DoubleParser instance = new DoubleParser();
    }

    public static DoubleParser getInstance() {
        return InstanceHolder.instance;
    }

    private DoubleParser(){}


    @Override
    public Try<Double> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.go(() -> Double.parseDouble(s))
                .enrichException(ex -> ParserException.of(s, Double.class, ex));
    }
}

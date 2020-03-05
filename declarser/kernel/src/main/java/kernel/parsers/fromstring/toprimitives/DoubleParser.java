package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.exceptions.ParseException;
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
        if(s == null || s.isEmpty()) return null;
        else return Try.go(() -> Double.parseDouble(s))
                .enrichException(ex -> ParseException.of(s, Double.class, ex));
    }
}

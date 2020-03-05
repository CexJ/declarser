package kernel.parsers.fromstring.toprimitives;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class IntegerParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final IntegerParser instance = new IntegerParser();
    }

    public static IntegerParser getInstance() {
        return InstanceHolder.instance;
    }

    private IntegerParser(){}

    @Override
    public Try<Integer> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.go(() -> Integer.parseInt(s))
                .enrichException(ex -> ParserException.of(s, Integer.class, ex));
    }
}

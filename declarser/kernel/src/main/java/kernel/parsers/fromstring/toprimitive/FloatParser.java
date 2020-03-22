package kernel.parsers.fromstring.toprimitive;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import java.util.function.Function;

public final class FloatParser implements Function<String, Try<?>> {

    private static class InstanceHolder {
        private static final FloatParser instance = new FloatParser();
    }

    public static FloatParser getInstance() {
        return InstanceHolder.instance;
    }

    private FloatParser(){}

    @Override
    public Try<Float> apply(
            final String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        else return Try.call(() -> Float.parseFloat(s))
                .enrichException(ex -> ParserException.of(s, Float.class, ex));
    }
}

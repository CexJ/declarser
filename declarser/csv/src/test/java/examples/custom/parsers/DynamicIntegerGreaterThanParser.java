package examples.custom.parsers;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import javax.naming.directory.InvalidAttributesException;
import java.util.function.Function;

public class DynamicIntegerGreaterThanParser implements Function<String, Try<?>> {

    private int min;

    public DynamicIntegerGreaterThanParser(
            final int min){
        this.min = min;
    }

    @Override
    public Try<Integer> apply(String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        return  Try.call(() -> Integer.parseInt(s))
                .flatMap(v -> v <= min ? Try.fail(new InvalidAttributesException()) :  Try.success(v))
                .enrichException(ex -> ParserException.of(s, Integer.class, ex));
    }
}

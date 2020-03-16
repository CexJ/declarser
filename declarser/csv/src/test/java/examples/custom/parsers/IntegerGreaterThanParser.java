package examples.custom.parsers;

import kernel.parsers.exceptions.ParserException;
import kernel.tryapi.Try;

import javax.naming.directory.InvalidAttributesException;
import java.util.function.Function;

public class IntegerGreaterThanParser implements Function<String, Try<?>> {

    private int min;

    public IntegerGreaterThanParser(
            final String[] min){
        this.min = Integer.parseInt(min[0]);
    }

    @Override
    public Try<Integer> apply(String s) {
        if(s == null || s.isEmpty()) return Try.success(null);
        return  Try.go(() -> Integer.parseInt(s))
                .flatMap(v -> v <= min ? Try.fail(new InvalidAttributesException()) :  Try.success(v))
                .enrichException(ex -> ParserException.of(s, Integer.class, ex));
    }
}
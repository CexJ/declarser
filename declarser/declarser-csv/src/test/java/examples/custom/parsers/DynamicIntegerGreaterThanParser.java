package examples.custom.parsers;

import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.parsers.exceptions.ParserException;
import io.github.cexj.declarser.kernel.tryapi.Try;

import javax.naming.directory.InvalidAttributesException;


public class DynamicIntegerGreaterThanParser implements Parser<String,Integer> {

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

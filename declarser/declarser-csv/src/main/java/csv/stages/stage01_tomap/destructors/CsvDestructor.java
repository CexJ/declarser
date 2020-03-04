package csv.stages.stage01_tomap.destructors;

import kernel.stages.stage01_tomap.impl.destructor.Destructor;
import kernel.tryapi.Try;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CsvDestructor implements Destructor<String,Integer,String> {

    private final String cellSeparator;

    private CsvDestructor(
            final String cellSeparator) {
        this.cellSeparator = cellSeparator;
    }

    public static CsvDestructor of(
            final String cellSeparator){
        return new CsvDestructor(cellSeparator);
    }

    @Override
    public Try<Map<Integer, String>> destruct(
            final String input) {
        final var splittedInput = input.split(cellSeparator);
        return Try.success(Stream.iterate(0, i -> i+1)
                .limit(splittedInput.length)
                .collect(Collectors.toMap(Function.identity(), i -> splittedInput[i])));
    }
}

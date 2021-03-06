package io.github.cexj.declarser.csv.stages.stage01_tomap.destructors;

import io.github.cexj.declarser.csv.stages.stage01_tomap.exceptions.CsvNullInputException;
import io.github.cexj.declarser.kernel.stages.stage01_tomap.impl.destructor.Destructor;
import io.github.cexj.declarser.kernel.tryapi.Try;

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
        if(input == null) return Try.fail(CsvNullInputException.getInstance());
        final var splittedInput = input.split(cellSeparator);
        return Try.success(Stream.iterate(0, i -> i+1)
                .limit(splittedInput.length)
                .collect(Collectors.toMap(Function.identity(), i -> splittedInput[i])));
    }
}

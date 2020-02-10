package impl.stages.stage01_tomap.destructor;

import kernel.stages.stage01_tomap.destructor.Destructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvDestructor implements Destructor<String,Integer,String> {

    private final String cellSeparator;

    private CsvDestructor(final String cellSeparator) {
        this.cellSeparator = cellSeparator;
    }

    public static CsvDestructor of(final String cellSeparator){
        return new CsvDestructor(cellSeparator);
    }

    @Override
    public Map<Integer, String> destruct(final String input) {
        final String[] splittedInput = input.split(cellSeparator);
        return Stream.iterate(0, i -> i+1)
                .limit(splittedInput.length)
                .collect(Collectors
                .toMap(Function.identity(), i -> splittedInput[i]));
    }
}

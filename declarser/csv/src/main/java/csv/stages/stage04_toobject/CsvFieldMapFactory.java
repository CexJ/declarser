package csv.stages.stage04_toobject;

import kernel.tryapi.Try;

import java.util.Map;

public interface CsvFieldMapFactory {

    static CsvFieldMapFactory getInstance(){
        return CsvFieldMapFactoryImpl.getInstance();
    }

    <O> Try<Map<String, Integer>> mapFieldNameColumn(
            Class<O> clazz);
}

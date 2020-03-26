package io.github.cexj.declarser.csv.stages.stage04_toobject;

import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.Map;

public interface CsvFieldMapFactory {

    static CsvFieldMapFactory getInstance(){
        return CsvFieldMapFactoryImpl.getInstance();
    }

    <O> Try<Map<String, Integer>> mapFieldNameColumn(
            Class<O> clazz);
}

package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories;

import io.github.cexj.declarser.kernel.exceptions.GroupedException;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldComposer;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.fieldsutils.FieldsExtractor;
import io.github.cexj.declarser.kernel.stages.stage02_totypedmap.impl.trasformer.Transformer;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

final class CsvFunctionMapFactoryImpl implements CsvFunctionMapFactory {

    private final FieldComposer<Integer, String> functionComposer;
    private final FieldsExtractor fieldsExtractor;

    private CsvFunctionMapFactoryImpl(
            final FieldsExtractor fieldsExtractor,
            final FieldComposer<Integer, String> functionComposer) {
        this.functionComposer = functionComposer;
        this.fieldsExtractor = fieldsExtractor;
    }

    static CsvFunctionMapFactoryImpl of(
            final FieldsExtractor fieldsExtractor,
            final FieldComposer<Integer, String> functionComposer) {
        return new CsvFunctionMapFactoryImpl(fieldsExtractor,functionComposer);
    }



    @Override
    public Try<Map<Integer, Parser<String>>> mapColumnToTransformer(
            final Class<?> clazz){
        final var partition = fieldsExtractor.extract(clazz).stream()
                .map(functionComposer::compute)
                .collect(Collectors.partitioningBy(Try::isSuccess));

        final var success = partition.get(true);
        final var failures = partition.get(false);

        return failures.isEmpty() ? collectSuccessMap(success)
                                  : collectFailureMap(failures);
    }

    private Try<Map<Integer, Parser<String>>> collectFailureMap(
            final List<Try<Transformer<Integer,String>>> errors) {
        return Try.fail(GroupedException.of(errors.stream()
                .map(Try::getException)
                .collect(Collectors.toList())));
    }

    private Try<Map<Integer, Parser<String>>> collectSuccessMap(
            final List<Try<Transformer<Integer,String>>> fields) {
        return Try.success(fields.stream()
                .map(Try::getValue)
                .collect(Collectors.toMap(Transformer::getKey, Transformer::getFunction)));
    }
}


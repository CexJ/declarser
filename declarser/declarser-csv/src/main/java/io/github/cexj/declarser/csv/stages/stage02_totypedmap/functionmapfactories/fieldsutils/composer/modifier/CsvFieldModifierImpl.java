package io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier;

import io.github.cexj.declarser.csv.stages.annotations.fields.CsvArray;
import io.github.cexj.declarser.csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingArrayException;
import io.github.cexj.declarser.kernel.exceptions.GroupedException;
import io.github.cexj.declarser.kernel.parsers.Parser;
import io.github.cexj.declarser.kernel.tryapi.Try;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

final class CsvFieldModifierImpl implements CsvFieldModifier {

    private static class InstanceHolder {
        private static final CsvFieldModifierImpl instance = new CsvFieldModifierImpl();
    }

    static CsvFieldModifierImpl getInstance() {
        return CsvFieldModifierImpl.InstanceHolder.instance;
    }

    private CsvFieldModifierImpl(){}

    @Override
    public Try<UnaryOperator<Parser<String>>> compute(Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvArray.class))
                .map(arrayModifierFromField(field))
                .orElse(Try.success(UnaryOperator.identity()));
    }

    private Function<CsvArray, Try<UnaryOperator<Parser<String>>>> arrayModifierFromField(Field field) {
        return arr -> field.getType().isArray() ? Try.success(getArrayFunction(arr.value()))
                                                : Try.fail(MissingArrayException.of(field));
    }

    private UnaryOperator<Parser<String>> getArrayFunction(
            final String arraySeparator){
        return (Parser<String> fun) ->
                s -> combine(Arrays.stream(s.split(arraySeparator))
                        .map(fun)
                        .collect(Collectors.toList()))
                        .map(List::toArray);
    }

    private Try<List<?>> combine(
            final List<Try<?>> list){
        final var partition = list.stream()
                .collect(Collectors.partitioningBy(Try::isSuccess));

        final var success =  partition.get(true);
        final var failures =  partition.get(false);


        return failures.isEmpty() ? collectSuccessList(success)
                                  : collectFailureList(failures);
    }

    private Try<List<?>> collectSuccessList(
            final List<Try<?>> success) {
        return Try.success(success.stream()
                .map(Try::getValue)
                .collect(Collectors.toList()));
    }

    private Try<List<?>> collectFailureList(
            final List<Try<?>> failures) {
        return Try.fail(GroupedException.of(failures.stream()
                .map(Try::getException)
                .collect(Collectors.toList())));
    }

}

package csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.composer.modifier;

import csv.stages.annotations.fields.CsvArray;
import csv.stages.stage02_totypedmap.functionmapfactories.fieldsutils.exceptions.MissingArrayException;
import kernel.exceptions.GroupedException;
import kernel.tryapi.Try;

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
    public Try<UnaryOperator<Function<String, Try<?>>>> compute(Field field) {
        return Optional.ofNullable(field.getAnnotation(CsvArray.class))
                .map(arr -> field.getType().isArray() ? Try.success(getArrayFunction(arr.value()))
                                                      : Try.<UnaryOperator<Function<String, Try<?>>>>fail(MissingArrayException.of(field)))
                .orElse(                                Try.success(UnaryOperator.identity()));
    }

    private UnaryOperator<Function<String, Try<?>>> getArrayFunction(
            final String arraySeparator){
        return (Function<String, Try<?>> fun) ->
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

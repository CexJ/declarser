package impl;

import impl.stages.stage01_tomap.destructors.CsvDestructor;
import impl.stages.stage02_totypedmap.functionmapfactories.CsvFunctionMapFactory;
import impl.stages.stage03_combinator.combinators.NoExceptionCombinator;
import impl.stages.stage04_toobject.restructors.ReflectionRestructor;
import kernel.Declarser;
import kernel.conf.ParallelizationStrategyEnum;
import kernel.stages.stage01_tomap.ToMap;
import kernel.stages.stage01_tomap.destructor.Destructor;
import kernel.stages.stage02_totypedmap.ToTypedMap;
import kernel.stages.stage03_combinator.Combinator;
import kernel.stages.stage04_toobject.ToObject;
import kernel.stages.stage04_toobject.restructor.Restructor;
import kernel.validator.Validator;
import utils.tryapi.Try;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class CsvDeclarserFactory<O> implements BiFunction<Class<O>, String[], Declarser<String, Integer, String, O>> {

    private final String cellSeparator;
    private final ParallelizationStrategyEnum parallelizationStrategy;
    private final Supplier<O> outputSupplier;
    private final CsvFunctionMapFactory mapFunctionFactory;

    private CsvDeclarserFactory(String cellSeparator, ParallelizationStrategyEnum parallelizationStrategy, Supplier<O> outputSupplier, Map<Class<? extends Function<String, Try<?>>>, Function<String[], Function<String, Try<?>>>> customConstructorMap) {
        this.cellSeparator = cellSeparator;
        this.parallelizationStrategy = parallelizationStrategy;
        this.outputSupplier = outputSupplier;
        this.mapFunctionFactory =  CsvFunctionMapFactory.of(customConstructorMap);
    }

    @Override
    public Declarser<String, Integer, String, O> apply(final Class<O> clazz, final String[] params) {
        final Validator<String> preValidator = (Validator<String>) Validator.ok;
        final Validator<Map<Integer, String>> mapValidator = (Validator<Map<Integer, String>>)Validator.ok;
        final Validator<O> postValidator = (Validator<O>)Validator.ok;

        final Map<Integer, Function<String, Try<?>>> mapFunction = mapFunctionFactory.getMap(clazz);
        final Map<String,Integer> mapFileds = null;

        final Destructor destructor = CsvDestructor.of(cellSeparator);
        final ToMap<String, Integer, String> toMap = ToMap.of(preValidator, mapValidator, destructor);

        final ToTypedMap<Integer, String> toTypedMap = ToTypedMap.of(mapFunction, parallelizationStrategy);

        final Combinator<Integer> combinator = NoExceptionCombinator.of(parallelizationStrategy);

        final Restructor<Integer, O> restructor = ReflectionRestructor.of(clazz, outputSupplier, mapFileds);
        final ToObject<Integer, O>  toObject = ToObject.of(postValidator,restructor);

        return Declarser.of(toMap, toTypedMap,combinator,toObject);
    }
}

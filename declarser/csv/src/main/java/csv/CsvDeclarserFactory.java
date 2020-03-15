package csv;

import kernel.Declarser;
import kernel.enums.ParallelizationStrategyEnum;
import kernel.enums.SubsetType;
import kernel.tryapi.Try;
import kernel.validations.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface CsvDeclarserFactory {

    default <O> Try<Declarser<String, Integer, String, O>> declarserOf(
            final Class<O> clazz,
            final String cellSeparator){
        return declarserOf(clazz, o -> Optional.empty(), cellSeparator);
    }

    <O> Try<Declarser<String, Integer, String, O>> declarserOf(
            final Class<O> clazz,
            final Validator<O> postValidator,
            final String cellSeparator);

    static Builder builder(){
        return new Builder();
    }
    static CsvDeclarserFactory defaultFactory(){
        return new Builder().build();
    }
    final class Builder {

        private Builder(){}

        private ParallelizationStrategyEnum parallelizationStrategy = ParallelizationStrategyEnum.SEQUENTIAL;
        private Map<Class<? extends Validator<String>>,
                Function<String[], Validator<String>>> customPreValidatorsMap = new HashMap<>();
        private Map<Class<? extends Function<String, Try<?>>>,
                Function<String[], Function<String, Try<?>>>> customConstructorMap =  new HashMap<>();
        private SubsetType annotationsSubsetType = SubsetType.NONE;

        public Builder withParallelizationStrategy(
                final ParallelizationStrategyEnum parallelizationStrategy) {
            if(parallelizationStrategy != null) this.parallelizationStrategy = parallelizationStrategy;
            return this;
        }

        public Builder withCustomPreValidatorsMap(
                final Map<Class<? extends Validator<String>>,
                        Function<String[], Validator<String>>> customPreValidatorsMap) {
            if(customPreValidatorsMap != null) this.customPreValidatorsMap = customPreValidatorsMap;
            return this;
        }

        public Builder withCustomConstructorMap(
                final Map<Class<? extends Function<String, Try<?>>>,
                        Function<String[], Function<String, Try<?>>>> customConstructorMap) {
            if(customConstructorMap != null) this.customConstructorMap = customConstructorMap;
            return this;
        }

        public Builder withAnnotationsSubsetType(
                final SubsetType annotationsSubsetType) {
            if(annotationsSubsetType != null) this.annotationsSubsetType = annotationsSubsetType;
            return this;
        }

        public CsvDeclarserFactory build(){
            return CsvDeclarserFactoryImp.of(
                    parallelizationStrategy,
                    customPreValidatorsMap,
                    customConstructorMap,
                    annotationsSubsetType);
        }
    }
}

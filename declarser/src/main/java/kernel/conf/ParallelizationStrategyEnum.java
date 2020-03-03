package kernel.conf;

import java.util.stream.Stream;

public enum ParallelizationStrategyEnum {
    PARALLEL{
        @Override
        public <T> Stream<T> exec(
                final Stream<T> stream){
            return stream.parallel();
        }
    }, SEQUENTIAL;


    public <T> Stream<T> exec(
            final Stream<T> stream){
        return stream.sequential();
    }
}

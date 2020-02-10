package kernel.conf;

import java.util.stream.Stream;

public enum ParallelizationStrategy {
    PARALLEL{
        @Override
        public <T> Stream<T> exec(Stream<T> stream){
            return stream.parallel();
        }
    }, NOT_PARALLEL;


    public <T> Stream<T> exec(Stream<T> stream){
        return stream.sequential();
    }
}

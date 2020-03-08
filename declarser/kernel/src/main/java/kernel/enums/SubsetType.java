package kernel.enums;


import kernel.exceptions.FunctionTypeException;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public enum SubsetType {
    CONTAINED{
        public <T> Optional<FunctionTypeException> validation(Collection<T> first, Collection<T> second){
            final var notContained = first.stream().filter(f -> ! second.contains(f)).collect(Collectors.toList());
            if(notContained.isEmpty()) return Optional.empty();
            else return Optional.of(FunctionTypeException.of(first, second));
        }
    }
    , CONTAINS {
        public <T> Optional<FunctionTypeException> validation(Collection<T> first, Collection<T> second){
            final var notContained = second.stream().filter(s -> ! first.contains(s)).collect(Collectors.toList());
            if(notContained.isEmpty()) return Optional.empty();
            else return Optional.of(FunctionTypeException.of(first, second));
        }
    }, BIJECTIVE {
        public <T> Optional<FunctionTypeException> validation(Collection<T> first, Collection<T> second){
            return CONTAINED.validation(first, second).or(() ->
                    CONTAINS.validation(first, second));
        }
    }, NONE;

    public <T> Optional<FunctionTypeException> validation(Collection<T> first, Collection<T> second){
      return Optional.empty();
    }
}

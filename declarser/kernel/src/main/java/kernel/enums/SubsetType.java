package kernel.enums;

import kernel.exceptions.FunctionTypeException;
import kernel.exceptions.InjectionTypeException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum SubsetType {
    CONTAINED{
        public <T> Optional<FunctionTypeException> validation(Collection<T> first, Collection<T> second){
            final var notContained = first.stream().filter(f -> ! second.contains(f)).collect(Collectors.toList());
            if(notContained.isEmpty()) return Optional.empty();
            else return Optional.of(InjectionTypeException.of(first, second));
        }
    }
    , CONTAINS {

    }, BIJECTIVE {

    }, NONE;

    public <T> Optional<FunctionTypeException> validation(Collection<T> first, Collection<T> second){
      return Optional.empty();
    }
}

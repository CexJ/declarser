package io.github.cexj.declarser.kernel.enums;


import io.github.cexj.declarser.kernel.exceptions.SubsetTypeException;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public enum SubsetType {
    CONTAINED(true) {
        public <T> Optional<SubsetTypeException> validation(
                final Collection<T> first,
                final Collection<T> second){
            final var notContained = first.stream().filter(f -> ! second.contains(f)).collect(Collectors.toList());
            if(notContained.isEmpty()) return Optional.empty();
            else return Optional.of(SubsetTypeException.of(first, second));
        }},
    CONTAINS(false){
        public <T> Optional<SubsetTypeException> validation(
        final Collection<T> first,
        final Collection<T> second){
            final var notContained = second.stream().filter(s -> ! first.contains(s)).collect(Collectors.toList());
            if(notContained.isEmpty()) return Optional.empty();
            else return Optional.of(SubsetTypeException.of(first, second));
        }},
    BIJECTIVE(true) {
        public <T> Optional<SubsetTypeException> validation(
                final Collection<T> first,
                final Collection<T> second){
            return CONTAINED.validation(first, second).or(() ->
                    CONTAINS.validation(first, second));
        }},
    NONE(false);

    private final boolean strict;

    SubsetType(
            final boolean strict) {
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    public <T> Optional<SubsetTypeException> validation(
            final Collection<T> first,
            final Collection<T> second){
      return Optional.empty();
    }
}

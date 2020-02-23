package impl.stages.stage04_toobject;

import impl.stages.annotations.fields.CsvField;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CsvFieldMapFactory {

    public <O> Map<String, Integer> getMap(Class<O> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(f -> NameKey.of(f.getName(), f.getAnnotation(CsvField.class).key()))
                .filter(nk -> nk.getKey().isPresent())
                .collect(Collectors.toMap(NameKey::getName, nk -> nk.getKey().get()));
    }
}

class NameKey{
    private final String name;
    private final Integer key;

    private NameKey(final String name, final Integer key) {
        this.name = name;
        this.key = key;
    }

    public static NameKey of(final String name, final Integer key) {
        return new NameKey(name,key);
    }

    public String getName() {
        return name;
    }

    public Optional<Integer> getKey() {
        return Optional.ofNullable(key);
    }
}

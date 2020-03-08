package kernel.stages.stage04_toobject.impl.restructor.impl;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kernel.enums.SubSetType;
import kernel.stages.stage04_toobject.impl.restructor.Restructor;
import kernel.exceptions.GroupedException;
import kernel.tryapi.Try;

public final class ReflectionRestructor<K,O> implements Restructor<K,O> {

	private final Class<O> clazz;
	private final Map<String,K> mapFileds;
	private final SubSetType inputMapType;

	private ReflectionRestructor(
			final Class<O> clazz,
			final Map<String,K> mapFileds,
			final SubSetType inputMapType) {
		this.clazz = clazz;
		this.mapFileds = mapFileds;
		this.inputMapType = inputMapType;
	}

	public static <K,O> Try<ReflectionRestructor<K,O>> of(
			final Class<O> clazz,
			final Map<String,K> mapFileds,
			final SubSetType inputMapType,
			final SubSetType fieldMapType){
		final var fieldsname = Stream.of(clazz.getDeclaredFields())
				.map(Field::getName)
				.collect(Collectors.toSet());
		return fieldMapType.validation(mapFileds.keySet(), fieldsname)
				.map(Try::<ReflectionRestructor<K,O>>fail)
				.orElse(Try.success(new ReflectionRestructor<>(clazz, mapFileds,inputMapType)));
	}

	@Override
	public Try<O> restruct(
			final Map<K,?> input){
		return inputMapType.validation(input.keySet(), mapFileds.values())
				.map(Try::<O>fail)
				.orElse(Try.go(() -> clazz.getConstructor().newInstance())
						.flatMap( value -> injectValues(input, value)));
	}

	private Try<O> injectValues(Map<K, ?> input, O value) {
		final List<Exception> exceptions = new LinkedList<>();
		Stream.of(clazz.getDeclaredFields())
				.peek(f -> f.setAccessible(true))
				.forEach(f -> {
					try {
						f.set(value, input.get(mapFileds.get(f.getName())) );
					} catch (Exception e) {
						exceptions.add(e);
					}
				});
		return exceptions.isEmpty() ? Try.success(value)
				                    : Try.fail(GroupedException.of(exceptions));
	}
}



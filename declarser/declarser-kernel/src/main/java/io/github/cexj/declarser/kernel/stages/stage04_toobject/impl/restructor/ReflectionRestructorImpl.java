package io.github.cexj.declarser.kernel.stages.stage04_toobject.impl.restructor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.cexj.declarser.kernel.enums.SubsetType;
import io.github.cexj.declarser.kernel.exceptions.GroupedException;
import io.github.cexj.declarser.kernel.tryapi.Try;

public final class ReflectionRestructorImpl<K,O> implements Restructor<K,O> {

	private final Class<O> clazz;
	private final Map<String,K> mapFileds;
	private final SubsetType inputMapType;

	private ReflectionRestructorImpl(
			final Class<O> clazz,
			final Map<String,K> mapFileds,
			final SubsetType inputMapType) {
		this.clazz = clazz;
		this.mapFileds = mapFileds;
		this.inputMapType = inputMapType;
	}

	static <K,O> Try<Restructor<K,O>> of(
			final Class<O> clazz,
			final Map<String,K> mapFields,
			final SubsetType inputMapType,
			final SubsetType fieldMapType){
		final var fieldsname = Stream.of(clazz.getDeclaredFields())
				.map(Field::getName)
				.collect(Collectors.toSet());
		return fieldMapType.validation(mapFields.keySet(), fieldsname)
				.map(Try::<Restructor<K,O>>fail)
				.orElse(Try.success(new ReflectionRestructorImpl<>(clazz, mapFields,inputMapType)));
	}

	@Override
	public Try<O> restruct(
			final Map<K,?> input){
		return inputMapType.validation(input.keySet(), mapFileds.values())
				.map(Try::<O>fail)
				.orElse(Try.call(() -> clazz.getConstructor().newInstance())
						.flatMap( value -> injectValues(input, value)));
	}

	private Try<O> injectValues(
			final Map<K, ?> input,
			final O value) {
		final List<Exception> exceptions = Stream.of(clazz.getDeclaredFields())
				.peek(f -> f.setAccessible(true))
				.map(f -> Try.run(() ->
						f.set(value, f.getType().isArray() ? arrayValue(input, f)
							                               : input.get(mapFileds.get(f.getName())))))
				.filter(Try::isFailure)
				.map(Try::getException)
				.collect(Collectors.toList());
		return exceptions.isEmpty() ? Try.success(value)
				                    : Try.fail(GroupedException.of(exceptions));
	}

	private Object arrayValue(Map<K, ?> input, Field f) {
		Object fieldValue;
		final var objectArray = (Object[])input.get(mapFileds.get(f.getName()));
		final var type = f.getType().getComponentType();
		fieldValue = typeArray(type, objectArray);
		return fieldValue;
	}


	@SuppressWarnings("unchecked")
	private <T> T[] typeArray(
			final Class<T> clazz,
			final Object[] array){
		T[] newArray =(T[]) clazz.arrayType().cast(Array.newInstance(clazz, array.length));
		Stream.iterate(0, i -> i+1).limit(array.length)
				.forEach(i -> newArray[i] = (T) array[i]);
		return newArray;
	}
}



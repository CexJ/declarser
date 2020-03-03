package impl.stages.stage04_toobject.restructors;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import kernel.stages.stage04_toobject.restructor.Restructor;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

public final class ReflectionRestructor<K,O> implements Restructor<K,O> {

	private final Class<O> clazz;
	private final Map<String,K> mapFileds;

	private ReflectionRestructor(
			final Class<O> clazz,
			final Map<String,K> mapFileds) {
		this.clazz = clazz;
		this.mapFileds = mapFileds;
	}

	public static <K,O> ReflectionRestructor<K,O> of(
			final Class<O> clazz,
			final Map<String,K> mapFileds){
		return new ReflectionRestructor<>(clazz, mapFileds);
	}

	@Override
	public Try<O> restruct(
			final Map<K,?> input){
		final var value = Try.go(() -> clazz.getConstructor().newInstance());

		final List<Exception> exceptions = new LinkedList<>();
		value.ifPresent(object -> Stream.of(clazz.getDeclaredFields())
				.peek(f -> f.setAccessible(true))
				.forEach(f -> {
					try {
						f.set(object, input.get(mapFileds.get(f.getName())) );
					} catch (Exception e) {
						exceptions.add(e);
					}
				}));

		return exceptions.isEmpty() ? value
				                    : Try.fail(GroupedException.of(exceptions));
	}
}



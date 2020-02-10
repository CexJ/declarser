package impl.stages.stage04_toobject.restructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import kernel.stages.stage04_toobject.restructor.Restructor;
import utils.exceptions.GroupedException;
import utils.tryapi.Try;

public class ReflectionRestructor<K,O> implements Restructor<K,O> {

	private final Class<O> clazz;
	private final Supplier<O> supplier;
	private final Map<String,K> mapFileds;

	private ReflectionRestructor(final Class<O> clazz, final Supplier<O> supplier, final Map<String,K> mapFileds) {
		this.clazz = clazz;
		this.supplier = supplier;
		this.mapFileds = mapFileds;
	}

	public static <K,O> ReflectionRestructor<K,O> of(final Class<O> clazz, final Supplier<O> supplier, final Map<String,K> mapFileds){
		return new ReflectionRestructor<>(clazz, supplier, mapFileds);
	}

	@Override
	public Try<O> restruct(final Map<K,?> input){
		final List<IllegalAccessException> exceptions = new LinkedList<>();
		final O object = supplier.get();

		Stream.of(clazz.getDeclaredFields())
				.peek(f -> f.setAccessible(true))
				.forEach(f -> {
					try {
						f.set(object, input.get(mapFileds.get(f.getName())));
					} catch (IllegalAccessException e) {
						exceptions.add(e);
					}
				});

		return exceptions.isEmpty() ? Try.success(object)
				                    : (Try<O>) Try.fail(GroupedException.of(exceptions));
	}
}



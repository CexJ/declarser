package declarser.toobject.restructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import declarser.typedvalue.TypedValue;
import utils.tryapi.Try;

public class Restructor<K,O> {

	private final Class<O> clazz;
	private final Supplier<O> supplier;
	private final Map<String,K> mapFileds;

	private Restructor(Class<O> clazz, Supplier<O> supplier, Map<String,K> mapFileds) {
		this.clazz = clazz;
		this.supplier = supplier;
		this.mapFileds = mapFileds;
	}

	public static <K,O> Restructor<K,O> of(Class<O> clazz, Supplier<O> supplier, Map<String,K> mapFileds){
		return new Restructor<>(clazz, supplier, mapFileds);
	}

	public Try<O> restruct(final Map<K,TypedValue<?>> input){
		final List<IllegalAccessException> exceptions = new LinkedList<>();
		O object = supplier.get();
		Stream.of(clazz.getDeclaredFields())
				.peek(f -> f.setAccessible(true))
				.forEach(f -> {
					try {
						f.set(object, input.get(mapFileds.get(f.getName())));
					} catch (IllegalAccessException e) {
						exceptions.add(e);
					}
				});

		Try<O> result;
		if(exceptions.isEmpty()){
			result = Try.success(object);
		} else {
			IllegalAccessException ex = exceptions.get(0);
			for(int i = 1; i < exceptions.size(); i++) {
				ex.addSuppressed(exceptions.get(i));
			}
			result = (Try<O>) Try.fail(ex);
		}

		return result;
	}
}

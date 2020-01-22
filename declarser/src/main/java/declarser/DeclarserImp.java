package declarser;

import declarser.tomap.destructor.Destructor;
import declarser.toobject.restructor.Restructor;

public final class DeclarserImp<I,K,V,O> {

	private final Destructor<I,K,V> destructor;
	private final Restructor<K,V,O> reconstructor;
	
	private DeclarserImp(Destructor<I, K, V> destructor, Restructor<K, V, O> reconstructor) {
		super();
		this.destructor = destructor;
		this.reconstructor = reconstructor;
	}
	
	public static <I,K,V,O> DeclarserImp<I,K,V,O> of(Destructor<I, K, V> destructor, Restructor<K, V, O> reconstructor) {
		return new DeclarserImp<>(destructor, reconstructor);
	}
}

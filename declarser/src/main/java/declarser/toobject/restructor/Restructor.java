package declarser.toobject.restructor;

import java.util.Map;

import utils.tryapi.Try;

public interface Restructor<K,V,O> {
	
	Try<O> restruct(Map<K,Map<K,Try<V>>> map);
}

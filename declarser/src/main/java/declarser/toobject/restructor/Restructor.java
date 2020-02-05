package declarser.toobject.restructor;

import java.util.Map;

import declarser.typedvalue.TypedValue;
import utils.tryapi.Try;

public interface Restructor<K,O> {
	
	Try<O> restruct(final Map<K,TypedValue<?>> map);
}

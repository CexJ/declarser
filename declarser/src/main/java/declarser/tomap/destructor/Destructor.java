package declarser.tomap.destructor;

import java.util.Map;

public interface Destructor<I,K,V> {

	Map<K,V> destruct(I input);
}

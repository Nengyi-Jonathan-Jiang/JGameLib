package jGameLib.util.caches;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A cache, used to store the results of expensive computations.
 */
@SuppressWarnings("unused")
public class Cache<K, V> {
    protected final Map<K, V> backingMap;

    public Cache() {
        this(HashMap::new);
    }

    public Cache(Supplier<Map<K, V>> backingMap) {
        this.backingMap = backingMap.get();
    }

    public V getOrCompute(K key, Supplier<V> computation) {
        if (backingMap.containsKey(key)) {
            return backingMap.get(key);
        }
        V value = computation.get();
        putValue(key, value);
        return value;
    }

    public V getOrCompute(K key, Function<K, V> computation) {
        return getOrCompute(key, () -> computation.apply(key));
    }

    public V get(K key) {
        return backingMap.get(key);
    }

    public void putValue(K key, V value) {
        backingMap.put(key, value);
    }
}

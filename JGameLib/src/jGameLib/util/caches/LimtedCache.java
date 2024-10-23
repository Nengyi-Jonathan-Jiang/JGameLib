package jGameLib.util.caches;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a cache with a maximum size
 */
@SuppressWarnings("unused")
public class LimtedCache<K, V> extends Cache<K, V> {
    private int limit = 0;

    public LimtedCache(int limit) {
        super(LinkedHashMap::new);
        setLimit(limit);
    }

    public void setLimit(int limit) {
        this.limit = Math.max(limit, 1);
    }

    @Override
    public void putValue(K key, V value) {
        super.putValue(key, value);

        var backingMap = (LinkedHashMap<K, V>) this.backingMap;
        while (backingMap.size() > limit) {
            Iterator<Map.Entry<K, V>> it = backingMap.entrySet().iterator();
            it.next();
            it.remove();
        }
    }
}

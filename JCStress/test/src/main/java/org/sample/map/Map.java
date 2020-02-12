package org.sample.map;

public interface Map<K, V> {
    V put(K key, V value);

    V get(Object key);

    V remove(Object key);

    int size();
}

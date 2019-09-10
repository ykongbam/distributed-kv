package com.distributedStore.kvStore;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

public interface KVStore<K,V> {
    V get(K key);
    void put(K key, V value);
    Map<K, V> getMap();
}

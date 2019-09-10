package com.distributedStore.kvStore.dataProvider;

import com.distributedStore.kvStore.KVStore;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */
@AllArgsConstructor
public class AtomicDataProvider<K,V> implements KVStore<K, V> {
    private ConcurrentHashMap<K, V> concurrentHashMap;

    @Override
    public V get(K key) {
        return concurrentHashMap.get(key);
    }

    @Override
    public void put(K key, V value) {
        concurrentHashMap.put(key, value);
    }

    @Override
    public Map<K, V> getMap() {
        return concurrentHashMap;
    }
}

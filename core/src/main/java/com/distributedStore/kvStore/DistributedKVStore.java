package com.distributedStore.kvStore;

import com.distributedStore.kvStore.partitionLocator.PartitionLocator;
import com.distributedStore.kvStore.partitionProxy.PartitionProxy;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

@AllArgsConstructor
public class DistributedKVStore<K, V> implements KVStore<K, V> {
    private PartitionLocator<K, V> partitionLocator;

    @Override
    public V get(K key) {
        PartitionProxy<K, V> partition = partitionLocator.getPartition(key);
        return partition.get(key);
    }

    @Override
    public void put(K key, V value) {
        PartitionProxy<K, V> partition = partitionLocator.getPartition(key);
        partition.put(key, value);
    }

    @Override
    public Map<K, V> getMap() {
        return null;
    }
}

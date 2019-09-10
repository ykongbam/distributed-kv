package com.distributedStore.kvStore.partitionLocator;

import com.distributedStore.kvStore.partitionProxy.PartitionProxy;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

@AllArgsConstructor
public class HashBucketedPartitionLocator<K, V> implements PartitionLocator<K, V> {
    private ConcurrentHashMap<Integer, PartitionProxy<K, V>> partitionBuckets;

    public PartitionProxy<K, V> getPartition(K key) {
        int bucket = key.hashCode() % partitionBuckets.size();
        return partitionBuckets.get(bucket);
    }
}

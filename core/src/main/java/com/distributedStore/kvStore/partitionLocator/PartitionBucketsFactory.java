package com.distributedStore.kvStore.partitionLocator;

import com.distributedStore.kvStore.partitionProxy.PartitionProxy;
import com.distributedStore.kvStore.partitionProxy.PartitionProxyProvider;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */
@AllArgsConstructor
public class PartitionBucketsFactory<K, V> {
    private PartitionProxyProvider<K, V> partitionProxyProvider;

    public ConcurrentHashMap<Integer, PartitionProxy<K, V>> createPartitionBuckets(int bucketSize) {
        ConcurrentHashMap<Integer, PartitionProxy<K, V>> partitionMap = new ConcurrentHashMap<>();
        for (int i = 0; i < bucketSize; i++) {
            partitionMap.put(i, partitionProxyProvider.getPartitionProxy(i));
        }
        return partitionMap;
    }
}

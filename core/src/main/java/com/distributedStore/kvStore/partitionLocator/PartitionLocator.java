package com.distributedStore.kvStore.partitionLocator;

import com.distributedStore.kvStore.partitionProxy.PartitionProxy;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

public interface PartitionLocator<K, V> {
    PartitionProxy<K, V> getPartition(K key);
}

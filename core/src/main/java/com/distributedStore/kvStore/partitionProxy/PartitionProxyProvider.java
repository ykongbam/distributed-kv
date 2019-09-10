package com.distributedStore.kvStore.partitionProxy;

import com.distributedStore.kvStore.cluster.ClusterManager;
import lombok.AllArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

@AllArgsConstructor
public class PartitionProxyProvider<K, V> {
    private ClusterManager<K, V> clusterManager;

    public PartitionProxy<K, V> getPartitionProxy(int i) {
        return new PartitionProxy<>(clusterManager, i);
    }
}

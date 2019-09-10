package com.distributedStore.kvStore.partitionProxy;

import com.distributedStore.kvStore.KVStore;
import com.distributedStore.kvStore.cluster.ClusterManager;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

@AllArgsConstructor
public class PartitionProxy<K, V> implements KVStore<K, V> {
//    private ArrayBlockingQueue<Tuple<K,V>> proxyQueue;
    private ClusterManager<K, V> clusterManager;
    private Integer partitionId;

    public V get(K key) {
        KVStore<K, V> kvStore = clusterManager.getOwner(partitionId);
        return kvStore.get(key);
    }

    public void put(K key, V value) {
        KVStore<K, V> kvStore = clusterManager.getOwner(partitionId);
        kvStore.put(key, value);
    }

    @Override
    public Map<K, V> getMap() {
        KVStore<K, V> kvStore = clusterManager.getOwner(partitionId);
        return kvStore.getMap();
    }
}

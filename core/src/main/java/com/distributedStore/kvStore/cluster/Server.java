package com.distributedStore.kvStore.cluster;

import com.distributedStore.kvStore.KVStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Server<K, V> {
    @Getter
    @EqualsAndHashCode.Include
    private int id;
    @Getter
    private ConcurrentHashMap<Integer, KVStore<K, V>> ownedPartitions;
    @Getter
    private boolean active;

    public Server(int id) {
        this.id = id;
    }

    public synchronized void start() {
        ownedPartitions = new ConcurrentHashMap<>();
    }

    public void addPartition(Integer partitionId, KVStore<K, V> kvStore) {
        ownedPartitions.put(partitionId, kvStore);
    }

    public KVStore<K, V> getPartition(int partitionId) {
        return ownedPartitions.get(partitionId);
    }

    public synchronized void stop() {
        active = false;
    }

    public void removePartition(Integer partitionId) {
        ownedPartitions.remove(partitionId);
    }

    @Override
    public String toString() {
        return "" + id;
    }
}

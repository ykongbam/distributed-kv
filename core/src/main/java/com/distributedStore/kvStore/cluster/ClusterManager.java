package com.distributedStore.kvStore.cluster;

import com.distributedStore.kvStore.KVStore;
import com.distributedStore.kvStore.dataProvider.AtomicDataProvider;
import javafx.util.Pair;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

@Slf4j
public class ClusterManager<K, V> {
    @Getter
    private ConcurrentHashMap<Integer, Server<K, V>> partitions;
    @Getter
    private List<Server<K, V>> servers;
    private int serverId = 0;
    private int partitionCount;

    public void initCluster(int initialSize, int partitionCount) {
        partitions = new ConcurrentHashMap<>();
        this.partitionCount = partitionCount;
        servers = new ArrayList<>();
        for (int i = 0; i < initialSize; i++) {
            Server<K, V> server = new Server<>(getServerId());
            server.start();
            servers.add(server);
        }
        for (int partitionId = 0; partitionId < partitionCount; partitionId++) {
            int bucket = partitionId % initialSize;
            Server<K, V> server = servers.get(bucket);
            partitions.put(partitionId, server);
            server.addPartition(partitionId, new AtomicDataProvider<>(new ConcurrentHashMap<>()));
        }
    }

    private synchronized int getServerId() {
        return serverId++;
    }

    public KVStore<K, V> getOwner(Integer partitionId) {
        Server<K, V> server = partitions.get(partitionId);
        return server.getPartition(partitionId);
    }

    public synchronized int addServer() {
        Server<K, V> server = new Server<>(getServerId());
        server.start();
        servers.add(server);
        return server.getId();
    }

    public synchronized void rebalance() {
        Map<Integer, Integer> partitionMap = new HashMap<>();
        List<Pair<Integer, Integer>> deletionList = new ArrayList<>();
        int counter=0;
        //Copy to servers
        int activeServersCount = servers.size();
        for (int partitionId = 0; partitionId < partitionCount; partitionId++) {
            int bucket = partitionId % activeServersCount;
            Server<K, V> newServer = servers.get(bucket);
            int newServerId = newServer.getId();
            int oldServerId = partitions.get(partitionId).getId();
            partitionMap.put(partitionId, newServerId);
            if (oldServerId != newServerId) {
                deletionList.add(new Pair<>(partitionId, oldServerId));
                log.info(partitionId + " " + oldServerId);
            }
            Server<K, V> oldServer = findServer(oldServerId);
            newServer.addPartition(partitionId, oldServer.getPartition(partitionId));
        }
        //Fix Pointers
        for (int partitionId = 0; partitionId < partitionCount; partitionId++) {
            partitions.put(partitionId, findServer(partitionMap.get(partitionId)));
        }
        log.info("size " + deletionList.size());
        //Remove from servers
        deletionList.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue)).forEach((partitionId, serverId) -> {
            findServer(serverId).removePartition(partitionId);
        });
    }

    private Server<K, V> findServer(int serverId) {
        return servers.stream().filter(s -> s.getId() == serverId).findFirst().get();
    }

    public synchronized void removeServer(int serverId) {
        servers.remove(serverId);
    }
}

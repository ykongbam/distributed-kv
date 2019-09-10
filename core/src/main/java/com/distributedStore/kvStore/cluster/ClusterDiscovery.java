package com.distributedStore.kvStore.cluster;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

public class ClusterDiscovery<K, V> {
    private Set<Server<K, V>> servers;
    private Map<Server<K, V>, Long> serverHearbeats;
    private ClusterManager<K, V> clusterManager;

    public ClusterDiscovery(ClusterManager<K, V> clusterManager) {
        this.clusterManager = clusterManager;
    }

    public void initCluster(){
        serverHearbeats = new ConcurrentHashMap<>();
        servers = new ConcurrentSkipListSet<>(Comparator.comparingInt(Server::getId));

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(heartBeatListener(),
                1, 1, TimeUnit.SECONDS);
    }

    private Runnable heartBeatListener() {
        return () -> {
                    servers.forEach(server -> {
                        if (server.isActive()) {
                            serverHearbeats.put(server, 0L);
                        }
                        else {
                            serverHearbeats.put(server, serverHearbeats.get(server) + 1);
                        }
                        if (serverHearbeats.get(server) > 10) {
                            clusterManager.removeServer(server.getId());
                        }
                    });
                };
    }
}

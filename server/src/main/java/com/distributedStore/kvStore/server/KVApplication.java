package com.distributedStore.kvStore.server;

import com.distributedStore.kvStore.DistributedKVStore;
import com.distributedStore.kvStore.cluster.ClusterDiscovery;
import com.distributedStore.kvStore.cluster.ClusterManager;
import com.distributedStore.kvStore.partitionLocator.HashBucketedPartitionLocator;
import com.distributedStore.kvStore.partitionLocator.PartitionBucketsFactory;
import com.distributedStore.kvStore.partitionLocator.PartitionLocator;
import com.distributedStore.kvStore.partitionProxy.PartitionProxy;
import com.distributedStore.kvStore.partitionProxy.PartitionProxyProvider;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import com.distributedStore.kvStore.server.resources.KVStoreResource;
import com.distributedStore.kvStore.server.resources.ServerResource;
import com.distributedStore.kvStore.server.resources.SnapshotResource;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

public class KVApplication extends Application<KVConfiguration> {
    private static final int PARTITION_COUNT = 12;
    private static final int SERVER_COUNT = 2;

    public static void main(String[] args) throws Exception {
        new KVApplication().run(args);
    }

    @Override
    public void run(KVConfiguration kvConfiguration, Environment environment) throws Exception {
        ClusterManager<String, String> clusterManager = new ClusterManager<>();
        clusterManager.initCluster(SERVER_COUNT, PARTITION_COUNT);
        ClusterDiscovery<String, String> clusterDiscovery = new ClusterDiscovery<>(clusterManager);
        PartitionBucketsFactory<String, String> partitionBucketsFactory = new PartitionBucketsFactory<>(
                new PartitionProxyProvider<>(clusterManager)
        );
        ConcurrentHashMap<Integer, PartitionProxy<String, String>> partitionBuckets =
                partitionBucketsFactory.createPartitionBuckets(PARTITION_COUNT);
        PartitionLocator<String, String> partitionLocator = new HashBucketedPartitionLocator<>(partitionBuckets);
        DistributedKVStore<String, String> dataStore = new DistributedKVStore<>(partitionLocator);
        seedData(dataStore);
        KVStoreResource kvStoreResource = new KVStoreResource(dataStore);
        SnapshotResource snapshotResource = new SnapshotResource(clusterManager);
        ServerResource serverResource = new ServerResource(clusterManager);
        environment.jersey().register(kvStoreResource);
        environment.jersey().register(snapshotResource);
        environment.jersey().register(serverResource);
    }

    private void seedData(DistributedKVStore<String, String> dataStore) {
        for (int i = 0; i < 120; i++) {
            dataStore.put("k" + i, "v" + i);
        }
    }
}

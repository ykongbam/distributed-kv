package com.distributedStore.kvStore;

import com.distributedStore.kvStore.cluster.ClusterManager;
import com.distributedStore.kvStore.partitionLocator.HashBucketedPartitionLocator;
import com.distributedStore.kvStore.partitionLocator.PartitionLocator;
import com.distributedStore.kvStore.partitionLocator.PartitionBucketsFactory;
import com.distributedStore.kvStore.partitionProxy.PartitionProxy;
import com.distributedStore.kvStore.partitionProxy.PartitionProxyProvider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */

public class DataStoreApplication {
    private static final int PARTITION_COUNT = 10;
    private static final int SERVER_COUNT = 5;

    public static void main(String[] args) {
        ClusterManager<String, String> clusterManager = new ClusterManager<>();
        clusterManager.initCluster(SERVER_COUNT, PARTITION_COUNT);
        PartitionBucketsFactory<String, String> partitionBucketsFactory = new PartitionBucketsFactory<>(
                new PartitionProxyProvider<>(clusterManager)
        );
        ConcurrentHashMap<Integer, PartitionProxy<String, String>> partitionBuckets =
                partitionBucketsFactory.createPartitionBuckets(PARTITION_COUNT);
        PartitionLocator<String, String> partitionLocator = new HashBucketedPartitionLocator<>(partitionBuckets);
        DistributedKVStore<String, String> dataStore = new DistributedKVStore<>(partitionLocator);
        dataStore.put("key", "value");
        System.out.println(dataStore.get("key"));
    }
}

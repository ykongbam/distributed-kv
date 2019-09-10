# Key Value Store
## Introduction
## Use cases
### Key Flows
#### Get Value
For a key, fetch the value of the key.
- Identify the partition the key is present in.
- Identify the server the partition is present in.
- Fetch the value of the key from the partition.

#### Put Value
For a key, if value not present add value else update the value. 
- Identify the partition the key is present in.
- Identify the server the partition is present on.
- If the key exists then update value for the key
- If the key doesn't exist then add key and value

#### Rebalance Partitions
When a new server is added or removed, the partitions need to be re-balanced.
Each server is assigned a bucketId. A BucketId is of the range 0 to noOfServers and is independent of the serverId. 

Based on a bucketing logic, the partitions are bucketed into buckets.

There are two phases of re-balancing, 
- The partition is copied from the old bucket to the new bucket.
- The partition is then deleted from the old bucket.

*Open Questions*
- Where to store the mapping of the partition to ?   

#### Repartition
A repartition happens when the number of partitions changes. This is a very expensive operation where the keys are distributed accross partitions.

#### Add Server
On addition of a server, a serverID is assigned to the new server and partition rebalancing happens.

#### Remove Server
On server removal, rebalancing happens with the replicated partitions of the partitions on the deleted server.

#### Replicate Partition
Every update to a partition is replicated to a replicated partition.


## Design
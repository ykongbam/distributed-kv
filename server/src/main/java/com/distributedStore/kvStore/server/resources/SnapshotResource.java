package com.distributedStore.kvStore.server.resources;

import com.distributedStore.kvStore.cluster.ClusterManager;
import lombok.AllArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */
@Path("snapshot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class SnapshotResource {
    private ClusterManager<String, String> clusterManager;

    @GET
    public Response getSnapShot() {
        Map<Integer, Map<Integer, Map<String, String>>> content = new HashMap<>();
        clusterManager.getServers().forEach(server -> {
            Map<Integer, Map<String, String>> partitionData = server.getOwnedPartitions()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getMap()));
            content.put(server.getId(), partitionData);
        });
        return Response
                .status(Response.Status.OK)
                .entity(content)
                .build();
    }
}

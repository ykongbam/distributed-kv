package com.distributedStore.kvStore.server.resources;

import com.distributedStore.kvStore.cluster.ClusterManager;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */
@Path("servers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class ServerResource {
    private ClusterManager<String, String> clusterManager;

    @GET
    @Path("add")
    public Response addServer() {
        int serverId = clusterManager.addServer();
        clusterManager.rebalance();
        return Response
                .status(Response.Status.OK)
                .entity("Successfully added server with id: " + serverId)
                .build();
    }

    @GET
    @Path("remove/{serverId}")
    public Response removeServer(@PathParam ("serverId")@NotNull Integer serverId) {
        clusterManager.removeServer(serverId);
        return Response
                .status(Response.Status.OK)
                .entity("Successfully removed server with id: " + serverId)
                .build();
    }
}

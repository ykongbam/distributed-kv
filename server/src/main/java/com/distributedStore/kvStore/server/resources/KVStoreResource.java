package com.distributedStore.kvStore.server.resources;

import com.distributedStore.kvStore.DistributedKVStore;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: ykongbam (ykongbam@gmail.com)
 * Date: 31/05/19
 */
@Path("map")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class KVStoreResource {
    private DistributedKVStore<String, String> distributedKVStore;

    @GET
    public Response get(@QueryParam("k") String key) {
        String response = distributedKVStore.get(key);
        return Response
                .status(Response.Status.OK)
                .entity(response)
                .build();
    }

    @GET
    @Path("update")
    public Response get(@QueryParam("k") String key, @QueryParam("v") String value) {
        distributedKVStore.put(key, value);
        return Response
                .status(Response.Status.OK)
                .entity(value)
                .build();
    }
}

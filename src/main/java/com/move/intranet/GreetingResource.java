package com.move.intranet;

import java.net.URI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class GreetingResource {

    @GET
    public Response root() {
        return Response.seeOther(URI.create("/Home/index")).build();
    }
}

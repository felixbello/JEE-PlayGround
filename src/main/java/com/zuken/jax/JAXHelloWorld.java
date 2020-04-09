package com.zuken.jax;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class JAXHelloWorld {

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMsg(){
        String result = "Hello World";
        return Response.status(200).entity(result).build();
    }
}

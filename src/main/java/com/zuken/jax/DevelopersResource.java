package com.zuken.jax;

import jdk.nashorn.internal.objects.annotations.Getter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("developers")
public class DevelopersResource {

    @GET
    public String developers(){
        return "duchess,duke";
    }

    @GET
    @Path("{first}-{last}")
    public String developer(@PathParam("first") String first, @PathParam("last") String last){
        return first + "_" + last;
    }
}

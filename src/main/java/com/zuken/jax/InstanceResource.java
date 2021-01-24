package com.zuken.jax;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path( "instance" )
public class InstanceResource {

    @GET
    public String developer(@Context HttpHeaders http){
        return "Instance Servlet Up and Running";
    }
}

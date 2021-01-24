package com.zuken.jax;

import jdk.nashorn.internal.objects.annotations.Getter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("developers")
public class DevelopersResource {

    @GET
    public String developers(@Context HttpHeaders http){
//        return "duchess, duke";
        return "duchess, duke";
    }

    @GET
    @Path("{first}-{last}")
    public Developer developer(@PathParam("first") String first, @PathParam("last") String last){
        return new Developer(first,last);
    }

    @GET
    @Path("{first}/{lang}")
    public String getLang(@PathParam("first") String first, @PathParam("lang") String lang) {
        if(first.contains("duke") && lang.contains("java")){
            return "its duke with java";
        } else if(first.contains("felix") && lang.contains("python")){
            return "its felix with python";
        } else {
            return "nothing existing about it";
        }
    }

    @GET
    @Path( "{first}/{second}/{third}" )
    public String testingThree(@PathParam( "first" ) String first, @PathParam( "second" ) String second, @PathParam( "third" ) String third){
        System.out.println("loooooks not bad");
        return first + " " + second + " " + third;
    }
}

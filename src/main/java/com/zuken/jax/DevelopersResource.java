package com.zuken.jax;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Path("developers")
public class DevelopersResource {

    private static HttpTransport httpTransport;
    private static final String APPLICATION_NAME = "";
    /** Set PROJECT_ID to your Project ID from the Overview pane in the Developers console. */
    private static final String PROJECT_ID = "online-school-labs";

    /** Set Compute Engine zone. */
    private static final String ZONE_NAME = "europe-west6-a";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Set the name of the sample VM instance to be created. */
    private static final String SAMPLE_INSTANCE_NAME = "my-sample-instance";

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

    @GET
    @Path("{print}")
    public String printInstanc(@PathParam( "print" ) String print){

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            GoogleCredentials credential = Helper.getGoogleCredentials();

            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);
            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            // List out instances, looking for the one created by this sample app.
            boolean foundOurInstance = printInstances(compute);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return "hopefully successuflly";
    }



    public static boolean printInstances(Compute compute) throws IOException {
        System.out.println("================== Listing Compute Engine Instances ==================");
        Compute.Instances.List instances = compute.instances().list(PROJECT_ID, ZONE_NAME);
        InstanceList list = instances.execute();
        boolean found = false;

        for (Instance instance: list.getItems()) {
            System.out.println(instance.getName());
        }
        return found;
    }
}

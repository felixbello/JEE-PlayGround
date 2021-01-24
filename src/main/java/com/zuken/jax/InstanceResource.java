package com.zuken.jax;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;
import com.google.api.services.compute.model.Operation;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Path( "instance" )
public class InstanceResource {

    private static HttpTransport httpTransport;
    private static final String APPLICATION_NAME = "";
    /** Set PROJECT_ID to your Project ID from the Overview pane in the Developers console. */
    private static final String PROJECT_ID = "online-school-labs";

    /** Set Compute Engine zone. */
    private static final String ZONE_NAME = "europe-west6-a";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @GET
    public String instance(@Context HttpHeaders http){
        return "Instance Servlet Up and Running";
    }

    @GET
    @Path("{name}")
    public String getStatus(@PathParam("name") String name) {

        String found = null;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            Compute.Instances.Get request = compute.instances().get(PROJECT_ID, ZONE_NAME, name);

            Instance response = request.execute();

            found = response.getStatus();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    @GET
    @Path("{zone}/{name}/start")
    public String start(@PathParam( "zone" ) String zone, @PathParam("name") String name) {

        String found = null;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            Compute.Instances.Start request = compute.instances().start(PROJECT_ID,zone, name);


            Operation response = request.execute();

            found = response.getStatus();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }
    @GET
    @Path("{zone}/{name}/stop")
    public String stop(@PathParam( "zone" ) String zone, @PathParam("name") String name) {

        String found = null;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            Compute.Instances.Stop request = compute.instances().stop(PROJECT_ID,zone, name);

            Operation response = request.execute();

            found = response.getStatus();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    @GET
    @Path( "{zone}/list" )
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ZukenInstance> list(@PathParam( "zone" ) String  zone) {

        ArrayList<ZukenInstance> found;
        found = new ArrayList<>();

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            Compute.Instances.List request = compute.instances().list( PROJECT_ID, zone);

            InstanceList response = request.execute();

            for (Instance instance:response.getItems()) {
                ZukenInstance zukenInstance = new ZukenInstance();
                zukenInstance.setName( instance.getName() );
                zukenInstance.setStatus( instance.getStatus() );
                zukenInstance.setZone( instance.getZone() );
                found.add( zukenInstance );
//               found.add(new ZukenInstance(instance.getName()));
            }


        } catch (Exception e){
            e.printStackTrace();
        }

        return found;
    }

}

package com.zuken.jax.resources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Operation;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.zuken.jax.Helper;
import com.zuken.jax.models.ZukenInstance;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Path( "disk" )
public class Disk {

    private static HttpTransport httpTransport;
    private static final String APPLICATION_NAME = "";
    /** Set PROJECT_ID to your Project ID from the Overview pane in the Developers console. */
    private static final String PROJECT_ID = "online-school-labs";

    /** Set Compute Engine zone. */
    private static final String ZONE_NAME = "europe-west6-a";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @GET
    public String disk(@Context HttpHeaders http){
        return "Disk Servlet Up and Running";
    }

    @GET
    @Path("{zone}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean exists(@PathParam( "zone" ) String zone,@PathParam("name") String name ){

        boolean exists = false;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            Compute.Disks.Get diskRequest = compute.disks().get(PROJECT_ID, zone, name);

            com.google.api.services.compute.model.Disk diskResponse = diskRequest.execute();

        } catch (GeneralSecurityException e) {
            if (e.getMessage().contains( "not found" )){
                exists = true;
            }
        } catch (IOException e) {
            if (e.getMessage().contains( "not found" ) ){
                exists = true;
            }
        }

        return exists;
    }

//    @GET
//    @Path( "{zone}/{name}/delete" )
//    public String delete(@PathParam( "zone" ) String zone, @PathParam( "name" ) String name){
//       String res = null;
//       try {
//           GoogleCredentials credentials = Helper.getGoogleCredentials();
//           httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//           HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );
//
//           // Create Compute Engine object for listing instances.
//           Compute compute =
//                   new Compute.Builder(httpTransport, JSON_FACTORY, requestInitializer)
//                           .setApplicationName(APPLICATION_NAME)
//                           .build();
//
//           Compute.Instances.Delete request = compute.instances().delete( PROJECT_ID, zone, name );
//
//           Operation response = request.execute();
//
//           res = response.getStatus();
//
//       } catch (GeneralSecurityException e) {
//           e.printStackTrace();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//
//       return res;
//    }

}

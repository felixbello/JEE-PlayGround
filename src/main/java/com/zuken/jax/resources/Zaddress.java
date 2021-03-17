package com.zuken.jax.resources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.AddressList;
import com.google.api.services.compute.model.Region;
import com.google.api.services.compute.model.RegionList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.zuken.jax.Helper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("address")
public class Zaddress {

    private static HttpTransport httpTransport;
    private static final String APPLICATION_NAME = "";
    /**
     * Set PROJECT_ID to your Project ID from the Overview pane in the Developers console.
     */
    private static final String PROJECT_ID = "online-school-labs";

    /**
     * Set Compute Engine zone.
     */
    private static final String ZONE_NAME = "europe-west6-a";

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @GET
    public String instance(@Context HttpHeaders http) {
        return "Address Servlet Up and Running";
    }

    @GET
    @Path("{region}/list")
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, String> list(@PathParam( "region" ) String region) {

        HashMap<String, String> addresses = new HashMap<String, String>();

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute computeService =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();



            Compute.Addresses.List request = computeService.addresses().list(PROJECT_ID, region);

            AddressList addresslist = request.execute();

            for (com.google.api.services.compute.model.Address add: addresslist.getItems()) {
                addresses.put( add.getName(), add.getAddress() );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return addresses;
    }
}

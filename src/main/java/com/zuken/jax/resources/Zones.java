package com.zuken.jax.resources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Region;
import com.google.api.services.compute.model.RegionList;
import com.google.api.services.compute.model.Zone;
import com.google.api.services.compute.model.ZoneList;
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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

@Path("zones")
public class Zones {

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
        return "Regions Servlet Up and Running";
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, String> list() {

        HashMap<String, String> regions = new HashMap<String, String>();

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute computeService =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            Compute.Regions.List regionsRequest = computeService.regions().list( PROJECT_ID );

            RegionList response = regionsRequest.execute();

            for (Region item : response.getItems()) {
                if (item.getName().equals( "europe-west6" )) {
                    regions.put( "SWITZERLAND", item.getName() );
                } else if (item.getName().equals( "europe-west3" )) {
                    regions.put( "GERMANY", item.getName() );
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return regions;
    }

    @GET
    @Path("{region}/list")
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, Integer> listFromRegion(@PathParam("region") String region) {

        HashMap<String, Integer> zoneList = new HashMap<String, Integer>();

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute computeService =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            Compute.Zones.List requestZones = computeService.zones().list( PROJECT_ID );

            ZoneList responseZones = requestZones.execute();

            for (Zone item : responseZones.getItems()) {
                String zone = item.getRegion().substring( item.getRegion().lastIndexOf( "/" ) + 1 );
                if (zone.equals( region )) {
                    zoneList.put( item.getName(), 0 );
                }
            }

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return zoneList;
    }
}

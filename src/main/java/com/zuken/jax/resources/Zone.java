package com.zuken.jax.resources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Region;
import com.google.api.services.compute.model.RegionList;
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
import java.util.*;

@Path("zone")
public class Zone {

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
    public String zone(@Context HttpHeaders http) {
        return "Zone Servlet Up and Running";
    }


    @GET
    @Path( "{region}/select" )
    public String selectZone(@PathParam( "region" ) String region){

        String selectedZone = null;

        HashMap<String, Integer> zoneList = listFromRegion( region );

        List<Map.Entry<String, Integer>> list = new ArrayList<>(zoneList.entrySet());

        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        } );

        selectedZone = list.get( 0 ).getKey();

        return selectedZone;
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

            for (com.google.api.services.compute.model.Zone item : responseZones.getItems()) {
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

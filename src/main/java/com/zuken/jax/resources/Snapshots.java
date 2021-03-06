package com.zuken.jax.resources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.*;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.Zone;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.zuken.jax.Helper;
import com.zuken.jax.models.ZukenInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("snapshots")
public class Snapshots {

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
    public String snapshots(@Context HttpHeaders http) {
        return "Snapshots Servlet Up and Running";
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<String> list() {

        ArrayList<String> snapshotsList = new ArrayList<>();

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
            ZoneList zoneResponse = requestZones.execute();

            Compute.Snapshots.List requestSnapshots = computeService.snapshots().list(PROJECT_ID);
            SnapshotList snapshotsResponse = requestSnapshots.execute();

            if (snapshotsResponse.getItems() != null){
                for (Snapshot item : snapshotsResponse.getItems()) {
                    snapshotsList.add(item.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return snapshotsList;
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

package com.zuken.jax.resources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Path("instance")
public class Instance {

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
        return "Instance Servlet Up and Running";
    }

    @GET
    @Path("{zone}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ZukenInstance get(@PathParam("zone") String zone, @PathParam("name") String name) {

        ZukenInstance instance = null;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            Compute.Instances.Get request = compute.instances().get( PROJECT_ID, zone, name );

            com.google.api.services.compute.model.Instance response = request.execute();

            instance = new ZukenInstance();
            instance.setName( response.getName() );
            instance.setStatus( response.getStatus() );
            instance.setZone( response.getZone() );

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instance;
    }

    @GET
    @Path("{zone}/{name}/delete")
    public String delete(@PathParam("zone") String zone, @PathParam("name") String name) {
        String res = null;
        try {
            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            System.out.println( "**********************" );
            System.out.println( "Instance should be going to be deleted" );
            Compute.Instances.Delete request = compute.instances().delete( PROJECT_ID, zone, name );

            Operation response = request.execute();
            System.out.println( "Instace should be deleted" );

            System.out.println( "prepare region" );

            String region = null;

            System.out.println(zone);

            if (zone.contains( "/" )) {
                region = zone.substring( zone.lastIndexOf( "/" ), zone.lastIndexOf( "-" ) );
            } else if (zone.contains( "a" ) || zone.contains( "b" ) || zone.contains( "c" )) {
                region = zone.substring( 0, zone.lastIndexOf( "-" ) );
            } else {
                region = zone;
            }

            System.out.println( region );
            System.out.println( "prepared" );

            System.out.println( "prepare to delete Address" );
            Compute.Addresses.Delete requestDeleteAddress = compute.addresses().delete( PROJECT_ID, region, name);
            Operation responseDelete = requestDeleteAddress.execute();
            System.out.println( "address should be deleted" );

            res = response.getStatus() + " *** " + responseDelete.getStatus();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    @GET
    @Path("{zone}/{name}/start")
    public String start(@PathParam("zone") String zone, @PathParam("name") String name) {

        String found = null;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            Compute.Instances.Start request = compute.instances().start( PROJECT_ID, zone, name );


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
    public String stop(@PathParam("zone") String zone, @PathParam("name") String name) {

        String found = null;

        try {

            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            Compute.Instances.Stop request = compute.instances().stop( PROJECT_ID, zone, name );

            Operation response = request.execute();

            found = response.getStatus();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    @POST
    @Path("model/inst")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String posting(ZukenInstance[] instance) {
        String name = null;
        boolean diskCreated = false;
        try {
            GoogleCredentials credentials = Helper.getGoogleCredentials();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter( credentials );

            // Create Compute Engine object for listing instances.
            Compute compute =
                    new Compute.Builder( httpTransport, JSON_FACTORY, requestInitializer )
                            .setApplicationName( APPLICATION_NAME )
                            .build();

            System.out.println( "**********************************" );
            System.out.println( "Got Request!!" );
            System.out.println( "**********************************" );
            System.out.println( "Size: " + instance.length );

            for (int i = 0; i <= instance.length - 1; i++) {

                System.out.println( "Name: " + instance[i].getName() );
                System.out.println( "Zone: " + instance[i].getZone() );
                System.out.println( "Template: " + instance[i].getTemplate() );

                String respCreated = Disk.create( instance[i].getZone(), instance[i].getTemplate(), instance[i].getName() );
                System.out.println("Creation of Disk started " +respCreated);

                int count = 0;
                while (!diskCreated) {
                    diskCreated = Disk.statusCreated( instance[i].getZone(), instance[i].getTemplate(), instance[i].getName() );
                    TimeUnit.SECONDS.sleep( 30);
                    count++;
                    if (count > 3){
                        Disk.create( instance[i].getZone(), instance[i].getTemplate(), instance[i].getName() );
                        count = 0;
                    }
                    System.out.println( "Disk: " + instance[i].getName() + " not yet created" );
                }

                System.out.println( "Disk: " + instance[i].getName() + " created" );

                com.google.api.services.compute.model.Instance requestInstanceBody = new com.google.api.services.compute.model.Instance();

                requestInstanceBody.setName( instance[i].getName() );
                requestInstanceBody.setMachineType( "projects/online-school-labs/zones/" + instance[i].getZone() + "/machineTypes/e2-standard-8" );
                requestInstanceBody.setZone( "projects/online-school-labs/zones/" + instance[i].getZone() );

                List<NetworkInterface> networkInterfacesList = new ArrayList<>();
                NetworkInterface networkInterface = new NetworkInterface();
                networkInterface.setKind( "compute#networkInterface" );

                String ipAddress = createFixedIP( compute, instance[i].getCountry(), instance[i].getName() );

                List<AccessConfig> accessConfigsList = new ArrayList<>();
                AccessConfig accessConfig = new AccessConfig();
                accessConfig.setKind( "compute#accessConfig" );
                accessConfig.setName( "External NAT" );
                accessConfig.setType( "ONE_TO_ONE_NAT" );
                accessConfig.setNatIP( ipAddress );
                accessConfigsList.add( accessConfig );

                networkInterface.setAccessConfigs( accessConfigsList );

                networkInterfacesList.add( networkInterface );

                requestInstanceBody.setNetworkInterfaces( networkInterfacesList );

                // Disk
                List<AttachedDisk> diskList = new ArrayList<>();
                AttachedDisk bootDisk = new AttachedDisk();
                bootDisk.setBoot( true );
                bootDisk.setSource( "projects/online-school-labs/zones/" + instance[i].getZone() + "/disks/" + instance[i].getName() );
                bootDisk.setAutoDelete( true );
                diskList.add( bootDisk );

                requestInstanceBody.setDisks( diskList );

                Compute.Instances.Insert insertInstance = compute.instances().insert( PROJECT_ID, instance[i].getZone(), requestInstanceBody );
                Operation requestInstance = insertInstance.execute();

                diskCreated = false;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }

    private String createFixedIP(Compute compute, String region, String name) {
        String ipAddress = null;

        boolean created = false;

        try {
            Address requestAddress = new Address();

            requestAddress.setName( name );
            Compute.Addresses.Insert request = compute.addresses().insert( PROJECT_ID, region, requestAddress );
            System.out.println( "Try to create network" );
            Operation response = request.execute();

            while (!created) {
                created = checkFixedIPCreated( compute, PROJECT_ID, region, name );
                TimeUnit.SECONDS.sleep( 3 );
            }
            System.out.println( "Network should be created" );

            Compute.Addresses.Get reqAdd = compute.addresses().get( PROJECT_ID, region, name );
            Address respAdd = reqAdd.execute();

            ipAddress = respAdd.getAddress();

            System.out.println( "Network created: " + ipAddress );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return ipAddress;
    }

    private boolean checkFixedIPCreated(Compute compute, String projectId, String region, String name) {
        boolean created = false;

        try {
            Compute.Addresses.Get reqAdd = compute.addresses().get( projectId, region, name );
            Address respAdd = reqAdd.execute();
            if (respAdd.getStatus().equals( "RESERVED" )) {
                created = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return created;
    }


//    private static void createInstance(Compute computeService) throws IOException, InterruptedException {

//        boolean diskCreated = false;
//        String zone = "europe-west6-a";
//        String region = null;
//
//        for (int i = 0; i < 6; i++) {
//            if (regions == null){
//                regions = getRegionsList( computeService );
//            }
//
//            region = regions.get( "GERMANY" );
//
//            zonList = getZonesListFromRegion( computeService, region );
//
//            zone = selectZone(zonList);
//
//            createDisk( computeService, PROJECT_ID, zone, "app-" + i );
//
//            while (!(diskCreated)) {
//                diskCreated = checkStatus( computeService, PROJECT_ID, zone, "app-" + i );
//                System.out.println( "still not ready... wait" );
//                TimeUnit.SECONDS.sleep( 3 );
//            }
//
//
//            com.google.api.services.compute.model.Instance requestInstanceBody = new com.google.api.services.compute.model.Instance();
//            requestInstanceBody.setName( "testing-app-" + i );
//            requestInstanceBody.setMachineType( "projects/online-school-labs/zones/europe-west6-a/machineTypes/e2-medium" );
//            requestInstanceBody.setMachineType( "projects/online-school-labs/zones/"+zone+"/machineTypes/e2-standard-8" );
//            requestInstanceBody.setZone( "projects/online-school-labs/zones/" + zone );
//
//            List<NetworkInterface> netWorkInterfacesList = new ArrayList<>();
//            NetworkInterface networkInterface = new NetworkInterface();
//            networkInterface.setKind( "compute#networkInterface" );
//
//            String ipAddress = createFixedIP( computeService, region, "testing-app-" + i );
//            List<AccessConfig> accessConfigList = new ArrayList<>();
//            AccessConfig accessConfig = new AccessConfig();
//            accessConfig.setKind( "compute#accessConfig" );
//            accessConfig.setName( "External NAT" );
//            accessConfig.setType( "ONE_TO_ONE_NAT" );
//            accessConfig.setNatIP( ipAddress );
//            accessConfigList.add( accessConfig );
//
//            networkInterface.setAccessConfigs( accessConfigList );
//
//            netWorkInterfacesList.add( networkInterface );
//
//            requestInstanceBody.setNetworkInterfaces( netWorkInterfacesList );
//
//             Disk
//            List<AttachedDisk> diskList = new ArrayList<>();
//            AttachedDisk bootD = new AttachedDisk();
//            bootD.setBoot( true );
//            bootD.setSource( "projects/online-school-labs/zones/" + zone + "/disks/" + "app-" + i );
//            bootD.setAutoDelete( true );
//            diskList.add( bootD );
//
//            requestInstanceBody.setDisks( diskList );
//
//            Compute.Instances.Insert instInsert = computeService.instances().insert( PROJECT_ID, zone, requestInstanceBody );
//            Operation reqInst = instInsert.execute();
//
//            diskCreated = false;
//        }
//    }

}

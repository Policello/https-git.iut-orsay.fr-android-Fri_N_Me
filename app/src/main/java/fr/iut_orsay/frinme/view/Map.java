package fr.iut_orsay.frinme.view;

import android.Manifest;
import android.app.Fragment;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import fr.iut_orsay.frinme.R;

public class Map extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Map";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;

    private ArrayList<LatLng> tab;

    //MarkerOptions currentPosition;
    private Marker myLocattionMarker;
    private boolean firstLocationUpdate = true;

    private final int AUTHORIZED_LOCATION = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_maps, container, false);



        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(2 * 1000); // seconds, in milliseconds

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);

        tab = new ArrayList<LatLng>();

        //Nord
        tab.add(new LatLng(48.908612, 2.439712));
        //Ouest
        tab.add(new LatLng(48.804865, 2.120355));
        //Est
        tab.add(new LatLng(48.848579, 2.5526099999999587));


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (LatLng l : tab) {
            addMarkerLatLng(l, BitmapDescriptorFactory.HUE_GREEN);
            Log.i("marker ", "" + l.latitude);
        }


    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

            if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                handleNewLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
            }
            else{
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AUTHORIZED_LOCATION);
            }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this.getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
        if (firstLocationUpdate) {

            myLocattionMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("me"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
            firstLocationUpdate = false;
        } else {
            myLocattionMarker.setPosition(myLoc);
        }
        //currentPosition = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(""+ location.getLatitude() + " ; " +location.getLongitude());

        //mMap.addMarker(currentPosition);
    }


    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void addMarkerLatLng(LatLng l, float couleur) {
        MarkerOptions options = new MarkerOptions().position(l).title("" + l.latitude + " ; " + l.longitude).icon(BitmapDescriptorFactory.defaultMarker(couleur));
        ;
        mMap.addMarker(options);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AUTHORIZED_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mGoogleApiClient.isConnected()) {
                        // Permsission granted
                        try {
                            Log.i(TAG, "API Connected");
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                            handleNewLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

                        } catch (SecurityException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    //System.exit(0);
                    Log.d(TAG, "Error permissions not granted");
                }
            }
        }
    }
}

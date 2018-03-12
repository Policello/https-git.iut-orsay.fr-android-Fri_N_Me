package fr.iut_orsay.frinme.view;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import fr.iut_orsay.frinme.SettingsActivity;


/**
 * Vue principale de l'application
 * carte et gestion de la localisation
 */
public class Map extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Map";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private LocationManager lm;
    private ArrayList<LatLng> tab;
    private Activity mActivity;

    private Marker myLocattionMarker;
    private boolean firstLocationUpdate = true;

    private final int AUTHORIZED_LOCATION = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        //Gestionnaire de localisation pour verifier que la localisation est activée
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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

        //Placer les autres marqueurs
        for (LatLng l : tab) {
            addMarkerLatLng(l, BitmapDescriptorFactory.HUE_GREEN);
            Log.i("marker ", "" + l.latitude);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        //Localisation activée ?
        boolean isLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (checkedPlayService(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()))) {

            if (!isLocationEnabled){
                redirectToSettings("Location not activated");
            }

            else {
                //Voir si on  la permission
                if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //Mettre à jour la localisation
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    Log.d("Location", "Location");

                    //Attendre 2 secondes que la localisation se mettre à jour
                    new Thread() {
                        public void run() {

                            getActivity().runOnUiThread(() -> {
                                //On ne fait rien, on attend
                            });
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    //Re-positionner sur la carte
                    handleNewLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
                } else {
                    //Demander la permission
                    ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUTHORIZED_LOCATION);
                }
            }
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

    /**
     *
     * @param location : nnew location to mark
     */
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());

        /*
         * Réglage de la caméra :
         *  - si c'est la première fois, c'est le placement sur la carte, on centre sur le marqueur
         *  - sinon, c'est une mise à jour, on ne bouge pas la caméra
         */
        if (firstLocationUpdate) {

            myLocattionMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("me"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
            firstLocationUpdate = false;
        } else {
            myLocattionMarker.setPosition(myLoc);
        }
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

    /**
     * Marche à suivre selon le statut des Google Play Services
     * @param PLAY_SERVICE_STATUS
     * @return true si il n'y a pas de problème
     *         redirection dans tous les cas
     *         false si rien n'est fait
     *
     */
    private boolean checkedPlayService(int PLAY_SERVICE_STATUS)
    {
        switch (PLAY_SERVICE_STATUS)
        {
            case ConnectionResult.API_UNAVAILABLE:
                //API is not available
                redirectToSettings("API is not available");
                break;
            case ConnectionResult.NETWORK_ERROR:
                //Network error while connection
                redirectToSettings("API is not available");
                break;
            case ConnectionResult.RESTRICTED_PROFILE:
                //Google Profile is restricted
                redirectToSettings("API is not available");
                break;
            case ConnectionResult.SERVICE_MISSING:
                //service is missing
                redirectToSettings("API is not available");
                break;
            case ConnectionResult.SIGN_IN_REQUIRED:
                //service available but user not signed in
                redirectToSettings("API is not available");
                break;
            case ConnectionResult.SUCCESS:
                return true;
                //break;
        }
        return false;
    }


    /**
     * Redirige vers les paramètres pour tout problème de configuration
     * @param text : texte à afficher à l'utilisateur
     */
    private void redirectToSettings(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        Log.d("TAG", text);
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
    }

}

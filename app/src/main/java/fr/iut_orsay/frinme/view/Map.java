package fr.iut_orsay.frinme.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.iut_orsay.frinme.R;
import fr.iut_orsay.frinme.SettingsActivity;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.EventModel;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
import fr.iut_orsay.frinme.rest.pojo.EventListDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private  List<ContactModel> tabContacts;
    private List<EventModel> tabEventJO;
    private  List<EventModel> tabEventUser;

    TextView dialog_msg, dialog_title, dialog_ok;
    Dialog dialog;
    View view;

    private Location location;

    private Marker myLocattionMarker;
    private boolean firstLocationUpdate = true;

    private final int AUTHORIZED_LOCATION = 1;

    private boolean networkEnabled;

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
        tabContacts = new ArrayList<ContactModel>();
        tabEventUser = new ArrayList<EventModel>();
        tabEventJO = new ArrayList<EventModel>();

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
        Log.d(TAG, "onmapready");
        //Placer les autres marqueurs


        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.set
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            //return;
        }*/
    }

    public void showDialog(String  content) {

        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
// 1.
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_dialog, null);

        dialog_msg = (TextView) layout.findViewById(R.id.dialog_msg);
        dialog_title = (TextView) layout.findViewById(R.id.dialog_title);
        dialog_ok = (TextView) layout.findViewById(R.id.dialog_ok);
        dialog_title.setText("information");
        dialog_msg.setText(content);
        dialog_ok.setText("voir les details");

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                这个MainActivity换成你要跳到的界面
                Intent intent =null;

                switch(content)
                {
                    case "test":
                        intent = new Intent(getActivity(), Event.class);
                        break;
                    case "test2":
                        intent = new Intent(getActivity(), Contact.class);
                        break;
                }

                intent.putExtra("data", dialog_msg.getText().toString());
                startActivity(intent);
                dialog.dismiss();
                getActivity().finish();

            }
        });

        builder.setView(layout);
        dialog=builder.create();
        dialog.show();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");


        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //Localisation activée
        //networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        final ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = connMgr.getActiveNetworkInfo();
        networkEnabled = net != null && net.isConnected();


        if (!networkEnabled) {
            redirectToSettings("Network not activated");
        } else {
            if (checkedPlayService(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()))) {

                int locationMode = getLocationMode(getActivity());
                Log.d(TAG, "Location Mode" + locationMode);
                Log.i(TAG, "network activated : " + networkEnabled);


                if (locationMode != 3) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {

                    //Voir si on  la permission
                    if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Mettre à jour la localisation
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        //Remplir tableaux
                        fetchContacts();
                        fetchEvents();



                    } else {
                        //Demander la permission
                        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUTHORIZED_LOCATION);
                    }
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
        Log.d(TAG, "" + firstLocationUpdate);
        if (firstLocationUpdate) {

            myLocattionMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("me"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            firstLocationUpdate = false;
        } else {
            myLocattionMarker.setPosition(myLoc);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onchanged");
        handleNewLocation(location);
    }

    public void addMarkerLatLng(LatLng l, float couleur) {
        Geocoder gcd = new Geocoder(this.getActivity(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(l.latitude, l.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkerOptions options;

        if (addresses.get(0) != null) {
             options = new MarkerOptions().position(l).title("" + addresses.get(0).getLocality()).icon(BitmapDescriptorFactory.defaultMarker(couleur));
        }

        else {
             options = new MarkerOptions().position(l).title("titre").icon(BitmapDescriptorFactory.defaultMarker(couleur));
        }
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
                            //handleNewLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

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
     *
     * @param PLAY_SERVICE_STATUS
     * @return true si il n'y a pas de problème
     * redirection dans tous les cas
     * false si rien n'est fait
     */
    private boolean checkedPlayService(int PLAY_SERVICE_STATUS) {
        switch (PLAY_SERVICE_STATUS) {
            case ConnectionResult.API_UNAVAILABLE:
                //API is not available
                redirectToSettings("API is not available");
                break;
            case ConnectionResult.NETWORK_ERROR:
                //Network error while connection
                redirectToSettings("Network error while connection");
                break;
            case ConnectionResult.RESTRICTED_PROFILE:
                //Google Profile is restricted
                redirectToSettings("Google Profile is restricted");
                break;
            case ConnectionResult.SERVICE_MISSING:
                //service is missing
                redirectToSettings("service is missing");
                break;
            case ConnectionResult.SIGN_IN_REQUIRED:
                //service available but user not signed in
                redirectToSettings("service available but user not signed in");
                break;
            case ConnectionResult.SUCCESS:
                return true;
            //break;
        }
        return false;
    }


    /**
     * Redirige vers les paramètres pour tout problème de configuration
     *
     * @param text : texte à afficher à l'utilisateur
     */
    private void redirectToSettings(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        Log.d("TAG", text);
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
    }

    public int getLocationMode(Context c) {
        try {
            return Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            //return -1;
        }
        return -1;
    }

    private void fetchEvents() {
        Call<EventListDetails> call = RestUser.get().getEventDetailedList();
        call.enqueue(new Callback<EventListDetails>() {
            @Override
            public void onResponse(Call<EventListDetails> call, Response<EventListDetails> response) {
                if (response.isSuccessful()) {
                    final EventListDetails r = response.body();
                    Log.e(TAG, r.getEvents().toString());
                    tabEventUser.addAll(r.getEvents());
                    tabEventJO.addAll(r.getEventsJo());
                    for (EventModel e : tabEventUser) {
                        addMarkerLatLng(new LatLng(e.getCoordonnées().getLatitude(), e.getCoordonnées().getLongitude()), BitmapDescriptorFactory.HUE_GREEN);
                        //Log.i("marker ", "" + l.latitude);
                    }
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<EventListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }


    private  void fetchContacts() {
        Call<ContactListDetails> call = RestUser.get().getContactDetailedList(23);
        call.enqueue(new Callback<ContactListDetails>() {
            @Override
            public void onResponse(Call<ContactListDetails> call, Response<ContactListDetails> response) {
                if (response.isSuccessful()) {
                    final ContactListDetails r = response.body();
                    Log.e(TAG, r.getContacts().toString());
                    tabContacts.addAll(r.getContacts());
                    for (ContactModel c : tabContacts) {
                        addMarkerLatLng(new LatLng(c.getCoordonnées().getLatitude(), c.getCoordonnées().getLongitude()), BitmapDescriptorFactory.HUE_BLUE);
                        //Log.i("marker ", "" + l.latitude);
                    }
                } else {
                    Log.e("REST CALL", "sendRequest not successful");
                }
            }

            @Override
            public void onFailure(Call<ContactListDetails> call, Throwable t) {
                Log.e("REST CALL", t.getMessage());
            }
        });
    }

}

package fr.iut_orsay.frinme;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import fr.iut_orsay.frinme.MainActivity;
import fr.iut_orsay.frinme.model.ContactModel;
import fr.iut_orsay.frinme.model.DataBase;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.ContactListDetails;
import fr.iut_orsay.frinme.rest.pojo.EventListDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Martin on 20/03/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchEvents();
        fetchContacts();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finish();
    }

    private void fetchEvents() {
        Call<EventListDetails> call = RestUser.get().getEventDetailedList();
        call.enqueue(new Callback<EventListDetails>() {
            @Override
            public void onResponse(Call<EventListDetails> call, Response<EventListDetails> response) {
                if (response.isSuccessful()) {
                    final EventListDetails r = response.body();
                    if (response.body().getEvents().size() != DataBase.getAppDatabase(getApplicationContext()).eventDao().countEvents())
                        DataBase.getAppDatabase(getApplicationContext()).eventDao().insertAll(r.getEvents());
                    Log.e("WEW",DataBase.getAppDatabase(getApplicationContext()).eventDao().countEvents()+"");
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
                    if (response.body().getContacts().size() != DataBase.getAppDatabase(getApplicationContext()).contactDao().countContacts())
                        DataBase.getAppDatabase(getApplicationContext()).contactDao().insertAll(r.getContacts());
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
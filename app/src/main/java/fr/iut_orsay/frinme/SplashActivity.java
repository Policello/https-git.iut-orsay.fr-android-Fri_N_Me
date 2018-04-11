package fr.iut_orsay.frinme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import static fr.iut_orsay.frinme.model.DataBase.fetchContacts;
import static fr.iut_orsay.frinme.model.DataBase.fetchEvents;

/**
 * Activité lancée au démarrage de l'application
 */

public class SplashActivity extends AppCompatActivity {

    private SessionManagerPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = SessionManagerPreferences.getSettings(getApplicationContext());

        fetchEvents(getApplicationContext());
        if (settings.getUsrId() != -1) {
            fetchContacts(getApplicationContext(), settings.getUsrId());
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }, 2000);
        } else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, ConnexionActivity.class));
                SplashActivity.this.finish();
            }, 2000);
        }
    }


}
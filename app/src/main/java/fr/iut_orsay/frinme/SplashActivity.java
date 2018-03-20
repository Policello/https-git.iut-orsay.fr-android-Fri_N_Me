package fr.iut_orsay.frinme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import fr.iut_orsay.frinme.MainActivity;

/**
 * Created by Martin on 20/03/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
package fr.iut_orsay.frinme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.Connexion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ETmail, ETmdpasse;
    private Button btnValider;

    private String mail;
    private String mdpasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        //récupérer le bouton et l'editText
        btnValider = findViewById(R.id.btnValider);
        ETmail = findViewById(R.id.mail);
        ETmdpasse = findViewById(R.id.mdpasse);

        //écouteurs sur le bouton
        btnValider.setOnClickListener(this);
    }

    //méthode faisant le travail de récupération des données JSON
    private void sendRequest(final String mail, final String mdpasse) {
        Call<Connexion> call = RestUser.get().checkLogin(mail, mdpasse);
        //Lançons notre objet call de manière asynchrone
        call.enqueue(new Callback<Connexion>() {
            @Override
            public void onResponse(Call<Connexion> call, Response<Connexion> response) {
                if (response.isSuccessful()) {
                    //Recupérer le corps de la reponse que Retrofit s'est chargé de désérialiser à notre place l'aide du convertor Gson
                    final Connexion r = response.body();
                    Toast.makeText(ConnexionActivity.this, r.message, Toast.LENGTH_LONG).show();
                    if (r.isSuccess()) {
                        SessionManagerPreferences.getSettings(getApplicationContext()).login(r.getId());
                        Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                } else {
                    Toast.makeText(ConnexionActivity.this, "Erreur rencontrée", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Connexion> call, Throwable t) {
                Toast.makeText(ConnexionActivity.this, "FAILURE", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnValider) {
            boolean error = false;
            mail = ETmail.getText().toString().trim();
            mdpasse = ETmdpasse.getText().toString().trim();
            if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(mdpasse)) {
                error = true;
            }
            if (!error) {
                Log.i("DEBUG", "mail saisit : " + mail);
                Log.i("DEBUG", "mot de passe saisit : " + mdpasse);
                sendRequest(mail, mdpasse);
            } else {
                Toast.makeText(ConnexionActivity.this, "Veuillez saisir vos informations.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

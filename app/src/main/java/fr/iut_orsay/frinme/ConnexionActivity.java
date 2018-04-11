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

import es.dmoral.toasty.Toasty;
import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import fr.iut_orsay.frinme.rest.RestUser;
import fr.iut_orsay.frinme.rest.pojo.Connexion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fr.iut_orsay.frinme.model.DataBase.fetchContacts;

/**
 * Activité de connexion
 */
public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ETmail, ETmdpasse;
    private Button btnValider, btnInscription;

    private String mail;
    private String mdpasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        //récupérer le bouton et l'editText
        btnValider = findViewById(R.id.btnValider);
        btnInscription = findViewById(R.id.btnInscript);
        ETmail = findViewById(R.id.mail);
        ETmdpasse = findViewById(R.id.mdpasse);

        //écouteurs sur les boutons
        btnValider.setOnClickListener(this);
        btnInscription.setOnClickListener(this);
    }

    /**
     * Connecte l'utilisateur
     *
     * @param mail mail de l'utilisateur
     * @param mdpasse mot de passe de l'utilisateur
     */
    private void sendRequest(final String mail, final String mdpasse) {
        Call<Connexion> call = RestUser.get().checkLogin(mail, mdpasse);
        //Lançons notre objet call de manière asynchrone
        call.enqueue(new Callback<Connexion>() {
            @Override
            public void onResponse(Call<Connexion> call, Response<Connexion> response) {
                if (response.isSuccessful()) {
                    //Recupérer le corps de la reponse que Retrofit s'est chargé de désérialiser à notre place l'aide du convertor Gson
                    final Connexion r = response.body();
                    if (r != null) {
                        Toasty.info(getApplicationContext(), r.getMessage(), Toast.LENGTH_LONG).show();
                        if (r.isSuccess()) {
                            SessionManagerPreferences.getSettings(getApplicationContext()).login(r.getId());
                            fetchContacts(getApplicationContext(), SessionManagerPreferences.getSettings(getApplicationContext()).getUsrId());
                            Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }

                } else {
                    Toasty.error(getApplicationContext(), "Erreur rencontrée", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Connexion> call, Throwable t) {
                Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
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
                Toasty.warning(getApplicationContext(), "Veuillez saisir vos informations.", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.btnInscript) {
            startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

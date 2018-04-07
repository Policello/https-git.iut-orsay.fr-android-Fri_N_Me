package fr.iut_orsay.frinme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText pseudo, mail, mdp, mdpConfirm;
    private Button btnValider, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        pseudo = findViewById(R.id.resetpwd_edit_name);
        mail = findViewById(R.id.resetpwd_edit_mail);
        mdp = findViewById(R.id.resetpwd_edit_pwd_old);
        mdpConfirm = findViewById(R.id.resetpwd_edit_pwd_new);
        btnValider = findViewById(R.id.register_btn_sure);
        btnCancel = findViewById(R.id.btnCancel);

        btnValider.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn_sure) {
            if (mdp.getText().toString().equals(mdpConfirm.getText().toString())) {
                inscription(mail.getText().toString(), mdp.getText().toString(), pseudo.getText().toString(), "", null, null);
            } else {
                Toast.makeText(getApplicationContext(), "Mot de passe non identique", Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.btnCancel) {
            startActivity(new Intent(InscriptionActivity.this, ConnexionActivity.class));
        }
    }

    private void inscription(String mail, String mdpasse,
                             String pseudo, String commentaire,
                             Long latitude, Long longitude) {
        Call<Connexion> call = RestUser.get().addUser(mail, mdpasse, pseudo, commentaire, latitude, longitude);
        //Lançons notre objet call de manière asynchrone
        call.enqueue(new Callback<Connexion>() {
            @Override
            public void onResponse(Call<Connexion> call, Response<Connexion> response) {
                if (response.isSuccessful()) {
                    final Connexion r = response.body();
                    if (r != null && r.isSuccess()) {
                        Toast.makeText(InscriptionActivity.this, r.getMessage(), Toast.LENGTH_LONG).show();
                        SessionManagerPreferences.getSettings(getApplicationContext()).login(r.getId());
                        Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                } else {
                    Toast.makeText(InscriptionActivity.this, "Erreur rencontrée", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Connexion> call, Throwable t) {
                Toast.makeText(InscriptionActivity.this, "FAILURE", Toast.LENGTH_LONG).show();
            }
        });
    }
}

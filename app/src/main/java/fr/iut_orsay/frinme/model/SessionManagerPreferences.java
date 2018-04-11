package fr.iut_orsay.frinme.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Gestionnaire de persistance longue
 */
public class SessionManagerPreferences {

    @SuppressLint("StaticFieldLeak")
    private static SessionManagerPreferences instance = null;

    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor;

    private SessionManagerPreferences(Context context){
        this.sharedPreferences = context.getSharedPreferences("LOGIN_SETTINGS", MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public synchronized static SessionManagerPreferences getSettings(Context context) {
        if (instance == null){
            instance = new SessionManagerPreferences(context);
        }
        return instance;
    }

    public void login(int userId){
        this.editor.putInt("ID_LOGIN", userId);
        this.editor.commit();
    }

    public void logout() {
        this.editor.clear();
        this.editor.commit();
    }

    public int getUsrId(){
        return this.sharedPreferences.getInt("ID_LOGIN", -1);
    }
}
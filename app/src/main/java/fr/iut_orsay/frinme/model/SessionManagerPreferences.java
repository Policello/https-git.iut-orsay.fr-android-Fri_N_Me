package fr.iut_orsay.frinme.model;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManagerPreferences {

    private static SessionManagerPreferences instance = null;

    private Context context = null;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    private SessionManagerPreferences(Context context){
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences("LOGIN_SETTINGS", MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public static SessionManagerPreferences getSettings(Context context) {
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
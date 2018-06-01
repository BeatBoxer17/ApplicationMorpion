package com.example.admin.applicationmorpion;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private final static String PREF_NAME = "app_prefs";
    private final static  int PRIVATE_MODE = 0;
    private final static String IS_LOGGED = "isloged";
    private final static String PSEUDO = "pseudo";
    private final static String ID = "id";
    private final static String ID_COULEUR = "id_couleur";
    private Context context;

    public SessionManager(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public boolean isLogged(){
        return prefs.getBoolean(IS_LOGGED, false);
    }

    public  String getPseudo(){
        return prefs.getString(PSEUDO, null);
    }

    public  String getId(){
        return prefs.getString(ID, null);
    }

    public String getIdCouleur(){
        return prefs.getString(ID_COULEUR, null);
    }

    public void insertUser(String id, String pseudo, String id_couleur){
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(ID, id);
        editor.putString(PSEUDO, pseudo);
        editor.putString(ID_COULEUR, id_couleur);
        editor.commit();
    }

    public void logout(){
         editor.clear().commit();
    }
}

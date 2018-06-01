package com.example.admin.applicationmorpion;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Déclaration des variable
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

    // Renvoie true si il y a un joueur de connecté en session sinon false
    public boolean isLogged(){
        return prefs.getBoolean(IS_LOGGED, false);
    }

    // Renvoie le pseudo du joueur
    public String getPseudo(){
        return prefs.getString(PSEUDO, null);
    }

    // Renvoie le pseudo du joueur
    public String getId(){
        return prefs.getString(ID, null);
    }

    // Renvoiee l'id_couleur du joueur
    public String getIdCouleur(){
        return prefs.getString(ID_COULEUR, null);
    }

    // Ajouter le joueur en session
    public void insertUser(String id, String pseudo, String id_couleur){
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(ID, id);
        editor.putString(PSEUDO, pseudo);
        editor.putString(ID_COULEUR, id_couleur);
        editor.commit();
    }

    // Supprime le joueur de la session
    public void logout(){
         editor.clear().commit();
    }
}

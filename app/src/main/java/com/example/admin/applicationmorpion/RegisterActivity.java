package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.GetCouleurRequest;
import com.example.admin.applicationmorpion.myrequest.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // Définition des variable
    private Button btn_send;
    private TextInputLayout til_nom, til_prenom, til_pseudo, til_email, til_password, til_password2;
    private ProgressBar pb_loader;
    private Spinner sp_couleur;
    private RequestQueue queue;
    private RegisterRequest registerRequest;
    private GetCouleurRequest getCouleurRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        registerRequest = new RegisterRequest(this, queue);
        getCouleurRequest = new GetCouleurRequest(this, queue);

        // Récuperaion des élément du XML
        sp_couleur = (Spinner) findViewById(R.id.spinner_couleur);
        btn_send = (Button) findViewById(R.id.btn_send);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader);
        til_pseudo = (TextInputLayout) findViewById(R.id.til_pseudo);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        til_password2 = (TextInputLayout) findViewById(R.id.til_password2);
        til_nom = (TextInputLayout) findViewById(R.id.til_nom);
        til_prenom = (TextInputLayout) findViewById(R.id.til_prenom);

        // ArrayList des libelle de couleur
        final List couleur = new ArrayList();
        // On ajouter cette ArrayList à un adapter
        final ArrayAdapter adapterCouleur = new ArrayAdapter(this, android.R.layout.simple_spinner_item, couleur);
        adapterCouleur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ArrayList des id de couleur
        final List id_couleur = new ArrayList();

        // Appel de la requête getCouleur
        getCouleurRequest.getCouleur(new GetCouleurRequest.getCouleurCallBack() {
            @Override
            // Si la requête reussi
            public void onSucces(JSONObject json) {
                try{
                    // On parcours l'objet retourner afin d'ajouter les libelle et les id des couleur à leur ArrayList
                    for(Iterator iterator = json.keys(); iterator.hasNext();){
                        Object cle = iterator.next();
                        Object val = json.get(String.valueOf(cle));
                        id_couleur.add(cle);
                        couleur.add(val);
                        sp_couleur.setAdapter(adapterCouleur);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        // Au clicl sur le bouton d'inscription
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // On rend la progress bar visible
                pb_loader.setVisibility(View.VISIBLE);

                // Récuperation des données rentrer
                String pseudo = til_pseudo.getEditText().getText().toString().trim();
                String email = til_email.getEditText().getText().toString().trim();
                String password = til_password.getEditText().getText().toString().trim();
                String password2 = til_password2.getEditText().getText().toString().trim();
                String nom = til_nom.getEditText().getText().toString().trim();
                String prenom = til_prenom.getEditText().getText().toString().trim();
                // Récuperation de la position du spinner
                Integer id_couleur_fin = sp_couleur.getSelectedItemPosition();
                // Récuperation de l'id de la couleur en fonction de la position dans le spinner
                Object id_couleur_fin2 = id_couleur.get(id_couleur_fin);

                // Si les champs ne sont pas vide
                if(pseudo.length() > 0 && email.length() > 0 && password.length() > 0 && password2.length() > 0 && nom.length() > 0 && prenom.length() > 0 && !id_couleur_fin2.toString().isEmpty()) {
                    // Requetes pour inscrire un joueur
                    registerRequest.register(nom, prenom, pseudo, email, password, password2, id_couleur_fin2.toString(), new RegisterRequest.RegisterCallBack() {
                        @Override
                        // Si elle reussi
                        public void onSucces(String message) {
                            // On cache la progress bar
                            pb_loader.setVisibility(View.GONE);
                            // Et on redirige vers l'activité Login
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("REGISTER", message);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        // Si il ya des erreurs d'entrée
                        public void inputErrors(Map<String, String> errors) {
                            // On cache la progress bar
                            pb_loader.setVisibility(View.GONE);

                            // Et grace a la librairi desgin on peut mettre les erreur recuperer directement en dessous du inputtext
                            if(errors.get("pseudo") != null){
                                til_pseudo.setError(errors.get("pseudo"));
                            } else {
                                til_pseudo.setErrorEnabled(false);
                            }
                            if(errors.get("email") != null){
                                til_email.setError(errors.get("email"));
                            } else {
                                til_email.setErrorEnabled(false);
                            }
                            if(errors.get("password") != null){
                                til_password.setError(errors.get("password"));
                            } else {
                                til_password.setErrorEnabled(false);
                            }
                            if(errors.get("nom") != null){
                                til_nom.setError(errors.get("nom"));
                            } else {
                                til_nom.setErrorEnabled(false);
                            }
                            if(errors.get("prenom") != null){
                                til_prenom.setError(errors.get("prenom"));
                            } else {
                                til_prenom.setErrorEnabled(false);
                            }
                        }

                        @Override
                        // S'il y a des erreurs de connection ou de la librairie volley
                        public void onError(String message) {
                            pb_loader.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else { // Si des champs ne sont pas remplit
                    pb_loader.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.MyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_send;
    private TextInputLayout til_nom, til_prenom, til_pseudo, til_email, til_password, til_password2;
    private ProgressBar pb_loader;
    private RequestQueue queue;
    private MyRequest request;
    private Spinner sp_couleur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final List couleur = new ArrayList();
        final ArrayAdapter adapterCouleur = new ArrayAdapter(this, android.R.layout.simple_spinner_item, couleur);

        /*final List id_couleur = new ArrayList();
        final ArrayAdapter adapterIdCouleur = new ArrayAdapter(this, android.R.layout.simple_spinner_item, id_couleur);*/

        adapterCouleur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_couleur = (Spinner) findViewById(R.id.spinner_couleur);
        btn_send = (Button) findViewById(R.id.btn_send);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader);
        til_pseudo = (TextInputLayout) findViewById(R.id.til_pseudo);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        til_password2 = (TextInputLayout) findViewById(R.id.til_password2);
        til_nom = (TextInputLayout) findViewById(R.id.til_nom);
        til_prenom = (TextInputLayout) findViewById(R.id.til_prenom);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

        request.getCouleur(new MyRequest.getCouleurCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                try{
                    for(Iterator iterator = json.keys(); iterator.hasNext();){
                        Object cle = iterator.next();
                        Object val = json.get(String.valueOf(cle));
                        /*id_couleur.add(cle);*/
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

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_loader.setVisibility(View.VISIBLE);
                String pseudo = til_pseudo.getEditText().getText().toString().trim();
                String email = til_email.getEditText().getText().toString().trim();
                String password = til_password.getEditText().getText().toString().trim();
                String password2 = til_password2.getEditText().getText().toString().trim();
                String nom = til_nom.getEditText().getText().toString().trim();
                String prenom = til_prenom.getEditText().getText().toString().trim();

                if(pseudo.length() > 0 && email.length() > 0 && password.length() > 0 && password2.length() > 0 && nom.length() > 0 && prenom.length() > 0) {
                    request.register(nom, prenom, pseudo, email, password, password2, new MyRequest.RegisterCallBack() {
                        @Override
                        public void onSucces(String message) {
                            pb_loader.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("REGISTER", message);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void inputErrors(Map<String, String> errors) {
                            pb_loader.setVisibility(View.GONE);
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
                        public void onError(String message) {
                            pb_loader.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

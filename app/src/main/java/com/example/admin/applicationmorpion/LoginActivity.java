package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.ConnectionRequest;

public class LoginActivity extends AppCompatActivity {

    // Déclaration des variable
    private TextInputLayout til_pseudo_log, til_password_log;
    private Button btn_send;
    private RequestQueue queue;
    private ConnectionRequest connectionRequest;
    private ProgressBar pb_loader;
    private Handler handler;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        connectionRequest = new ConnectionRequest(this, queue);
        handler = new Handler();
        sessionManager = new SessionManager(this);

        // Si on joueur vient d'être enregister on recupère l'intent afin d'afficher le message
        Intent intent = getIntent();
        if(intent.hasExtra("REGISTER")){
            Toast.makeText(this, intent.getStringExtra("REGISTER"), Toast.LENGTH_SHORT).show();
        }

        // Si il y a un joueur connecté en session on va directement a l'activité Menu1
        if(sessionManager.isLogged()){
            Intent test = new Intent(this, Menu1Activity.class);
            startActivity(test);
            finish();
        }

        // Récuperation des élément du XML
        til_pseudo_log = (TextInputLayout) findViewById(R.id.til_pseudo_log);
        til_password_log = (TextInputLayout) findViewById(R.id.til_password_log);
        btn_send = (Button) findViewById(R.id.btn_send);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader_log);

        // Si on clique sur le bouton envoyer
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récuperation du pseudo et du mot de passe
                final String pseudo = til_pseudo_log.getEditText().getText().toString().trim();
                final String password = til_password_log.getEditText().getText().toString().trim();

                // On rend la progress bar visible
                pb_loader.setVisibility(View.VISIBLE);

                // Si le pseudo et le mot de passe ne sont pas vide
                if(pseudo.length() > 0 && password.length() > 0) {
                    // On fait un handler pour afficher au moins 1 seconde la progress bar
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Requêtes pour connecter le joueur
                            connectionRequest.connection(pseudo, password, new ConnectionRequest.LoginCallBack() {
                                @Override
                                // S'il n'y a pas d'erreur
                                public void onSucces(String id, String pseudo, String id_couleur) {
                                    // On cache la progress bar
                                    pb_loader.setVisibility(View.GONE);
                                    // On l'ajoute en session
                                    sessionManager.insertUser(id, pseudo, id_couleur);
                                    // Et on va vers l'activité Menu1
                                    Intent intent = new Intent(getApplicationContext(), Menu1Activity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                // S'il y a une erreur
                                public void onError(String message) {
                                    // On cache la progress bar
                                    pb_loader.setVisibility(View.GONE);
                                    // Et on affiche le message d'erreur
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    },1000);
                } else { // Si le champ pseudo ou le champ mot de passe et vide
                    // On affiche un message disant de remplir tout les champs
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

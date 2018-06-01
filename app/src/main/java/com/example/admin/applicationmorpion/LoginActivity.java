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

        Intent intent = getIntent();
        if(intent.hasExtra("REGISTER")){
            Toast.makeText(this, intent.getStringExtra("REGISTER"), Toast.LENGTH_SHORT).show();
        }

        til_pseudo_log = (TextInputLayout) findViewById(R.id.til_pseudo_log);
        til_password_log = (TextInputLayout) findViewById(R.id.til_password_log);
        btn_send = (Button) findViewById(R.id.btn_send);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader_log);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        connectionRequest = new ConnectionRequest(this, queue);
        handler = new Handler();
        sessionManager = new SessionManager(this);

        if(sessionManager.isLogged()){
            Intent test = new Intent(this, Menu1Activity.class);
            startActivity(test);
            finish();
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pseudo = til_pseudo_log.getEditText().getText().toString().trim();
                final String password = til_password_log.getEditText().getText().toString().trim();
                pb_loader.setVisibility(View.VISIBLE);
                if(pseudo.length() > 0 && password.length() > 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connectionRequest.connection(pseudo, password, new ConnectionRequest.LoginCallBack() {
                                @Override
                                public void onSucces(String id, String pseudo, String id_couleur) {
                                    pb_loader.setVisibility(View.GONE);
                                    sessionManager.insertUser(id, pseudo, id_couleur);
                                    Intent intent = new Intent(getApplicationContext(), Menu1Activity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(String message) {
                                    pb_loader.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    },1000);
                } else {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

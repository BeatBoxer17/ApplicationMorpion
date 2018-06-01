package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Déclaration des variabble
    private Button btn_login, btn_register;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        // Si il y a un jouueur connected en session on emmène directement vers l'activité Menu1
        if(sessionManager.isLogged()){
            Intent intent = new Intent(this, Menu1Activity.class);
            startActivity(intent);
            finish();
        }

        // Récuperation des éléments du XML
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);

        // Quand on clique sur le bouton d'inscription on va vers l'activité Register
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
            }
        });

        // Qaund on clique sur le bouton de connection on va vers l'activité Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            }
        });
    }

}

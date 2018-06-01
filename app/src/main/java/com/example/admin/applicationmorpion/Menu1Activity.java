package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.CreateDuelRequest;
import com.example.admin.applicationmorpion.myrequest.GetJoueurNokRequest;
import com.example.admin.applicationmorpion.myrequest.GetJoueurOkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Menu1Activity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView textView;
    private Button btn_logout, btn_duel, btn_nduel, btn_ajout;
    private RequestQueue queue;
    private CreateDuelRequest createDuelRequest;
    private GetJoueurNokRequest getJoueurNokRequest;
    private GetJoueurOkRequest getJoueurOkRequest;
    private AppCompatSpinner sp_j_ok, sp_j_nok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        createDuelRequest = new CreateDuelRequest(this, queue);
        getJoueurNokRequest = new GetJoueurNokRequest(this, queue);
        getJoueurOkRequest = new GetJoueurOkRequest(this, queue);

        sessionManager = new SessionManager(this);

        textView = (TextView) findViewById(R.id.tv_pseudo);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        sp_j_ok = (AppCompatSpinner) findViewById(R.id.sp_j_ok);
        sp_j_nok = (AppCompatSpinner) findViewById(R.id.sp_j_nok);
        btn_duel = (Button) findViewById(R.id.btn_go_duel);
        btn_nduel = (Button) findViewById(R.id.btn_go_duel_n);
        btn_ajout = (Button) findViewById(R.id.btn_ajout);

        if(sessionManager.isLogged()){
            String pseudo = sessionManager.getPseudo();
            String id = sessionManager.getId();
            textView.setText(pseudo);
        }

        final List j_ok = new ArrayList();
        final ArrayAdapter adapterJok = new ArrayAdapter(this, android.R.layout.simple_spinner_item, j_ok);
        adapterJok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ArrayList des id de joueur affrontés
        final List id_j_ok = new ArrayList();

        final List j_nok = new ArrayList();
        final ArrayAdapter adapterJnok = new ArrayAdapter(this, android.R.layout.simple_spinner_item, j_nok);
        adapterJnok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ArrayList des id des joueurs non affrontés
        final List id_j_nok = new ArrayList();

        getJoueurOkRequest.getJoueurok(Integer.valueOf(sessionManager.getId()), new GetJoueurOkRequest.getJoueurOkCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                try{
                    for(Iterator iterator = json.keys(); iterator.hasNext();) {
                        Object cle = iterator.next();
                        Object val = json.get(String.valueOf(cle));
                        id_j_ok.add(cle);
                        j_ok.add(val);
                        sp_j_ok.setAdapter(adapterJok);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        getJoueurNokRequest.getJoueurNok(Integer.valueOf(sessionManager.getId()), new GetJoueurNokRequest.getJoueurNokCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                try{
                    for(Iterator iterator = json.keys(); iterator.hasNext();){
                        Object cle = iterator.next();
                        Object val = json.get(String.valueOf(cle));
                        id_j_nok.add(cle);
                        j_nok.add(val);
                        sp_j_nok.setAdapter(adapterJnok);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        btn_duel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!j_ok.isEmpty()){
                    // Récuperation de la position du spinner
                    Integer id_j_ok_fin = sp_j_ok.getSelectedItemPosition();
                    // Récuperation de l'id du joueur en fonction de la position dans le spinner
                    Object id_j_ok_fin2 = id_j_ok.get(id_j_ok_fin);
                    // Récupeation du pseudo du joueur en fonction de la position dans le spinner
                    Object j_ok_fin = j_ok.get(id_j_ok_fin);

                    Intent intent = new Intent(getApplicationContext(), JeuActivity.class);
                    intent.putExtra("id_joueur2", id_j_ok_fin2.toString());
                    intent.putExtra("pseudo_joueur2", j_ok_fin.toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Pas de joueur déjà affrontés", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_nduel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!j_nok.isEmpty()){
                    // Récuperation de la position du spinner
                    Integer id_j_nok_fin = sp_j_nok.getSelectedItemPosition();
                    // Récuperation de l'id de la couleur en fonction de la position dans le spinner
                    final Object id_j_nok_fin2 = id_j_nok.get(id_j_nok_fin);
                    // Récupeation du pseudo du joueur en fonction de la position dans le spinner
                    final Object j_nok_fin = j_nok.get(id_j_nok_fin);

                    createDuelRequest.createDuel(sessionManager.getId(), id_j_nok_fin2.toString(), new CreateDuelRequest.createDuelCallBack() {
                        @Override
                        public void onSucces(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), JeuActivity.class);
                            intent.putExtra("id_joueur2", id_j_nok_fin2.toString());
                            intent.putExtra("pseudo_joueur2", j_nok_fin.toString());
                            intent.putExtra("create", message);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void inputErrors(Map<String, String> errors) {

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Pas de joueur non affrontés", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

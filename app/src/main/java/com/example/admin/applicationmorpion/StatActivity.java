package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.GetDuelRequest;
import com.example.admin.applicationmorpion.myrequest.GetWinRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StatActivity extends AppCompatActivity {

    private TextView tv_duel , tv_win, tv_pseudo, tv_pourcentage;
    private RequestQueue queue;
    private GetWinRequest getWinRequest;
    private GetDuelRequest getDuelRequest;
    private String id_joueur = "";
    public Integer victoire = 0;
    public Integer duel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        getWinRequest = new GetWinRequest(this, queue);
        getDuelRequest = new GetDuelRequest(this, queue);

        tv_pseudo = (TextView) findViewById(R.id.stat_joueur);
        tv_duel = (TextView) findViewById(R.id.tv_tduel_val);
        tv_win = (TextView) findViewById(R.id.tv_tduelv_val);
        tv_pourcentage = (TextView) findViewById(R.id.pourcentage);

        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            id_joueur = intent.getStringExtra("id");
        }
        if(intent.hasExtra("pseudo")){
            String pseudo = intent.getStringExtra("pseudo");
            Log.d("APP",pseudo);
            tv_pseudo.setText(pseudo);
        }

        getWinRequest.getWin(id_joueur, new GetWinRequest.getWinCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                // Pour chaque jooueur on met son psuedo dans j_ok et son id dans id_j_ok
                for(Iterator iterator = json.keys(); iterator.hasNext();) {
                    Object cle = iterator.next();
                    victoire++;
                }
                Toast.makeText(getApplicationContext(), victoire, Toast.LENGTH_SHORT).show();
                tv_win.setText(victoire);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        getDuelRequest.getDuel(id_joueur, new GetDuelRequest.getDuelCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                // Pour chaque jooueur on met son psuedo dans j_ok et son id dans id_j_ok
                for(Iterator iterator = json.keys(); iterator.hasNext();) {
                    Object cle = iterator.next();
                    duel++;
                }
                Toast.makeText(getApplicationContext(), duel, Toast.LENGTH_SHORT).show();
                tv_duel.setText(duel);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        Integer pourcentage = (victoire/duel)*100;
        tv_pourcentage.setText(pourcentage);
    }
}

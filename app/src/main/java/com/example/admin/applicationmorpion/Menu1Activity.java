package com.example.admin.applicationmorpion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.admin.applicationmorpion.myrequest.MyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Menu1Activity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView textView;
    private Button btn_logout;
    private RequestQueue queue;
    private MyRequest request;
    private AppCompatSpinner sp_j_ok, sp_j_nok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);

        final List j_ok = new ArrayList();
        final ArrayAdapter adapterJok = new ArrayAdapter(this, android.R.layout.simple_spinner_item, j_ok);
        adapterJok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final List j_nok = new ArrayList();
        final ArrayAdapter adapterJnok = new ArrayAdapter(this, android.R.layout.simple_spinner_item, j_nok);
        adapterJnok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        textView = (TextView) findViewById(R.id.tv_pseudo);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        sp_j_ok = (AppCompatSpinner) findViewById(R.id.sp_j_ok);
        sp_j_nok = (AppCompatSpinner) findViewById(R.id.sp_j_nok);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

        sessionManager = new SessionManager(this);

        if(sessionManager.isLogged()){
            String pseudo = sessionManager.getPseudo();
            String id = sessionManager.getId();
            textView.setText(pseudo);
        }

        request.getJoueurok(Integer.valueOf(sessionManager.getId()), new MyRequest.getJoueurOkCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                try{
                    for(Iterator iterator = json.keys(); iterator.hasNext();) {
                        Object cle = iterator.next();
                        Object val = json.get(String.valueOf(cle));
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

        request.getJoueurNok(Integer.valueOf(sessionManager.getId()), new MyRequest.getJoueurNokCallBack() {
            @Override
            public void onSucces(JSONObject json) {
                try{
                    for(Iterator iterator = json.keys(); iterator.hasNext();){
                        Object cle = iterator.next();
                        Object val = json.get(String.valueOf(cle));
                        /*id_couleur.add(cle);*/
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

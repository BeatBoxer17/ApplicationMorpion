package com.example.admin.applicationmorpion.myrequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 01/06/2018.
 */

//
public class UpdateScoreRequest {
    private Context context;
    private RequestQueue queue;

    public UpdateScoreRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void updateScore(final String id_joueur1, final String id_joueur2, final String id_vainqueur, final updateScoreCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/duel/updatescore.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);

                    callBack.onSucces("Nouveau duel crée");

                    callBack.inputErrors(errors);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NetworkError){
                    callBack.onError("Impossible de se connecter");
                } else if(error instanceof VolleyError){
                    callBack.onError("Une erreure c'est produite");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("idj1", id_joueur1);
                map.put("idj2", id_joueur2);
                map.put("v", id_vainqueur);


                return map;
            }
        };

        queue.add(request);
    }

    public interface updateScoreCallBack{
        void onSucces(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }
}

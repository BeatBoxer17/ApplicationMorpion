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

public class CreateDuelRequest {
    private Context context;
    private RequestQueue queue;

    public CreateDuelRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void createDuel(final String id_joueur, final String id_joueur2, final createDuelCallBack callBack){

        String url = "http://appmorpiontest.000webhostapp.com/duel/createduel.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        callBack.onSucces("Nouveau duel crée");
                    } else{
                        JSONObject message = json.getJSONObject("message");
                        callBack.inputErrors(errors);
                    }
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
                map.put("id_joueur1", id_joueur);
                map.put("id_joueur2", id_joueur2);

                return map;
            }
        };

        queue.add(request);
    }

    public interface createDuelCallBack{
        void onSucces(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }
}

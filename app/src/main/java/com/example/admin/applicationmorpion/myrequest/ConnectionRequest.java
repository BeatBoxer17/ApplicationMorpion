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

public class ConnectionRequest {
    private Context context;
    private RequestQueue queue;

    public ConnectionRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void connection(final String pseudo, final String password, final LoginCallBack callBack){

        String url = "http://appmorpiontest.000webhostapp.com/login/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;

                try {
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        String id = json.getString("id");
                        String pseudo = json.getString("pseudo");
                        String id_couleur = json.getString("id_couleur");
                        callBack.onSucces(id, pseudo, id_couleur);
                    } else{
                        callBack.onError(json.getString("message"));
                    }

                } catch (JSONException e) {
                    callBack.onError("Une erreure c'est produite");
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
                map.put("pseudo", pseudo);
                map.put("password", password);

                return map;
            }
        };

        queue.add(request);

    }

    public interface LoginCallBack{
        void onSucces(String id, String pseudo, String id_couleur);
        void onError(String message);
    }
}

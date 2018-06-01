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
 * Created by admin on 27/05/2018.
 */

public class GetInfoJoueurRequest {

    private Context context;
    private RequestQueue queue;

    public GetInfoJoueurRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void getInfoJoueur(final String id, final String idj2, final String idinfo, final getInfoJoueurCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/duel/infojoueur.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                     json = new JSONObject(response);

                    String score = json.getString("score");
                    String couleur = json.getString("couleur");

                    callBack.onSucces(score, couleur);

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
                map.put("idj1", id);
                map.put("idj2", idj2);
                map.put("idinfo", idinfo);
                return map;
            }
        };

        queue.add(request);

    }

    public interface getInfoJoueurCallBack{
        void onSucces(String score, String couleur);
        void onError(String message);
    }
}

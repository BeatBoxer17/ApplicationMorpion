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

public class GetWinRequest {
    private Context context;
    private RequestQueue queue;

    public GetWinRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void getWin(final String id, final getWinCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/duel/victoire.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    callBack.onSucces(json);

                } catch (JSONException e) {
                    callBack.onError("Une erreure c'est produite2");
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
                map.put("id", String.valueOf(id));
                return map;
            }
        };

        queue.add(request);

    }

    public interface getWinCallBack{
        void onSucces(JSONObject json);
        void onError(String message);
    }
}

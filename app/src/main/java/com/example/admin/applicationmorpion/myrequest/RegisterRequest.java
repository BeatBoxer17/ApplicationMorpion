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

public class RegisterRequest{

    private Context context;
    private RequestQueue queue;

    public RegisterRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void register(final String nom, final String prenom, final String pseudo, final String email, final String password, final String password2, final String id_couleur_fin, final RegisterCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/register/register.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        callBack.onSucces("Vous etes bien inscrit");
                    } else{
                        JSONObject message = json.getJSONObject("message");
                        if(message.has("pseudo")){
                            errors.put("pseudo", message.getString("pseudo"));
                        }
                        if(message.has("email")){
                            errors.put("email", message.getString("email"));
                        }
                        if(message.has("password")){
                            errors.put("password", message.getString("password"));
                        }
                        if(message.has("nom")){
                            errors.put("nom", message.getString("nom"));
                        }
                        if(message.has("prenom")){
                            errors.put("prenom", message.getString("prenom"));
                        }
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
                map.put("nom", nom);
                map.put("prenom", prenom);
                map.put("pseudo", pseudo);
                map.put("email", email);
                map.put("password", password);
                map.put("password2", password2);
                map.put("id_couleur", id_couleur_fin);

                return map;
            }
        };

        queue.add(request);
    }

    public interface RegisterCallBack{
        void onSucces(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }
}

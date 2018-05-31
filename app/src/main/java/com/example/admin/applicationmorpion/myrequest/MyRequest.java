package com.example.admin.applicationmorpion.myrequest;

import android.content.Context;
import android.util.Log;

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

public class MyRequest {

    private Context context;
    private RequestQueue queue;

    public MyRequest(Context context, RequestQueue queue) {
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

    public void connection(final String pseudo, final String password, final LoginCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/login/login.php";

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
                        callBack.onSucces(id, pseudo);
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
        void onSucces(String id, String pseudo);
        void onError(String message);
    }

    public void getCouleur(final getCouleurCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/couleur/couleur.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    callBack.onSucces(json);

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

                return map;
            }
        };

        queue.add(request);

    }

    public interface getCouleurCallBack{
        void onSucces(JSONObject json);
        void onError(String message);
    }

    public void getJoueurok(final Integer id, final getJoueurOkCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/duel/joueurok.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    callBack.onSucces(json);

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
                map.put("id", String.valueOf(id));
                return map;
            }
        };

        queue.add(request);

    }

    public interface getJoueurOkCallBack{
        void onSucces(JSONObject json);
        void onError(String message);
    }

    public void getJoueurNok(final Integer id, final getJoueurNokCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/duel/joueurnok.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);

                    callBack.onSucces(json);

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
                map.put("id", String.valueOf(id));
                return map;
            }
        };

        queue.add(request);

    }

    public interface getJoueurNokCallBack{
        void onSucces(JSONObject json);
        void onError(String message);
    }

    public void createDuel(final String id_joueur, final String id_joueur2, final createDuelCallBack callBack){

        String url = "http://app-morpion.000webhostapp.com/duel/createduel.php";

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

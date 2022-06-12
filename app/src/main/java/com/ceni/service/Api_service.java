package com.ceni.service;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.service.autofill.FieldClassification;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.ceni.model.Electeur;
import com.ceni.model.Localisation;
import com.ceni.model.User;
import com.ceni.recensementnumerique.LoginActivity;
import com.ceni.recensementnumerique.MainActivity;
import com.ceni.recensementnumerique.MenuActivity;
import com.ceni.recensementnumerique.RegisterUserActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


public class Api_service {
    //private static String base_url = "https://cenirecensement.herokuapp.com/";
    private static String base_url = "http://192.168.8.100:8080/";

    public static void getLocalisation(Db_sqLite DB,Context context) {
        try{
            List<Localisation> list = new ArrayList<Localisation>();
            Map<String, String> headers = new ArrayMap<String, String>();
            headers.put("Accept", "application/json");
            RequestQueue queue = Volley.newRequestQueue(context);
            String myUrl = base_url+"LocalisationApi/Localisation";
            StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                    response -> {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("API SERVICE  ",""+jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Localisation l = new Localisation();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                l.setBv_label(jsonObject.getString("BV_LABEL"));
                                l.setCode_bv(jsonObject.getString("CODE_BV"));
                                l.setCv_label(jsonObject.getString("CV_LABEL"));
                                l.setCode_cv(jsonObject.getString("CODE_CV"));
                                l.setFokontany_label(jsonObject.getString("FOKONTANY_LABEL"));
                                l.setCode_fokontany(jsonObject.getString("CODE_FOKONTANY"));
                                l.setCommune_label(jsonObject.getString("COMMUNE_LABEL"));
                                l.setCode_commune(jsonObject.getString("CODE_COMMUNE"));
                                l.setDistrict_label(jsonObject.getString("DISTRICT_LABEL"));
                                l.setCode_district(jsonObject.getString("CODE_DISTRICT"));
                                l.setRegion_label(jsonObject.getString("REGION_LABEL"));
                                l.setCode_region(jsonObject.getString("CODE_REGION"));
                                list.add(l);
//                                Log.d("VOLLEY SUCCES ","");
                            }
                            DB.insertLocalisationData(list);
                            Toast.makeText(context, "REGISTER", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(context, RegisterUserActivity.class);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    volleyError -> Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
            ){
                public Map<String, String> getHeaders() {
                    return headers;
                }
            };
            queue.add(myRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Localisation> getLocalisation2(Db_sqLite DB) {
        List<Localisation> resultat = new ArrayList<>();
        AndroidNetworking.get(base_url + "LocalisationApi/Localisation")
                .setPriority(Priority.MEDIUM)
                .addHeaders("Accept", "application/json")
                .build()
                .getAsObjectList(Localisation.class, new ParsedRequestListener<List<Localisation>>() {
                    @Override
                    public void onResponse(List<Localisation> response) {
                        boolean deleted = DB.deleteAllLocalisation();
                        Log.d(TAG, "ilay response: " + response);
                        for (int i = 0; i < response.size(); i++) {
                            resultat.add(response.get(i));
                            Log.d(TAG, "RESULTAT : " + response.get(i).getBv_label());
                            //boolean result = DB.insertLocalisationData(response.get(i));
//                            if (result) {
//                                Log.i("API_SERVICE","Insertion localisation succes "+response.get(i).getBv_label());
//                            } else {
//                                Log.i("API_SERVICE","Insertion localisation ERROR");
//                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "ERROR API_SERVICE getLocalisation " + error);
                    }
                });
        Log.d(TAG, "TAILLE RESULT: " + resultat.size());
        return resultat;
    }

    public void addNewElecteur(Context context,Electeur electeur) {
        final boolean[] result = {false};
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code_bv", electeur.getCode_bv());
            jsonObject.put("nom", electeur.getNom());
            jsonObject.put("prenom", electeur.getPrenom());
            jsonObject.put("sexe", electeur.getSexe());
            jsonObject.put("profession", electeur.getProfession());
            jsonObject.put("adresse", electeur.getAdresse());
            jsonObject.put("datenaiss", electeur.getDateNaiss());
            jsonObject.put("lieunaiss", electeur.getLieuNaiss());
            jsonObject.put("nompere", electeur.getNomPere());
            jsonObject.put("nommere", electeur.getNomMere());
            jsonObject.put("cinelect", electeur.getCinElect());
            jsonObject.put("datedeliv", electeur.getDateDeliv());
            jsonObject.put("lieudeliv", electeur.getLieuDeliv());
            jsonObject.put("ficheelect", electeur.getFicheElect());
            jsonObject.put("cinrecto", electeur.getCinRecto());
            jsonObject.put("cinverso", electeur.getCinVerso());
            AndroidNetworking.post(base_url + "ElecteurApi/Electeur")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.MEDIUM)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast toast = Toast.makeText(context, "ELECTEUR AJOUTER A LA BASE", Toast.LENGTH_LONG);
                            Intent i = new Intent(context, MenuActivity.class);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        }
                        @Override
                        public void onError(ANError error) {
                            if (error.getErrorCode() != 0) {
                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                Log.d(TAG, "onError errorDetail1 : " + error.getErrorDetail());
                            } else {
                                Log.d(TAG, "onError errorDetail2 : " + error.getErrorDetail());
                                Log.d(TAG, "onError errorDetail3 : " + error.getResponse());

                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUser() {
        List<User> resultat = new ArrayList<>();
        AndroidNetworking.get(base_url + "UtilisateurApi/utilisateur")
                .setTag("utilisateur")
                .setPriority(Priority.LOW)
                .addHeaders("Accept", "application/json")
                .build()
                .getAsObjectList(User.class, new ParsedRequestListener<List<User>>() {
                    @Override
                    public void onResponse(List<User> response) {
                        Log.d(TAG, "RESPONSE " + response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "ERROR " + error);
                    }
                });

        return resultat;
    }

    public void addNewUser(Context context,User user){
        Map<String, String> headers = new ArrayMap<String, String>();
        headers.put("Accept", "application/json");
        String postUrl = base_url+"UtilisateurApi/utilisateur";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject postData = new JSONObject();
        try {
            postData.put("nomuser", user.getNomUser());
            postData.put("prenomuser", user.getPrenomUser());
            postData.put("pseudo", user.getPseudo());
            postData.put("motdepasse", user.getMotdepasse());
            postData.put("code_district", user.getCode_district());
            postData.put("role", user.getRole());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, response ->  {
            try{
                Db_sqLite DB = new Db_sqLite(context);
                User u = new User();
                u.setIdUser(Integer.parseInt(response.getString("id")));
                u.setNomUser(response.getString("nomuser"));
                u.setPrenomUser(response.getString("prenomuser"));
                u.setRole(response.getString("role"));
                u.setPseudo(response.getString("pseudo"));
                u.setMotdepasse(response.getString("motdepasse"));
                u.setCode_district(response.getString("code_district"));
                Log.d(TAG,"USER LASA: "+u.toString());
                boolean isInserted = DB.insertUser(u);
                Log.d(TAG,"USER INSERT SQLITE: "+isInserted);
                Intent i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();
            }catch (Exception e ){
                e.printStackTrace();
            }
        },
                volleyError ->Log.d("ERROR","INSERTION USER "+volleyError.getMessage())
        ){
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
                        //Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);

    }

    public void addNewElecteur2(Context context,Electeur electeur){
        Map<String, String> headers = new ArrayMap<String, String>();
        headers.put("Accept", "application/json");
        String postUrl = base_url+"ElecteurApi/Electeur";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code_bv", electeur.getCode_bv());
            jsonObject.put("nom", electeur.getNom());
            jsonObject.put("prenom", electeur.getPrenom());
            jsonObject.put("sexe", electeur.getSexe());
            jsonObject.put("profession", electeur.getProfession());
            jsonObject.put("adresse", electeur.getAdresse());
            jsonObject.put("datenaiss", electeur.getDateNaiss());
            jsonObject.put("lieunaiss", electeur.getLieuNaiss());
            jsonObject.put("nompere", electeur.getNomPere());
            jsonObject.put("nommere", electeur.getNomMere());
            jsonObject.put("cinelect", electeur.getCinElect());
            jsonObject.put("datedeliv", electeur.getDateDeliv());
            jsonObject.put("lieudeliv", electeur.getLieuDeliv());
            jsonObject.put("ficheelect", electeur.getFicheElect());
            jsonObject.put("cinrecto", electeur.getCinRecto());
            jsonObject.put("cinverso", electeur.getCinVerso());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, jsonObject, response ->  {
            try{
                Log.i("API SERVICE ","INSERT ELECTEUR : ");
                Toast toast = Toast.makeText(context, "ELECTEUR AJOUTER A LA BASE", Toast.LENGTH_LONG);
                Intent i = new Intent(context, MenuActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();
            }catch (Exception e ){
                e.printStackTrace();
            }
        },
                volleyError ->Log.d("ERROR","INSERTION ELECTEUR "+volleyError.getMessage())
        ){
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        //Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);

    }
}

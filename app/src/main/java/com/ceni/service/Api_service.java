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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


public class Api_service {
    //private static String base_url = "https://cenirecensement.herokuapp.com/";
    private static String base_url = "http://192.168.8.101:8080/";

    SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
    public void addNewElecteur(Context context,Electeur electeur) {
        final boolean[] result = {false};
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code_bv", electeur.getCode_bv());
            jsonObject.put("num_feuillet", electeur.getnFiche());
            jsonObject.put("nomelect", electeur.getNom());
            jsonObject.put("prenomelect", electeur.getPrenom());
            jsonObject.put("sexe", electeur.getSexe());
            jsonObject.put("profession", electeur.getProfession());
            jsonObject.put("adresse", electeur.getAdresse());
            jsonObject.put("datenaiss", electeur.getDateNaiss());
            jsonObject.put("lieunaiss", electeur.getLieuNaiss());
            jsonObject.put("nompereelect", electeur.getNomPere());
            jsonObject.put("nommereelect", electeur.getNomMere());
            jsonObject.put("cinelect", electeur.getCinElect());
            jsonObject.put("num_serie_cin", electeur.getNserieCin());
            jsonObject.put("datedeliv", electeur.getDateDeliv());
            jsonObject.put("lieudeliv", electeur.getLieuDeliv());
            jsonObject.put("imageelect", electeur.getFicheElect());
            jsonObject.put("cinrecto", electeur.getCinRecto());
            jsonObject.put("cinverso", electeur.getCinVerso());
            jsonObject.put("drecensement", electeur.getDateinscription());
            Log.d("date inscr "," "+electeur.getDateinscription());
            AndroidNetworking.post(base_url + "Electeur/ElecteurApi")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.MEDIUM)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "Reponse Insert : " + response);
                            /*Toast toast = Toast.makeText(context, "ELECTEUR AJOUTER A LA BASE", Toast.LENGTH_LONG);
                            Intent i = new Intent(context, MenuActivity.class);
                            context.startActivity(i);
                            ((Activity)context).finish();*/
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
}

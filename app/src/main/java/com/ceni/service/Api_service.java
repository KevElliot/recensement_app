package com.ceni.service;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ceni.model.Electeur;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


public class Api_service {
    public static void addNewElecteur(Context context, String ip, String port, Electeur electeur, SharedPreferences resultat) {
        String base_url = "http://"+ip+":"+port+"/";
        try {
            SharedPreferences.Editor editor = resultat.edit();
            editor.remove("resultat").commit();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code_bv", electeur.getCode_bv());
            jsonObject.put("num_feuillet", electeur.getnFiche());
            jsonObject.put("nomelect", electeur.getNom());
            jsonObject.put("prenomelect", electeur.getPrenom());
            jsonObject.put("sexe", electeur.getSexe());
            jsonObject.put("profession", electeur.getProfession());
            jsonObject.put("adresse", electeur.getAdresse());
            jsonObject.put("datenaiss", electeur.getDateNaiss());
            jsonObject.put("nevers", electeur.getNevers());
            jsonObject.put("lieunaiss", electeur.getLieuNaiss());
            jsonObject.put("nompereelect", electeur.getNomPere());
            jsonObject.put("nommereelect", electeur.getNomMere());
            jsonObject.put("cinelect", electeur.getCinElect());
            jsonObject.put("num_serie_cin", electeur.getNserieCin());
            jsonObject.put("datedeliv", electeur.getDateDeliv());
            jsonObject.put("lieudeliv", electeur.getLieuDeliv());
            jsonObject.put("imagefeuillet", electeur.getFicheElect());
            jsonObject.put("cinrecto", electeur.getCinRecto());
            jsonObject.put("cinverso", electeur.getCinVerso());
            jsonObject.put("observation", electeur.getObservation());
            jsonObject.put("docreference", electeur.getDocreference());
            jsonObject.put("num_userinfo", electeur.getNum_userinfo());
            jsonObject.put("drecensement", electeur.getDateinscription());
            AndroidNetworking.post(base_url + "api/electeur")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.HIGH)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SharedPreferences.Editor editor = resultat.edit();
                            editor.putBoolean("resultat",true).commit();
                            editor.apply();
                            Log.d(TAG, "Reponse Insert : " + response);
                        }
                        @Override
                        public void onError(ANError error) {
                            SharedPreferences.Editor editor = resultat.edit();
                            editor.putBoolean("resultat",false).commit();
                            editor.apply();
                            Log.d("onError ",""+error);
//                            if (error.getErrorCode() != 0) {
//                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                                Log.d(TAG, "onError errorDetail1 : " + error.getErrorDetail());
//                            } else {
//                                Log.d(TAG, "onError errorDetail2 : " + error.getErrorDetail());
//                                Log.d(TAG, "onError errorDetail3 : " + error.getResponse());

//                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.ceni.recensementnumerique;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Configuration_model;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Task_insertElect extends AsyncTask<Void, Void, Void> {
    Api_service API;
    User us;
    Db_sqLite DB;
    Configuration_model params;
    Context c;
    Gson gson;
    Tablette tab;
    Button enregistrer;
    boolean result;


    public Task_insertElect(Context c, Configuration_model params, Button enregistrer, User us, Tablette tab) {
        this.c = c;
        this.params = params;
        this.enregistrer = enregistrer;
        this.us = us;
        this.tab=tab;
    }

    @Override
    protected void onPreExecute() {
        this.API = new Api_service();
        this.DB = new Db_sqLite(c);
        this.gson = new Gson();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(Void... voids) {
        result = false;

        Button tmp = enregistrer;

        Context c = params.getContext();
        String ip = params.getIp();
        String port = params.getPort();
        List<Electeur> listElect = params.getListElect();
        List<Document> documents = params.getDocuments();
        JSONArray notebooks = new JSONArray();

        documents.stream().forEach(document -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("idfdocreference", document.getIdfdocreference());
                jsonObject.put("code_bv", document.getDoccode_bv());
                jsonObject.put("numdocreference", document.getNumdocreference());
                jsonObject.put("datedocreference", document.getDatedocreference());
                jsonObject.put("voters", getVotersByDocument(document, listElect));
                notebooks.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("notebooks", notebooks);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("xxx", "DEBUGGING ...");
        Log.i("xxx", jsonObject.toString());

        API.insertNotebooks(DB, c, ip, port, tab, us, jsonObject, tmp);


//        for (int i = 0; i < listElect.size(); i++) {
//            Document doc = DB.selectDocumentbyid(listElect.get(i).getDocreference());
//            API.addNewDoc(DB,c,ip,port,listElect.get(i),tab,us,doc);
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

//        enregistrer.setEnabled(true);
//        enregistrer.setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private JSONArray getVotersByDocument(Document document, List<Electeur> voters){
        Stream<Electeur> electeurStream = voters.stream().filter(voter -> Objects.equals(voter.getDocreference(), document.getIdfdocreference()));
        JSONArray newVoters = new JSONArray();
        electeurStream.forEach(electeur -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", electeur.getIdElect());
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
                jsonObject.put("idfdocreference", electeur.getDocreference());
                jsonObject.put("num_userinfo", electeur.getNum_userinfo());
                jsonObject.put("drecensement", electeur.getDateinscription());

                newVoters.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return newVoters;
    }
}

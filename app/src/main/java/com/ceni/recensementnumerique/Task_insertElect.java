package com.ceni.recensementnumerique;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.model.Parametre_model;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class Task_insertElect extends AsyncTask<Void, Void, Void> {
    Api_service API;
    User us;
    Db_sqLite DB;
    Parametre_model params;
    Context c;
    Gson gson;
    Tablette tab;
    boolean result;


    public Task_insertElect(Context c, Parametre_model params, User us, Tablette tab) {
        this.c = c;
        this.params = params;
        this.us = us;
        this.tab=tab;
    }

    @Override
    protected void onPreExecute() {
        this.API = new Api_service();
        this.DB = new Db_sqLite(c);
        this.gson = new Gson();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        result = false;
        Context c = params.getContext();
        String ip = params.getIp();
        String port = params.getPort();
        List<Electeur> listElect = params.getListElect();
        SharedPreferences resultat = params.getResultat();
        for (int i = 0; i < listElect.size(); i++) {
            API.addNewElecteur(DB,c, ip, port, listElect.get(i), resultat,tab,us);
//            boolean res = resultat.getBoolean("resultat", false);
//            Log.d("eeee", "res =  " + res);
//            if (res) {
//                result = true;
//
//            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}

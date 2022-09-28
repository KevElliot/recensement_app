package com.ceni.recensementnumerique;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Configuration_model;
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

    @Override
    protected Void doInBackground(Void... voids) {
        result = false;
        Context c = params.getContext();
        String ip = params.getIp();
        String port = params.getPort();
        List<Electeur> listElect = params.getListElect();
        for (int i = 0; i < listElect.size(); i++) {
            Document doc = DB.selectDocumentbyid(listElect.get(i).getDocreference());
            API.addNewDoc(DB,c,ip,port,listElect.get(i),tab,us,doc);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        enregistrer.setVisibility(View.VISIBLE);
    }
}

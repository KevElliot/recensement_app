package com.ceni.recensementnumerique;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.ceni.model.ConfigurationMac_model;
import com.ceni.model.Configuration_model;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

class Task_insertTabs  extends AsyncTask<Void, Void, Void> {

   Api_service API;
   User us;
   Db_sqLite DB;
   ConfigurationMac_model params;
   Context c;
   Gson gson;
   Tablette tab;
   Button enregistrer;
   boolean result;

   public Task_insertTabs(Context c, ConfigurationMac_model params, Button enregistrer, User us, Tablette tab) {
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

      String tmpImei = tab.getImei();
      Tablette tbs = DB.selectImei(tmpImei);

      API.addNewInformationTabs(DB,c,ip,port,tbs,us);
      return null;
   }

   @Override
   protected void onPostExecute(Void aVoid) {

   }
}

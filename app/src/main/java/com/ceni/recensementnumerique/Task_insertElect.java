package com.ceni.recensementnumerique;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ceni.interfaces.CallBack_Interface;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Task_insertElect extends AsyncTask<Void, Void, String> {
    Api_service API;
    User us;
    Db_sqLite DB;
    Context context;
    Gson gson;
    Tablette tab;
    String ip, p;
    SharedPreferences resultat;
    Button enregistrer;
    public static ArrayList<Integer> tabsToStatistique;
    boolean result;

    public Task_insertElect(Context c, String ip, String p, SharedPreferences resultat, Button enregistrer, User us, Tablette tab) {
        this.context = c;
        this.ip = ip;
        this.p = p;
        this.resultat = resultat;
        this.enregistrer = enregistrer;
        this.us = us;
        this.tab = tab;
    }

    @Override
    protected void onPreExecute() {
        this.API = new Api_service();
        this.DB = new Db_sqLite(context);
        this.gson = new Gson();
        this.tabsToStatistique = new ArrayList<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(Void... voids) {
        result = false;
        Log.d("doInBackground", "tabsToStatistique : " + tabsToStatistique.size());
        API.insertNotebooks(DB, context, ip, p, tabsToStatistique,us, new CallBack_Interface() {
            @Override
            public void statistique(ArrayList<Integer> statistique) {
                Log.i("TAILLE STATISTIQUE" , "CALLBACK  - "+statistique.size());
                //Miantso an'ilay Statistique
                Intent intent = new Intent(context, StatistiqueActivity.class);
                String configTab = gson.toJson(tab);
                String user = gson.toJson(us);
                intent.putExtra("configTab", configTab);
                intent.putExtra("user", user);
                intent.putExtra("statistique", statistique);
                enregistrer.setEnabled(true);
                enregistrer.setClickable(true);
                context.startActivity(intent);
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        Log.d("onPostExecute", "tabsToStatistique : " + tabsToStatistique.size());
        Toast toast = Toast.makeText(context, "Electeur enregistré!", Toast.LENGTH_LONG);
        toast.show();
    }
}

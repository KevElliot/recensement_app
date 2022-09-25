package com.ceni.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class Configuration_model {
    private Context context;
    private String ip;
    private String port;
    private List<Electeur> listElect;
    private SharedPreferences resultat;



    public Configuration_model(Context context, String ip, String port, List<Electeur> listElect, SharedPreferences resultat) {
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.listElect = listElect;
        this.resultat = resultat;
    }
    public Configuration_model() {
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<Electeur> getListElect() {
        return listElect;
    }

    public void setListElect(List<Electeur> listElect) {
        this.listElect = listElect;
    }

    public SharedPreferences getResultat() {
        return resultat;
    }

    public void setResultat(SharedPreferences resultat) {
        this.resultat = resultat;
    }
}

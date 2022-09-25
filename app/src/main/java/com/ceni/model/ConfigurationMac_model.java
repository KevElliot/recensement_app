package com.ceni.model;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigurationMac_model {
    private Context context;
    private String ip;
    private String port;
    private Tablette Tabs;
    private SharedPreferences resultat;

    public ConfigurationMac_model(Context context, String ip, String port, Tablette tabs, SharedPreferences resultat) {
        this.context = context;
        this.ip = ip;
        this.port = port;
        Tabs = tabs;
        this.resultat = resultat;
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

    public Tablette getTabs() {
        return Tabs;
    }

    public void setTabs(Tablette tabs) {
        Tabs = tabs;
    }

    public SharedPreferences getResultat() {
        return resultat;
    }

    public void setResultat(SharedPreferences resultat) {
        this.resultat = resultat;
    }
}

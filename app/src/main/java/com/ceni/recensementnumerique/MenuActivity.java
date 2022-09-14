package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private static Button newElect;
    private static Button listeElect;
    private static Button documents;
    private static Button recherche;
    private static Button tablette;
    private static Tablette tab;
    private static User current_user;
    private ImageView profil,deco;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tablette = findViewById(R.id.tablette);
        newElect = findViewById(R.id.newInscription);
        listeElect = findViewById(R.id.listeSqLite);
        documents = findViewById(R.id.documents);
        recherche = findViewById(R.id.recherche);
        profil = findViewById(R.id.imageViewProfil);
        deco = findViewById(R.id.imageViewDeco);
        user = getIntent().getStringExtra("user");
        Gson gson = new Gson();
        tab = gson.fromJson(getIntent().getStringExtra("configTab"), Tablette.class);
        current_user = gson.fromJson(user, User.class);
        this.setTab(tab);
        this.setCurrent_user(current_user);
        Log.d("xx",tab.toString());

        tablette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecensementTablette.class);
                i.putExtra("user", user);
                String configTab = gson.toJson(tab);
                i.putExtra("configTab", configTab);
                startActivity(i);
            }
        });
        newElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newElect.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), LocalisationActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recherche.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), RechercheElecteur.class);
                startActivity(i);
            }
        });

        listeElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeElect.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), ListeFokontanyActivity.class);
                i.putExtra("user", user);
                startActivity(i);

            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InfoUserActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
        documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documents.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), DocumentActivity.class);
                startActivity(i);
            }
        });
        deco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public static void setListeElect(boolean x) {
        listeElect.setEnabled(x);
    }

    public static void setNewElect(boolean x) {
        newElect.setEnabled(x);
    }

    public static void setDocuments(boolean x) {
        documents.setEnabled(x);
    }

    public static void setRecherche(boolean x) {
        recherche.setEnabled(x);
    }

    public static Tablette getTab() {
        return tab;
    }

    public void setTab(Tablette tab) {
        this.tab = tab;
    }

    public static User getCurrent_user() {
        return current_user;
    }

    public static void setCurrent_user(User current_user) {
        MenuActivity.current_user = current_user;
    }
}
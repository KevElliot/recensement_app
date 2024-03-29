package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Configuration_model;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends AppCompatActivity{
    private ImageView previous;
    private EditText adressIp,port;
    private ProgressBar chargement;
    public Button enregistrer;
    private Api_service API;
    private Db_sqLite DB;
    private User user;
    private static SharedPreferences resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        this.previous = this.findViewById(R.id.imageViewPrevious);
        this.adressIp = this.findViewById(R.id.adressIp);
        this.port = this.findViewById(R.id.port);
        this.enregistrer = this.findViewById(R.id.enregistrerToutBtn);
        this.chargement = this.findViewById(R.id.progressBar2);
        enregistrer.setVisibility(View.VISIBLE);
        this.DB = new Db_sqLite(Configuration.this);
        this.API = new Api_service();
        Gson gson = new Gson();
        this.resultat = getSharedPreferences("response",Context.MODE_PRIVATE);
        Tablette tab = MenuActivity.getTab();
         this.user = MenuActivity.getCurrent_user();

        enregistrer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                enregistrerButtonDisable();

                // enregistrer.setVisibility(View.GONE);
                String ip = adressIp.getText().toString();
                String p = port.getText().toString();

                ArrayList<Long> tabsToStatistique = new ArrayList<Long>();

                new Task_insertElect(Configuration.this,ip,p,resultat,chargement,enregistrer, user, tab).execute();
            }
        });

        this.previous.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void enregistrerButtonEnable() {
        chargement.setVisibility(View.GONE);
        enregistrer.setEnabled(true);
        enregistrer.setClickable(true);
    }

    public void enregistrerButtonDisable() {
        chargement.setVisibility(View.VISIBLE);
        enregistrer.setEnabled(false);
        enregistrer.setClickable(false);
    }
}
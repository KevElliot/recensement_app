package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ceni.model.Electeur;
import com.ceni.model.Configuration_model;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class Configuration extends AppCompatActivity{
    private ImageView previous;
    private EditText adressIp,port;
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
        enregistrer.setVisibility(View.VISIBLE);
        this.DB = new Db_sqLite(Configuration.this);
        this.API = new Api_service();
        Gson gson = new Gson();
        this.resultat = getSharedPreferences("response",Context.MODE_PRIVATE);
        Tablette tab = MenuActivity.getTab();
         this.user = MenuActivity.getCurrent_user();
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enregistrer.setVisibility(View.GONE);
                String ip = adressIp.getText().toString();
                String p = port.getText().toString();
                List<Electeur> listElect = DB.selectElecteur();
                Configuration_model params = new Configuration_model(Configuration.this,ip,p,listElect,resultat);
                new Task_insertElect(Configuration.this,params,enregistrer,user,tab).execute();
            }
        });

        this.previous.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
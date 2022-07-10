package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;

public class MenuActivity extends AppCompatActivity{

    Button newElect,listeElect,documents;
    private ImageView profil;
    private Db_sqLite DB;
    private Api_service API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        newElect = findViewById(R.id.newInscription);
        listeElect = findViewById(R.id.listeSqLite);
        documents = findViewById(R.id.documents);
        profil = findViewById(R.id.imageViewProfil);
        API = new Api_service();
        DB = new Db_sqLite(this);

        newElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LocalisationActivity.class);
                startActivity(i);
            }
        });

        listeElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ListeElecteurActivity.class);
                startActivity(i);
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),InfoUserActivity.class);
                i.putExtra("user",getIntent().getStringExtra("user"));
                startActivity(i);
            }
        });
        documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),DocumentActivity.class);
                startActivity(i);
            }
        });
    }
}
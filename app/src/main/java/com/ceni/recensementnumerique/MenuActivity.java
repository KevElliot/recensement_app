package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;

public class MenuActivity extends AppCompatActivity {

    private static Button newElect;
    private static Button listeElect;
    private static Button documents;
    private static Button recherche;
    private ImageView profil,deco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        newElect = findViewById(R.id.newInscription);
        listeElect = findViewById(R.id.listeSqLite);
        documents = findViewById(R.id.documents);
        recherche = findViewById(R.id.recherche);
        profil = findViewById(R.id.imageViewProfil);
        deco = findViewById(R.id.imageViewDeco);

        newElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newElect.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), LocalisationActivity.class);
                i.putExtra("user", getIntent().getStringExtra("user"));
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
                startActivity(i);
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InfoUserActivity.class);
                i.putExtra("user", getIntent().getStringExtra("user"));
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
                finish();
                startActivity(i);
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
}
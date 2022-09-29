package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceni.model.User;
import com.google.gson.Gson;

public class InfoUserActivity extends AppCompatActivity {
    private TextView nom,prenom,region,district,commune,nbsaisi;
    private ImageView retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        retour = findViewById(R.id.retour);
        nom = findViewById(R.id.nomUser);
        prenom = findViewById(R.id.prenomUser);
        region = findViewById(R.id.regionUser);
        district = findViewById(R.id.districtUser);
        commune = findViewById(R.id.communeUser);
        nbsaisi = findViewById(R.id.nbsaisi);
        Gson gson = new Gson();
        User user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        Log.d("----------", "onCreate: " +user.toString());

        SharedPreferences params_localisation = this.getSharedPreferences("params_localisation", Context.MODE_PRIVATE);
        String tmpCOmmune = params_localisation.getString("label_commune", "");

        nom.setText(user.getNomUser());
        prenom.setText(user.getPrenomUser());
        region.setText(user.getRegionUser());
        district.setText(user.getDistrictUser());
        commune.setText(tmpCOmmune);
        nbsaisi.setText(""+user.getNbSaisi());
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
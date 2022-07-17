package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ceni.adapter.ListElecteurAdapter;
import com.ceni.model.Electeur;
import com.ceni.model.ListFokontany;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class ListeElecteurActivity extends AppCompatActivity {
    static ListeElecteurActivity listeElecteurActivity;
    private ListView listViewElect;
    private TextView fokontanyLabel;
    private ImageView retour;
    Db_sqLite DB;
    List<Electeur> listElect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_electeur);
        listeElecteurActivity = this;
        listViewElect = findViewById(R.id.listElecteur);
        fokontanyLabel = findViewById(R.id.fokontanyLabel);
        retour = findViewById(R.id.imageViewPrevious);
        this.DB = new Db_sqLite(ListeElecteurActivity.this);
        Gson gson = new Gson();
        ListFokontany fokontany = gson.fromJson(getIntent().getStringExtra("fokontany"), ListFokontany.class);
        String code_fokontany = fokontany.getCodeFokontany();
        listElect = DB.selectElecteurbycodeFokontany(code_fokontany);
        fokontanyLabel.setText("Fokontany: "+fokontany.getFokontany());
        ListElecteurAdapter listElecteurAdapter = new ListElecteurAdapter(ListeElecteurActivity.this, listElect);
        listViewElect.setAdapter(listElecteurAdapter);
        listViewElect.setClickable(true);
        listViewElect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Gson gson = new Gson();
                String myjson = gson.toJson(listElect.get(position));
                Log.i("Liste electeur activity", myjson);
                Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
                i.putExtra("electeur", myjson);
                startActivity(i);
            }
        });

        //Button retour
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public static ListeElecteurActivity getInstance() {
        return listeElecteurActivity;
    }
}
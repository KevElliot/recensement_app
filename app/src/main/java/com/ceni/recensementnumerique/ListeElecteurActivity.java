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
import android.widget.Toast;

import com.ceni.adapter.ListElecteurAdapter;
import com.ceni.model.Electeur;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class ListeElecteurActivity extends AppCompatActivity {
    ListView listViewElect;
    Button enregistrer;
    ImageView retour;
    Api_service API;
    Db_sqLite DB;
    boolean delete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_electeur);
        listViewElect = findViewById(R.id.listElecteur);
        enregistrer = findViewById(R.id.enregistrerToutBtn);
        retour =  findViewById(R.id.imageViewPrevious);
        this.DB = new Db_sqLite(ListeElecteurActivity.this);
        API = new Api_service();
        List<Electeur> listElect = DB.selectElecteur();
        if (listElect.size() <= 0) {
            Toast toast = Toast.makeText(ListeElecteurActivity.this, "Tsy misy pifidy voasoratra!", Toast.LENGTH_LONG);
            toast.show();
        }
        ListElecteurAdapter listElecteurAdapter = new ListElecteurAdapter(ListeElecteurActivity.this, listElect);
        listViewElect.setAdapter(listElecteurAdapter);
        listViewElect.setClickable(true);
        listViewElect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Electeur electeur = new Electeur();
                electeur.setCode_bv(listElect.get(position).getCode_bv());
                electeur.setIdElect(listElect.get(position).getIdElect());
                electeur.setnFiche(listElect.get(position).getnFiche());
                electeur.setNom(listElect.get(position).getNom());
                electeur.setPrenom(listElect.get(position).getPrenom());
                electeur.setSexe(listElect.get(position).getSexe());
                electeur.setProfession(listElect.get(position).getProfession());
                electeur.setAdresse(listElect.get(position).getAdresse());
                electeur.setDateNaiss(listElect.get(position).getDateNaiss());
                electeur.setLieuNaiss(listElect.get(position).getLieuNaiss());
                electeur.setNomPere(listElect.get(position).getNomPere());
                electeur.setNomMere(listElect.get(position).getNomMere());
                electeur.setCinElect(listElect.get(position).getCinElect());
                electeur.setNserieCin(listElect.get(position).getNserieCin());
                electeur.setDateDeliv(listElect.get(position).getDateDeliv());
                electeur.setLieuDeliv(listElect.get(position).getLieuDeliv());
                electeur.setFicheElect(listElect.get(position).getFicheElect());
                electeur.setCinRecto(listElect.get(position).getCinRecto());
                electeur.setCinVerso(listElect.get(position).getCinVerso());
                electeur.setDateinscription(listElect.get(position).getDateinscription());

                Gson gson = new Gson();
                String myjson = gson.toJson(electeur);
                Log.i("Liste electeur activity", myjson);
                Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
                i.putExtra("electeur", myjson);
                startActivity(i);
                finish();
            }
        });
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < listElect.size(); i++) {
                    API.addNewElecteur(ListeElecteurActivity.this, listElect.get(i));
                    //boolean deleted = DB.deleteElect(listElect.get(i).getCinElect());
                }
                Toast toast = Toast.makeText(ListeElecteurActivity.this, "Electeur enregistrer!", Toast.LENGTH_LONG);
                toast.show();
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                finish();
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
}
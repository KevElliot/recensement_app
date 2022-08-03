package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceni.model.Electeur;
import com.google.gson.Gson;

public class ModifierElecteur extends AppCompatActivity {
    private EditText nFiche,nomElect,prenomElect,professionElect,adresseElect,lieuNaissElect,nomPereElect,nomMereElect,cinElect,nSerie,lieuCinElect,editTextNevers;
    private ImageView ficheElect, cin_recto,cin_verso;
    private Button button_image_fiche, pick_date_button, pick_datecin, button_image_recto, button_image_verso;
    private CheckBox sexeHomme, sexeFemme, nevers, observation1, observation2, observation3;
    private TextView dateNaissElect, dateCinElect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_electeur);
        Gson gson = new Gson(electeur.get);
        Electeur electeur = gson.fromJson(getIntent().getStringExtra("electeur"), Electeur.class);
        nFiche = findViewById(R.id.nFiche);
        nomElect = findViewById(R.id.nomElect);
        prenomElect = findViewById(R.id.prenomElect);
        professionElect = findViewById(R.id.professionElect);
        adresseElect = findViewById(R.id.adresseElect);
        lieuNaissElect = findViewById(R.id.lieuNaissElect);
        nomPereElect = findViewById(R.id.nomPereElect);
        nomMereElect = findViewById(R.id.nomMereElect);
        cinElect = findViewById(R.id.cinElect);
        nSerie = findViewById(R.id.nSerie);
        lieuCinElect = findViewById(R.id.lieuCinElect);
        editTextNevers= findViewById(R.id.editTextNevers);
        ficheElect = findViewById(R.id.ficheElect);
        cin_recto = findViewById(R.id.cin_recto);
        cin_verso = findViewById(R.id.cin_verso);
        button_image_fiche= findViewById(R.id.button_image_fiche);
        pick_date_button = findViewById(R.id.pick_date_button);
        pick_datecin = findViewById(R.id.pick_datecin);
        button_image_recto= findViewById(R.id.button_image_recto);
        button_image_verso = findViewById(R.id.button_image_verso);
        sexeHomme = findViewById(R.id.sexeHomme);
        sexeFemme = findViewById(R.id.sexeFemme);
        nevers = findViewById(R.id.nevers);
        observation1 = findViewById(R.id.Observation1);
        observation2 = findViewById(R.id.Observation2);
        observation3 = findViewById(R.id.Observation3);
        dateNaissElect = findViewById(R.id.dateNaissElect);
        dateCinElect = findViewById(R.id.dateCinElect);


        nFiche.setText(electeur.getnFiche());
        nomElect.setText(electeur.getNom());
        prenomElect.setText(electeur.getPrenom());
        professionElect.setText(electeur.getProfession());
        adresseElect.setText(electeur.getAdresse());
        lieuNaissElect.setText(electeur.getLieuNaiss());
        nomPereElect.setText(electeur.getNomPere());
        nomMereElect.setText(electeur.getNomMere());
        cinElect.setText(electeur.getCinElect());
        nSerie.setText(electeur.getNserieCin());
        lieuCinElect.setText(electeur.getLieuDeliv());
        editTextNevers.setText(electeur.getNevers());
        ficheElect.setText(electeur.get);
        cin_recto.setText(electeur.get);
        cin_verso.setText(electeur.get);
        sexeHomme.setText(electeur.get);
        sexeFemme.setText(electeur.get);
        nevers.setText(electeur.get);
        observation1.setText(electeur.get);
        observation2.setText(electeur.get);
        observation3.setText(electeur.get);
        dateNaissElect.setText(electeur.getDateNaiss());
        dateCinElect.setText(electeur.getDateDeliv());
    }
}
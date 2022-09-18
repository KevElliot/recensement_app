package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApercuInscriptionActivity extends AppCompatActivity {
    private ImageView ficheElect, cinRecto, cinVerso, retour;
    private TextView nfiche, dateInscr, nom, prenom, sexe, profession, adresse, dateNaiss, lieuNaiss, nomPere, nomMere, cinElect, nserie, dateDeliv, lieuDeliv, observation;
    private Button enregistrer;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apercu_inscription);
        retour = findViewById(R.id.retour);
        enregistrer = findViewById(R.id.enregistrer);
        ficheElect = findViewById(R.id.ficheElect);
        dateInscr = findViewById(R.id.dateInscr);
        cinRecto = findViewById(R.id.cin_recto);
        cinVerso = findViewById(R.id.cin_verso);
        nfiche = findViewById(R.id.nFiche);
        nom = findViewById(R.id.nomElect);
        prenom = findViewById(R.id.prenomElect);
        sexe = findViewById(R.id.sexeElect);
        profession = findViewById(R.id.professionElect);
        adresse = findViewById(R.id.adresseElect);
        dateNaiss = findViewById(R.id.dateNaissElect);
        lieuNaiss = findViewById(R.id.lieuNaissElect);
        nomPere = findViewById(R.id.nomPereElect);
        nomMere = findViewById(R.id.nomMereElect);
        cinElect = findViewById(R.id.cinElect);
        nserie = findViewById(R.id.nSerie);
        dateDeliv = findViewById(R.id.dateCinElect);
        lieuDeliv = findViewById(R.id.lieuCinElect);
        observation = findViewById(R.id.observation);
        Gson gson = new Gson();
        Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
        nfiche.setText(electeur.getnFiche());
        dateInscr.setText(electeur.getDateinscription());
        nom.setText(electeur.getNom());
        prenom.setText(electeur.getPrenom());
        sexe.setText(electeur.getSexe());
        profession.setText(electeur.getProfession());
        adresse.setText(electeur.getAdresse());
        if (electeur.getDateNaiss().length() != 0) {
            dateNaiss.setText(electeur.getDateNaiss());
        } else {
            dateNaiss.setText(electeur.getNevers());
        }
        lieuNaiss.setText(electeur.getLieuNaiss());
        nomPere.setText(electeur.getNomPere());
        nomMere.setText(electeur.getNomMere());
        cinElect.setText(electeur.getCinElect());
        nserie.setText(electeur.getNserieCin());
        dateDeliv.setText(electeur.getDateDeliv());
        lieuDeliv.setText(electeur.getLieuDeliv());
        Bitmap imgFiche = this.decodeImage(electeur.getFicheElect());
        Bitmap imgCinRecto = this.decodeImage(electeur.getCinRecto());
        Bitmap imgCinVerso = this.decodeImage(electeur.getCinVerso());
        ficheElect.setImageBitmap(imgFiche);
        cinRecto.setImageBitmap(imgCinRecto);
        cinVerso.setImageBitmap(imgCinVerso);
        observation.setText(electeur.getObservation());
        user = getIntent().getStringExtra("user");

        enregistrer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Db_sqLite DB = new Db_sqLite(ApercuInscriptionActivity.this);
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                Log.d("Electeur final:", "Electeur : " + electeur.toString());
                Log.d("Aperçu ACTIVITY", "id DOC REF:  " + electeur.getDocreference());
                boolean result = DB.insertElecteurData(electeur.getCode_bv(), electeur.getnFiche(), electeur.getNom(), electeur.getPrenom(), electeur.getSexe(), electeur.getProfession(), electeur.getAdresse(), electeur.getDateNaiss(), electeur.getNevers(), electeur.getLieuNaiss(), electeur.getNomPere(), electeur.getNomMere(), electeur.getCinElect(), electeur.getNserieCin(), electeur.getDateDeliv(), electeur.getLieuDeliv(), electeur.getFicheElect(), electeur.getCinRecto(), electeur.getCinVerso(), electeur.getObservation(), electeur.getDocreference(), electeur.getDateinscription());
                if (result) {
                    Document doc = DB.selectDocumentbyid(electeur.getDocreference());
                    User us = gson.fromJson(user, User.class);
                    DB.counterStat(doc,us,1);
                    Toast toast = Toast.makeText(ApercuInscriptionActivity.this, "Electeur enregistré!", Toast.LENGTH_LONG);
                    toast.show();
                    LocalisationActivity.getInstance().finish();
                    ImageFicheActivity.getInstance().finish();
                    Inscription2Activity.getInstance().finish();
                    InscriptionActivity.getInstance().finish();
                    ImageCinActivity.getInstance().finish();
                    Intent i = new Intent(getApplicationContext(), ListeFokontanyActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                } else {
                    Toast toast = Toast.makeText(ApercuInscriptionActivity.this, "Erreur à l'enregistrement!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public Bitmap decodeImage(String dataImageElect) {
        //Decode image
        byte[] img = Base64.decode(dataImageElect, Base64.DEFAULT);
        Bitmap bm;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        bm = BitmapFactory.decodeByteArray(img, 0, img.length, opt);
        return bm;
    }
}
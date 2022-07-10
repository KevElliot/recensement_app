package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ObservationActivity extends AppCompatActivity {
    private ImageView previous, next;
    private CheckBox obs1,obs2,obs3;
    private Db_sqLite DB;
    private Api_service API;
    private String observation ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        this.obs1 = this.findViewById(R.id.Observation1);
        this.obs2 = this.findViewById(R.id.Observation2);
        this.obs3 = this.findViewById(R.id.Observation3);
        this.next = this.findViewById(R.id.imageViewNext);
        this.previous = this.findViewById(R.id.imageViewPrevious);
        DB = new Db_sqLite(this);
        API = new Api_service();

        obs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obs1.isChecked()) {
                    observation = "Nouveau titulaire CIN";
                    obs1.setChecked(true);
                    obs2.setChecked(false);
                    obs3.setChecked(false);
                } else {
                    observation = "JSN/CIN";
                    obs1.setChecked(false);
                    obs2.setChecked(true);
                    obs3.setChecked(false);
                }
            }
        });
        obs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obs2.isChecked()) {
                    observation = "JSN/CIN";
                    obs1.setChecked(false);
                    obs2.setChecked(true);
                    obs3.setChecked(false);
                } else {
                    observation = "Nouveau recensement electeur";
                    obs1.setChecked(false);
                    obs2.setChecked(false);
                    obs3.setChecked(true);
                }
            }
        });
        obs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obs3.isChecked()) {
                    observation = "Nouveau recensement electeur";
                    obs1.setChecked(false);
                    obs2.setChecked(false);
                    obs3.setChecked(true);
                } else {
                    observation = "Nouveau titulaire CIN";
                    obs1.setChecked(true);
                    obs2.setChecked(false);
                    obs3.setChecked(false);
                }
            }
        });
        this.next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                if (observation != "") {
                    electeur.setObservation(observation);
                    Date daty = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dat = sdf.format(daty.getTime());
                    electeur.setDateinscription("" + dat);

                    boolean result = DB.insertElecteurData(electeur.getCode_bv(), electeur.getnFiche(), electeur.getNom(), electeur.getPrenom(), electeur.getSexe(), electeur.getProfession(), electeur.getAdresse(), electeur.getDateNaiss(),electeur.getNevers(), electeur.getLieuNaiss(), electeur.getNomPere(), electeur.getNomMere(), electeur.getCinElect(), electeur.getNserieCin(), electeur.getDateDeliv(), electeur.getLieuDeliv(), electeur.getFicheElect(), electeur.getCinRecto(), electeur.getCinVerso(),electeur.getObservation(),electeur.getDocreference(), electeur.getDateinscription());
                    if (result) {
                        Toast toast = Toast.makeText(ObservationActivity.this, "Electeur enregistrer!", Toast.LENGTH_LONG);
                        toast.show();
                        LocalisationActivity.getInstance().finish();
                        ImageFicheActivity.getInstance().finish();
                        Inscription2Activity.getInstance().finish();
                        InscriptionActivity.getInstance().finish();
                        ImageCinActivity.getInstance().finish();
                        Intent i = new Intent(getApplicationContext(), ListeElecteurActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(ObservationActivity.this, "Erreur à l'enregistrement!", Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
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
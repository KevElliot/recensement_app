package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ceni.adapter.SpinerBvAdapter;
import com.ceni.adapter.SpinerCommuneAdapter;
import com.ceni.adapter.SpinerCvAdapter;
import com.ceni.adapter.SpinerFokontanyAdapter;
import com.ceni.model.Bv;
import com.ceni.model.Commune;
import com.ceni.model.Cv;
import com.ceni.model.Electeur;
import com.ceni.model.Fokontany;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class LocalisationActivity extends AppCompatActivity {
    static LocalisationActivity localisationActivity;
    private Spinner spinnerCommune,spinnerFokontany,spinnerCv,spinnerBv;
    private List<Commune> communes;
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private ImageView next, previous;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);
        MenuActivity.setNewElect(true);
        Db_sqLite DB = new Db_sqLite(this);
        this.next = this.findViewById(R.id.buttonNext);
        previous = this.findViewById(R.id.imageViewPrevious);
        localisationActivity = this;
        Gson gson = new Gson();
        //User user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        User user = LoginActivity.getUser();
        String codeDistrict = user.getCode_district();
        String idUser = user.getIdUser();
        this.communes = DB.selectCommuneFromDistrict(codeDistrict);
        this.spinnerCommune = (Spinner) this.findViewById(R.id.spinner_commune);


        // Adapter Commune
        SpinerCommuneAdapter adapterCommune = new SpinerCommuneAdapter(LocalisationActivity.this,
                R.layout.dropdown_localisation,
                R.id.textViewLabel,
                R.id.textViewCode,
                this.communes);
        this.spinnerCommune.setAdapter(adapterCommune);
        this.spinnerCommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Commune communeSelected = (Commune) spinnerCommune.getSelectedItem();
                Toast toast = Toast.makeText(LocalisationActivity.this, "COMMUNE: "+communeSelected.getLabel_commune(), Toast.LENGTH_LONG);
                toast.show();
                fokontany = DB.selectFokotanyFromCommune(communeSelected.getCode_commune());
                spinnerFokontany = (Spinner) LocalisationActivity.this.findViewById(R.id.spinner_fokontany);
                // Adapter Fokontany
                SpinerFokontanyAdapter adapterFokontany = new SpinerFokontanyAdapter(LocalisationActivity.this,
                        R.layout.dropdown_localisation,
                        R.id.textViewLabel,
                        R.id.textViewCode,
                        fokontany);
                spinnerFokontany.setAdapter(adapterFokontany);
                spinnerFokontany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Fokontany fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                        Toast toast = Toast.makeText(LocalisationActivity.this, "FOKONTANY: "+fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                        toast.show();
                        cv = DB.selectCvFromFokontany(fokontanySelected.getCode_fokontany());
                        spinnerCv = (Spinner) LocalisationActivity.this.findViewById(R.id.spinner_cv);
                        // Adapter Cv
                        SpinerCvAdapter adapterCv = new SpinerCvAdapter(LocalisationActivity.this,
                                R.layout.dropdown_localisation,
                                R.id.textViewLabel,
                                R.id.textViewCode,
                                cv);
                        spinnerCv.setAdapter(adapterCv);
                        spinnerCv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Cv cvSelected = (Cv) spinnerCv.getSelectedItem();
                                Toast toast = Toast.makeText(LocalisationActivity.this, "CV : "+cvSelected.getLabel_cv(), Toast.LENGTH_LONG);
                                toast.show();
                                bv = DB.selectBvFromCv(cvSelected.getCode_cv());
                                spinnerBv = (Spinner) LocalisationActivity.this.findViewById(R.id.spinner_bv);
                                // Adapter Bv
                                SpinerBvAdapter adapterBv = new SpinerBvAdapter(LocalisationActivity.this,
                                        R.layout.dropdown_localisation,
                                        R.id.textViewLabel,
                                        R.id.textViewCode,
                                        bv);
                                spinnerBv.setAdapter(adapterBv);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bv bvSelected = (Bv) spinnerBv.getSelectedItem();
                Electeur electeur = new Electeur();
                electeur.setCode_bv(bvSelected.getCode_bv());
                electeur.setNum_userinfo(idUser);
                Gson gson = new Gson();
                String myJson = gson.toJson(electeur);
                Intent i = new Intent(getApplicationContext(),InscriptionActivity.class);
                i.putExtra("user", getIntent().getStringExtra("user"));
                i.putExtra("newElect", myJson);
                startActivity(i);
            }
        });
        //Button previous
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public static LocalisationActivity getInstance(){
        return localisationActivity;
    }
}
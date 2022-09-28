package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.adapter.SpinerCommuneAdapter;
import com.ceni.adapter.SpinerFokontanyAdapter;
import com.ceni.model.Bv;
import com.ceni.model.Commune;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Fokontany;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class RecensementTablette extends AppCompatActivity {
    private Spinner spinnerCommune, spinnerFokontany;
    private EditText responsable;
    private List<Commune> communes;
    private List<Fokontany> fokontany;
    private ImageView previous;
    private TextView macWifi,imei;
    private Button enregistrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recensement_tablette);
        Db_sqLite DB = new Db_sqLite(this);
        Gson gson = new Gson();
        User user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        Tablette tablette = gson.fromJson(getIntent().getStringExtra("configTab"), Tablette.class);
        Log.d("tag",tablette.toString());
        String codeDistrict = user.getCode_district();
        previous = this.findViewById(R.id.imageViewPrevious);
        responsable = this.findViewById(R.id.nomResponsable);
        macWifi = this.findViewById(R.id.macwifi);
        macWifi.setText(tablette.getMacWifi());
        imei = this.findViewById(R.id.imei);
        imei.setText(tablette.getImei());
        enregistrer = this.findViewById(R.id.enregistrer);
        this.communes = DB.selectCommuneFromDistrict(codeDistrict);
        this.spinnerCommune = (Spinner) this.findViewById(R.id.spinner_commune);
        SpinerCommuneAdapter adapterCommune = new SpinerCommuneAdapter(RecensementTablette.this,
                R.layout.dropdown_localisation,
                R.id.textViewLabel,
                R.id.textViewCode,
                this.communes);
        this.spinnerCommune.setAdapter(adapterCommune);
        this.spinnerCommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Commune communeSelected = (Commune) spinnerCommune.getSelectedItem();
                Toast toast = Toast.makeText(RecensementTablette.this, "COMMUNE: " + communeSelected.getLabel_commune(), Toast.LENGTH_LONG);
                toast.show();
                fokontany = DB.selectFokotanyFromCommune(communeSelected.getCode_commune());
                spinnerFokontany = (Spinner) RecensementTablette.this.findViewById(R.id.spinner_fokontany);
                // Adapter Fokontany
                SpinerFokontanyAdapter adapterFokontany = new SpinerFokontanyAdapter(RecensementTablette.this,
                        R.layout.dropdown_localisation,
                        R.id.textViewLabel,
                        R.id.textViewCode,
                        fokontany);
                spinnerFokontany.setAdapter(adapterFokontany);
                spinnerFokontany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Fokontany fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                        Toast toast = Toast.makeText(RecensementTablette.this, "FOKONTANY: " + fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomResp = responsable.getText().toString();
                if(nomResp.length()!=0) {
                    Commune communeSelected = (Commune) spinnerCommune.getSelectedItem();
                    Fokontany fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                    Tablette tab = new Tablette();

                    tab.setRegion(user.getRegionUser());
                    tab.setCode_region(user.getCode_region());
                    tab.setDistrict(user.getDistrictUser());
                    tab.setCode_district(user.getCode_district());
                    tab.setCommune(communeSelected.getLabel_commune());
                    tab.setCode_commune(communeSelected.getCode_commune());
                    tab.setCode_fokontany(fokontanySelected.getCode_fokontany());
                    tab.setFokontany(fokontanySelected.getLabel_fokontany());
                    tab.setResponsable(nomResp);
                    tab.setImei(tablette.getImei());
                    tab.setMacWifi(tablette.getMacWifi());

                    boolean tmp = DB.findIMEISimilare(tab);

                    if(tmp){
                        Intent intent = new Intent(getApplicationContext(), ConfigurationMac.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("tag",tab.toString());
                        boolean result = DB.insertInformationTablette(tab);
                        Log.d("TO :", String.valueOf(result));

                        if (result) {
                            Intent intent = new Intent(getApplicationContext(), ConfigurationMac.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(RecensementTablette.this, "Erreur à l'enregistrement de l'information tablette", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                }else{
                    responsable.setError("Mila fenoina");
                }
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
}
package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ceni.adapter.SpinerCommuneAdapter;
import com.ceni.adapter.SpinerDistrictAdapter;
import com.ceni.adapter.SpinerRegionAdapter;
import com.ceni.model.Commune;
import com.ceni.model.District;
import com.ceni.model.Region;
import com.ceni.model.User;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;

import java.util.List;

public class RegisterUserActivity extends AppCompatActivity {
    static RegisterUserActivity registerUserActivity;
    private Spinner spinnerRegion, spinnerDistrict, spinnerCommune;
    private List<Region> region;
    private List<District> district;
    private List<Commune> commune;
    private Button enregistrer;
    private EditText txtNom, txtPrenom, txtPseudo, txtMdp;
    private boolean inserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Db_sqLite DB = new Db_sqLite(this);
        Api_service API = new Api_service();
        enregistrer = this.findViewById(R.id.enregistrerBtn);
        txtNom = this.findViewById(R.id.nomUser);
        txtPrenom = this.findViewById(R.id.prenomUser);
        txtPseudo = this.findViewById(R.id.pseudoUser);
        txtMdp = this.findViewById(R.id.mdpUser);
        this.spinnerRegion = (Spinner) this.findViewById(R.id.spinner_region);
        this.spinnerDistrict = (Spinner) this.findViewById(R.id.spinner_district);
        this.spinnerCommune = (Spinner) this.findViewById(R.id.spinner_commune);
        this.region = DB.selectRegion();
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Region regionSelected = (Region) spinnerRegion.getSelectedItem();
                District districtSelected = (District) spinnerDistrict.getSelectedItem();
                Commune communeSelected = (Commune) spinnerCommune.getSelectedItem();
                String nom = txtNom.getText().toString();
                String prenom = txtPrenom.getText().toString();
                String pseudo = txtPseudo.getText().toString();
                String mdp = txtMdp.getText().toString();
                String regionUser = regionSelected.getLabel_region();
                String code_region = regionSelected.getCode_region();
                String districtUser = districtSelected.getLabel_district();
                String code_district = districtSelected.getCode_district();
                String commune = communeSelected.getLabel_commune();
                String code_commune = communeSelected.getCode_commune();
                User user = new User(nom, prenom, "CID", pseudo, mdp, regionUser, code_region, districtUser, code_district, commune, code_commune, 0);
                //mampiditra utilisateur any aminy WS
                // API.addNewUser(RegisterUserActivity.this,user);
                inserted = DB.insertUser(user);
                if (inserted) {
                    Toast.makeText(RegisterUserActivity.this, "Utilisateur inserer", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterUserActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        SpinerRegionAdapter adapterRegion = new SpinerRegionAdapter(RegisterUserActivity.this,
                R.layout.dropdown_localisation,
                R.id.textViewLabel,
                R.id.textViewCode,
                this.region);
        this.spinnerRegion.setAdapter(adapterRegion);
        this.spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Region regionSelected = (Region) spinnerRegion.getSelectedItem();
                Toast toast = Toast.makeText(RegisterUserActivity.this, "REGION: " + regionSelected.getLabel_region(), Toast.LENGTH_LONG);
                toast.show();
                district = DB.selectDistrictFromRegion(regionSelected.getCode_region());

                SpinerDistrictAdapter adapterDistrict = new SpinerDistrictAdapter(RegisterUserActivity.this,
                        R.layout.dropdown_localisation,
                        R.id.textViewLabel,
                        R.id.textViewCode,
                        district);
                spinnerDistrict.setAdapter(adapterDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       this.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               District districtSelected = (District) spinnerDistrict.getSelectedItem();
               commune = DB.selectCommuneFromDistrict(districtSelected.getCode_district());
               SpinerCommuneAdapter adapterCommune = new SpinerCommuneAdapter(RegisterUserActivity.this,
                       R.layout.dropdown_localisation,
                       R.id.textViewLabel,
                       R.id.textViewCode,
                       commune);
               spinnerCommune.setAdapter(adapterCommune);
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

    }

    public static RegisterUserActivity getInstance() {
        return registerUserActivity;
    }
}

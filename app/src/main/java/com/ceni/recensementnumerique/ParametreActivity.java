package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.ceni.model.Fokontany;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class ParametreActivity extends AppCompatActivity {
    private Spinner spinnerCommune,spinnerFokontany,spinnerCv,spinnerBv;
    private List<Commune> communes;
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private ImageView previous;
    private static SharedPreferences params_localisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        if(MenuActivity.getInstance()!=null){
            MenuActivity.setParametre(true);
        }
        Db_sqLite DB = new Db_sqLite(this);
        User user = LoginActivity.getUser();
        Gson gson = new Gson();
        Tablette tab = gson.fromJson(getIntent().getStringExtra("configTab"), Tablette.class);
        String codeDistrict = user.getCode_district();
        previous = this.findViewById(R.id.imageViewPrevious);
        this.communes = DB.selectCommuneFromDistrict(codeDistrict);
        spinnerCommune = (Spinner) this.findViewById(R.id.spinner_commune);
        spinnerFokontany = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_fokontany);
        params_localisation = this.getSharedPreferences("params_localisation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = params_localisation.edit();

        if(params_localisation!=null){
            // Adapter Commune
            SpinerCommuneAdapter adapterCommune = new SpinerCommuneAdapter(ParametreActivity.this,
                    R.layout.dropdown_localisation,
                    R.id.textViewLabel,
                    R.id.textViewCode,
                    this.communes);

            this.spinnerCommune.setAdapter(adapterCommune);
            int commune_pref = params_localisation.getInt("position_commune",0);
           this.spinnerCommune.setSelection(commune_pref);
            this.spinnerCommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Commune communeSelected = (Commune) spinnerCommune.getSelectedItem();
                    editor.putString("code_commune", communeSelected.getCode_commune());
                    editor.putString("label_commune", communeSelected.getLabel_commune());
                    editor.putInt("position_commune", spinnerCommune.getSelectedItemPosition());
                    editor.apply();
                    Toast toast = Toast.makeText(ParametreActivity.this, "COMMUNE: " + communeSelected.getLabel_commune(), Toast.LENGTH_LONG);
                    toast.show();
                    fokontany = DB.selectFokotanyFromCommune(communeSelected.getCode_commune());
                    // Adapter Fokontany
                    SpinerFokontanyAdapter adapterFokontany = new SpinerFokontanyAdapter(ParametreActivity.this,
                            R.layout.dropdown_localisation,
                            R.id.textViewLabel,
                            R.id.textViewCode,
                            fokontany);
                    //spinnerFokontany.setSelection(3);
                    spinnerFokontany.setAdapter(adapterFokontany);
                    int fokontany_pref = params_localisation.getInt("position_fokontany",0);
                    spinnerFokontany.setSelection(fokontany_pref);
                    spinnerFokontany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Fokontany fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                            editor.putString("code_fokontany", fokontanySelected.getCode_fokontany());
                            editor.putString("label_fokontany", fokontanySelected.getLabel_fokontany());
                            editor.putInt("position_fokontany", spinnerFokontany.getSelectedItemPosition());
                            editor.apply();
                            Toast toast = Toast.makeText(ParametreActivity.this, "FOKONTANY: " + fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                            toast.show();
                            cv = DB.selectCvFromFokontany(fokontanySelected.getCode_fokontany());
                            spinnerCv = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_cv);
                            spinnerCv.setEnabled(false);
                            // Adapter Cv
                            SpinerCvAdapter adapterCv = new SpinerCvAdapter(ParametreActivity.this,
                                    R.layout.dropdown_localisation,
                                    R.id.textViewLabel,
                                    R.id.textViewCode,
                                    cv);
                            spinnerCv.setAdapter(adapterCv);
                            spinnerCv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Cv cvSelected = (Cv) spinnerCv.getSelectedItem();
                                    Toast toast = Toast.makeText(ParametreActivity.this, "CV : " + cvSelected.getLabel_cv(), Toast.LENGTH_LONG);
                                    toast.show();
                                    bv = DB.selectBvFromCv(cvSelected.getCode_cv());
                                    spinnerBv = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_bv);
                                    spinnerBv.setEnabled(false);
                                    // Adapter Bv
                                    SpinerBvAdapter adapterBv = new SpinerBvAdapter(ParametreActivity.this,
                                            R.layout.dropdown_localisation,
                                            R.id.textViewLabel,
                                            R.id.textViewCode,
                                            bv);
                                    spinnerBv.setAdapter(adapterBv);
                                    spinnerBv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            Bv bvSelecter = (Bv)spinnerBv.getSelectedItem();
                                            editor.putString("code_bv", bvSelecter.getCode_bv());
                                            editor.putString("label_bv", bvSelecter.getLabel_bv());
                                            editor.putInt("position_bv", spinnerBv.getSelectedItemPosition());
                                            editor.apply();
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
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }else {
            // Adapter Commune
            SpinerCommuneAdapter adapterCommune = new SpinerCommuneAdapter(ParametreActivity.this,
                    R.layout.dropdown_localisation,
                    R.id.textViewLabel,
                    R.id.textViewCode,
                    this.communes);
            this.spinnerCommune.setAdapter(adapterCommune);
            this.spinnerCommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Commune communeSelected = (Commune) spinnerCommune.getSelectedItem();
                    editor.putString("code_commune", communeSelected.getCode_commune());
                    editor.putString("label_commune", communeSelected.getLabel_commune());
                    editor.putInt("position_commune", spinnerCommune.getSelectedItemPosition());
                    editor.apply();
                    Toast toast = Toast.makeText(ParametreActivity.this, "COMMUNE: " + communeSelected.getLabel_commune(), Toast.LENGTH_LONG);
                    toast.show();
                    fokontany = DB.selectFokotanyFromCommune(communeSelected.getCode_commune());
                    // Adapter Fokontany
                    SpinerFokontanyAdapter adapterFokontany = new SpinerFokontanyAdapter(ParametreActivity.this,
                            R.layout.dropdown_localisation,
                            R.id.textViewLabel,
                            R.id.textViewCode,
                            fokontany);
                    spinnerFokontany.setAdapter(adapterFokontany);
                    spinnerFokontany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Fokontany fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                            editor.putString("code_fokontany", fokontanySelected.getCode_fokontany());
                            editor.putString("label_fokontany", fokontanySelected.getLabel_fokontany());
                            editor.putInt("position_fokontany", spinnerFokontany.getSelectedItemPosition());
                            editor.apply();
                            Toast toast = Toast.makeText(ParametreActivity.this, "FOKONTANY: " + fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                            toast.show();
                            cv = DB.selectCvFromFokontany(fokontanySelected.getCode_fokontany());
                            spinnerCv = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_cv);
                            spinnerCv.setEnabled(false);
                            // Adapter Cv
                            SpinerCvAdapter adapterCv = new SpinerCvAdapter(ParametreActivity.this,
                                    R.layout.dropdown_localisation,
                                    R.id.textViewLabel,
                                    R.id.textViewCode,
                                    cv);
                            spinnerCv.setAdapter(adapterCv);
                            spinnerCv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Cv cvSelected = (Cv) spinnerCv.getSelectedItem();
                                    Toast toast = Toast.makeText(ParametreActivity.this, "CV : " + cvSelected.getLabel_cv(), Toast.LENGTH_LONG);
                                    toast.show();
                                    bv = DB.selectBvFromCv(cvSelected.getCode_cv());
                                    spinnerBv = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_bv);
                                    spinnerBv.setEnabled(false);
                                    // Adapter Bv
                                    SpinerBvAdapter adapterBv = new SpinerBvAdapter(ParametreActivity.this,
                                            R.layout.dropdown_localisation,
                                            R.id.textViewLabel,
                                            R.id.textViewCode,
                                            bv);
                                    spinnerBv.setAdapter(adapterBv);
                                    spinnerBv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            Bv bvSelecter = (Bv)spinnerBv.getSelectedItem();
                                            editor.putString("code_bv", bvSelecter.getCode_bv());
                                            editor.putString("label_bv", bvSelecter.getLabel_bv());
                                            editor.putInt("position_bv", spinnerBv.getSelectedItemPosition());
                                            editor.apply();
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
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
        //Button previous
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MenuActivity.getInstance()!=null){
                    onBackPressed();
                }else{
                    Gson gson = new Gson();
                    String configTab = gson.toJson(tab);
                    String myjson = gson.toJson(user);

                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    i.putExtra("user", myjson);
                    i.putExtra("configTab", configTab);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


}
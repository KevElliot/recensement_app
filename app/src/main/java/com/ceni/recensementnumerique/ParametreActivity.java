package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.ceni.model.Fokontany;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;

import java.util.List;

public class ParametreActivity extends AppCompatActivity {
    private Spinner spinnerCommune,spinnerFokontany,spinnerCv,spinnerBv;
    private List<Commune> communes;
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private ImageView previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        MenuActivity.setParametre(true);
        Db_sqLite DB = new Db_sqLite(this);
        User user = LoginActivity.getUser();
        String codeDistrict = user.getCode_district();
        previous = this.findViewById(R.id.imageViewPrevious);
        this.communes = DB.selectCommuneFromDistrict(codeDistrict);
        this.spinnerCommune = (Spinner) this.findViewById(R.id.spinner_commune);


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
                Toast toast = Toast.makeText(ParametreActivity.this, "COMMUNE: "+communeSelected.getLabel_commune(), Toast.LENGTH_LONG);
                toast.show();
                fokontany = DB.selectFokotanyFromCommune(communeSelected.getCode_commune());
                spinnerFokontany = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_fokontany);
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
                        Toast toast = Toast.makeText(ParametreActivity.this, "FOKONTANY: "+fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                        toast.show();
                        cv = DB.selectCvFromFokontany(fokontanySelected.getCode_fokontany());
                        spinnerCv = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_cv);
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
                                Toast toast = Toast.makeText(ParametreActivity.this, "CV : "+cvSelected.getLabel_cv(), Toast.LENGTH_LONG);
                                toast.show();
                                bv = DB.selectBvFromCv(cvSelected.getCode_cv());
                                spinnerBv = (Spinner) ParametreActivity.this.findViewById(R.id.spinner_bv);
                                // Adapter Bv
                                SpinerBvAdapter adapterBv = new SpinerBvAdapter(ParametreActivity.this,
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
        //Button previous
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
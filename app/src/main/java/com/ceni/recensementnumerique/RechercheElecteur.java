package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ceni.adapter.ListElecteurAdapter;
import com.ceni.model.Electeur;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class RechercheElecteur extends AppCompatActivity {
    private CheckBox search1, search2, search3;
    private ImageView previous;
    private EditText inputRecherche;
    private Button btnRecherche;
    private ListView listElecteur;
    private String valeurSearch;
    private List<Electeur> elect;
    private ListElecteurAdapter listElecteurAdapter;
    private Db_sqLite DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_electeur);
        MenuActivity.setRecherche(true);
        search1 = this.findViewById(R.id.search1);
        search2 = this.findViewById(R.id.search2);
        search3 = this.findViewById(R.id.search3);
        previous = this.findViewById(R.id.imageViewPrevious);
        listElecteur = this.findViewById(R.id.listElecteur);
        inputRecherche = this.findViewById(R.id.inputRecherche);
        btnRecherche = this.findViewById(R.id.btnRecherche);
        DB = new Db_sqLite(this);
        valeurSearch = "cinElect";

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search1.isChecked()) {
                    valeurSearch = "cinElect";
                    inputRecherche.setText("");
                    search1.setChecked(true);
                    search2.setChecked(false);
                    search3.setChecked(false);
                } else {
                    valeurSearch = "nFiche";
                    inputRecherche.setText("");
                    search1.setChecked(false);
                    search2.setChecked(true);
                    search3.setChecked(false);
                }
            }
        });
        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search2.isChecked()) {
                    valeurSearch = "nFiche";
                    inputRecherche.setText("");
                    search1.setChecked(false);
                    search2.setChecked(true);
                    search3.setChecked(false);
                } else {
                    valeurSearch = "docreference";
                    inputRecherche.setText("");
                    search1.setChecked(false);
                    search2.setChecked(false);
                    search3.setChecked(true);
                }
            }
        });
        search3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search3.isChecked()) {
                    valeurSearch = "docreference";
                    inputRecherche.setText("");
                    search1.setChecked(false);
                    search2.setChecked(false);
                    search3.setChecked(true);
                } else {
                    valeurSearch = "cinElect";
                    inputRecherche.setText("");
                    search1.setChecked(true);
                    search2.setChecked(false);
                    search3.setChecked(false);
                }
            }
        });
        btnRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rech = inputRecherche.getText().toString();
                if(rech.length()!=0) {
                    elect = DB.Recherche(valeurSearch, rech);
                    if (elect.size() != 0) {
                        Log.d("Elect", "... " + elect.get(0).toString());
                        listElecteurAdapter = new ListElecteurAdapter(RechercheElecteur.this, elect);
                        listElecteur.setAdapter(listElecteurAdapter);
                        listElecteur.setClickable(true);
                        listElecteur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Electeur electeur = new Electeur();
                                electeur.setCode_bv(elect.get(position).getCode_bv());
                                electeur.setIdElect(elect.get(position).getIdElect());
                                electeur.setnFiche(elect.get(position).getnFiche());
                                electeur.setNom(elect.get(position).getNom());
                                electeur.setPrenom(elect.get(position).getPrenom());
                                electeur.setSexe(elect.get(position).getSexe());
                                electeur.setProfession(elect.get(position).getProfession());
                                electeur.setAdresse(elect.get(position).getAdresse());
                                electeur.setDateNaiss(elect.get(position).getDateNaiss());
                                electeur.setNevers(elect.get(position).getNevers());
                                electeur.setLieuNaiss(elect.get(position).getLieuNaiss());
                                electeur.setNomPere(elect.get(position).getNomPere());
                                electeur.setNomMere(elect.get(position).getNomMere());
                                electeur.setCinElect(elect.get(position).getCinElect());
                                electeur.setNserieCin(elect.get(position).getNserieCin());
                                electeur.setDateDeliv(elect.get(position).getDateDeliv());
                                electeur.setLieuDeliv(elect.get(position).getLieuDeliv());
                                electeur.setFicheElect(elect.get(position).getFicheElect());
                                electeur.setCinRecto(elect.get(position).getCinRecto());
                                electeur.setCinVerso(elect.get(position).getCinVerso());
                                electeur.setObservation(elect.get(position).getObservation());
                                electeur.setDocreference(elect.get(position).getDocreference());
                                electeur.setDateinscription(elect.get(position).getDateinscription());
                                Gson gson = new Gson();
                                String myjson = gson.toJson(electeur);
                                Log.i("Recherche activity", myjson);
                                Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
                                i.putExtra("electeur", myjson);
                                startActivity(i);
                            }
                        });
                    }else{
                        listElecteur.setAdapter(null);
                        Log.d("null","... Tsy misy mpifidy");
                        Toast toast = Toast.makeText(RechercheElecteur.this, "Tsy misy ilay mpifidy tediavina!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    inputRecherche.setError("Mila fenoina");
                }
            }
        });
        previous.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
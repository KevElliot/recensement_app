package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
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
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.User;
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
    private User user;
    static RechercheElecteur rechercheElecteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_electeur);
        rechercheElecteur = this;
        MenuActivity.setRecherche(true);
        search1 = this.findViewById(R.id.search1);
        search2 = this.findViewById(R.id.search2);
        search3 = this.findViewById(R.id.search3);
        previous = this.findViewById(R.id.imageViewPrevious);
        listElecteur = this.findViewById(R.id.listElecteur);
        inputRecherche = this.findViewById(R.id.inputRecherche);
        btnRecherche = this.findViewById(R.id.btnRecherche);
        DB = new Db_sqLite(this);
        this.user = MenuActivity.getCurrent_user();
        valeurSearch = "cinElect";
        inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search1.isChecked()) {
                    valeurSearch = "cinElect";
                    inputRecherche.setText("");
                    search1.setChecked(true);
                    inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    search2.setChecked(false);
                    search3.setChecked(false);
                } else {
                    valeurSearch = "nFiche";
                    inputRecherche.setText("");
                    search1.setChecked(false);
                    search2.setChecked(true);
                    inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
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
                    inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                    search1.setChecked(false);
                    search2.setChecked(true);
                    search3.setChecked(false);
                } else {
                    valeurSearch = "docreference";
                    inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
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
                    inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                    search1.setChecked(false);
                    search2.setChecked(false);
                    search3.setChecked(true);
                } else {
                    valeurSearch = "cinElect";
                    inputRecherche.setText("");
                    search1.setChecked(true);
                    inputRecherche.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    search2.setChecked(false);
                    search3.setChecked(false);
                }
            }
        });
//        btnRecherche.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String rech = inputRecherche.getText().toString();
//                if (rech.length() != 0) {
//                    Document doc = DB.selectDocumentbyNum(rech);
//                    Log.d("DOC", doc.toString());
//                    if (doc.getIdfdocreference()!=null) {
//                        elect = DB.Recherche(valeurSearch, doc.getIdfdocreference());
//                        if (elect.size() != 0) {
//                            Log.d("Elect", "... " + elect.get(0).toString());
//                            listElecteurAdapter = new ListElecteurAdapter(RechercheElecteur.this, elect);
//                            listElecteur.setAdapter(listElecteurAdapter);
//                            listElecteur.setClickable(true);
//                            listElecteur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                    Electeur electeur = new Electeur();
//                                    electeur.setCode_bv(elect.get(position).getCode_bv());
//                                    electeur.setIdElect(elect.get(position).getIdElect());
//                                    electeur.setnFiche(elect.get(position).getnFiche());
//                                    electeur.setNom(elect.get(position).getNom());
//                                    electeur.setPrenom(elect.get(position).getPrenom());
//                                    electeur.setSexe(elect.get(position).getSexe());
//                                    electeur.setProfession(elect.get(position).getProfession());
//                                    electeur.setAdresse(elect.get(position).getAdresse());
//                                    electeur.setDateNaiss(elect.get(position).getDateNaiss());
//                                    electeur.setNevers(elect.get(position).getNevers());
//                                    electeur.setLieuNaiss(elect.get(position).getLieuNaiss());
//                                    electeur.setNomPere(elect.get(position).getNomPere());
//                                    electeur.setNomMere(elect.get(position).getNomMere());
//                                    electeur.setCinElect(elect.get(position).getCinElect());
//                                    electeur.setNserieCin(elect.get(position).getNserieCin());
//                                    electeur.setDateDeliv(elect.get(position).getDateDeliv());
//                                    electeur.setLieuDeliv(elect.get(position).getLieuDeliv());
//                                    electeur.setFicheElect(elect.get(position).getFicheElect());
//                                    electeur.setCinRecto(elect.get(position).getCinRecto());
//                                    electeur.setCinVerso(elect.get(position).getCinVerso());
//                                    electeur.setObservation(elect.get(position).getObservation());
//                                    electeur.setDocreference(elect.get(position).getDocreference());
//                                    electeur.setDateinscription(elect.get(position).getDateinscription());
//                                    Gson gson = new Gson();
//                                    String myjson = gson.toJson(electeur);
//                                    Log.i("Recherche activity", myjson);
//                                    Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
//                                    i.putExtra("electeur", myjson);
//                                    startActivity(i);
//                                }
//                            });
//                        }
//                    } else {
//                        listElecteur.setAdapter(null);
//                        Log.d("null", "... Tsy misy mpifidy");
//                        Toast toast = Toast.makeText(RechercheElecteur.this, "Tsy misy ilay mpifidy tadiavina!", Toast.LENGTH_LONG);
//                        toast.show();
//                    }
//
//                } else {
//                    inputRecherche.setError("Mila fenoina");
//                }
//            }
//        });

        btnRecherche.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String rech = inputRecherche.getText().toString();
                if (rech.length() != 0) {
                    if (valeurSearch == "docreference") {
                        Document doc = DB.selectDocumentbyNum(rech);
                        Log.d("RECHERCHE","-----------  "+doc.toString());
                        if (doc.getIdfdocreference() != null) {
                            elect = DB.Recherche("docreference", doc.getIdfdocreference(),user.getCode_district());
                            if (elect.size() != 0) {
                                Log.d("Elect", "..elect.get(0).toString(). " + elect.get(0).toString());
                                listElecteurAdapter = new ListElecteurAdapter(RechercheElecteur.this, elect);
                                listElecteur.setAdapter(listElecteurAdapter);
                                listElecteur.setClickable(true);
                                listElecteur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        Electeur electeur = new Electeur();
                                        electeur.setIdElect(elect.get(position).getIdElect());
                                        electeur.setCode_district(elect.get(position).getCode_district());
                                        electeur.setCode_bv(elect.get(position).getCode_bv());
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
                                        electeur.setNum_userinfo(elect.get(position).getNum_userinfo());
                                        electeur.setDateinscription(elect.get(position).getDateinscription());
                                        Gson gson = new Gson();
                                        String myjson = gson.toJson(electeur);
                                        Log.i("Recherche activity", myjson);
                                        Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
                                        i.putExtra("electeur", myjson);
                                        startActivity(i);
                                    }
                                });

                            } else {
                                listElecteur.setAdapter(null);
                                Log.d("null", "... Tsy misy mpifidy");
                                Toast toast = Toast.makeText(RechercheElecteur.this, "Tsy misy ilay mpifidy tadiavina!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else {
                            listElecteur.setAdapter(null);
                            Toast toast = Toast.makeText(RechercheElecteur.this, "Tsy misy ilay karine!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        elect = DB.Recherche(valeurSearch, rech,user.getCode_district());
                        if (elect.size() != 0) {
                            Log.d("Elect", "... " + elect.get(0).toString());
                            listElecteurAdapter = new ListElecteurAdapter(RechercheElecteur.this, elect);
                            listElecteur.setAdapter(listElecteurAdapter);
                            listElecteur.setClickable(true);
                            listElecteur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    Electeur electeur = new Electeur();
                                    electeur.setIdElect(elect.get(position).getIdElect());
                                    electeur.setCode_district(elect.get(position).getCode_district());
                                    electeur.setCode_bv(elect.get(position).getCode_bv());
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
                                    electeur.setNum_userinfo(elect.get(position).getNum_userinfo());
                                    electeur.setDateinscription(elect.get(position).getDateinscription());
                                    Gson gson = new Gson();
                                    String myjson = gson.toJson(electeur);
                                    Log.i("Recherche activity", myjson);
                                    Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
                                    i.putExtra("electeur", myjson);
                                    startActivity(i);
                                }
                            });
                        } else {
                            listElecteur.setAdapter(null);
                            Log.d("null", "... Tsy misy mpifidy");
                            Toast toast = Toast.makeText(RechercheElecteur.this, "Tsy misy ilay mpifidy tadiavina!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
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

    public static RechercheElecteur getInstance() {
        return rechercheElecteur;
    }
}
package com.ceni.recensementnumerique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.adapter.SpinerBvAdapter;
import com.ceni.adapter.SpinerCvAdapter;
import com.ceni.adapter.SpinerFokontanyAdapter;
import com.ceni.adapter.SpinnerDocumentAdapter;
import com.ceni.model.Bv;
import com.ceni.model.Cv;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Fokontany;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
// import java.util.Base64;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NewElecteurActivity extends AppCompatActivity {
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private Spinner spinnerFokontany, spinnerCv, spinnerBv, spinnerDocument;
    private List<Document> document;
    private Button mPickDateButton, buttonRecto, buttonVerso, buttonImage, enregistrer;
    private CheckBox sexeHomme, sexeFemme, feuPere, feuMere, nevers;
    private String sexe;
    private EditText nFiche, nom, prenom, lieuNaiss, profession, adresse, nomMere, nomPere, datedeNaissance, editNevers;
    private EditText cin, nserie, nserie2, lieuCin, dateCin, datederecensement;
    private TextView infoCarnet;
    private int countFormValide;
    private boolean isMemeFiche, isSamePers, isNevers, feuMereSelected, feuPereSelected, fichefull, isDateNaissValide, isDateDerecense;
    private ImageView recto, verso, imageView;
    private String msg, user, format, imageRecto, imageVerso, dataFicheElect;
    private String currentPhotoPath_fiche_recensement, currentPhotoPath_cin_recto_recensement, currentPhotoPath_cin_verso_recensement;
    private Fokontany fokontanySelected;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_electeur);
        MenuActivity.setNewElect(true);
        Db_sqLite DB = new Db_sqLite(this);
        sexe = "Masculin";
        msg = "Iangaviana ianao mba ameno ireo banga na ny diso azafady.";
        sexeHomme = findViewById(R.id.sexeHomme);
        sexeFemme = findViewById(R.id.sexeFemme);
        nFiche = findViewById(R.id.editTextnFiche);
        infoCarnet = findViewById(R.id.textView31);
        nom = findViewById(R.id.editTextNom);
        prenom = findViewById(R.id.editTextPrenom);
        lieuNaiss = findViewById(R.id.editTextLieuNaissance);
        profession = findViewById(R.id.editTextProfession);
        adresse = findViewById(R.id.editTextAdresse);
        nomMere = findViewById(R.id.editNomMere);
        nomPere = findViewById(R.id.editTextNomPere);
        feuPere = findViewById(R.id.feuPere);
        feuMere = findViewById(R.id.feuMere);
        datedeNaissance = findViewById(R.id.datedeNaissance);
        nevers = findViewById(R.id.nevers);
        editNevers = findViewById(R.id.editTextNevers);
        cin = findViewById(R.id.editTextCin);
        nserie = findViewById(R.id.editTextNserie);
        nserie2 = findViewById(R.id.editTextNserie2);
        lieuCin = findViewById(R.id.editTextLieuCIN);
        dateCin = findViewById(R.id.dateCIN);
        buttonRecto = this.findViewById(R.id.button_image_recto);
        recto = this.findViewById(R.id.cin_recto);
        buttonVerso = this.findViewById(R.id.button_image_verso);
        verso = this.findViewById(R.id.cin_verso);
        buttonImage = (Button) this.findViewById(R.id.button_fiche);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        datederecensement = this.findViewById(R.id.datederecensement);
        enregistrer = this.findViewById(R.id.enregistrer);
        spinnerDocument = this.findViewById(R.id.spinner_document);
        user = getIntent().getStringExtra("user");
        isNevers = false;
        fichefull = false;
        feuMereSelected = false;
        feuPereSelected = false;
        isDateNaissValide = false;
        isDateDerecense = false;
        final String[] docReference = {""};
        final String[] idFdocReference = {""};
        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int moisNow = Calendar.getInstance().get(Calendar.MONTH);
        int dayNow = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int anneeMajor = anneeNow - 18;
        int anneeDead = anneeNow - 150;
        countFormValide = 0;
        SharedPreferences params_localisation = this.getSharedPreferences("params_localisation", Context.MODE_PRIVATE);
        //if tsisy dia paramettre
        // FOKONTANY
        String commune_pref = params_localisation.getString("code_commune", "");
        fokontany = DB.selectFokotanyFromCommune(commune_pref);
        spinnerFokontany = (Spinner) NewElecteurActivity.this.findViewById(R.id.spinner_fokontany);
        SpinerFokontanyAdapter adapterFokontany = new SpinerFokontanyAdapter(NewElecteurActivity.this,
                R.layout.dropdown_localisation,
                R.id.textViewLabel,
                R.id.textViewCode,
                fokontany);
        spinnerFokontany.setAdapter(adapterFokontany);
        int fokontany_pref = params_localisation.getInt("position_fokontany", 0);
        spinnerFokontany.setSelection(fokontany_pref);
        spinnerFokontany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                document = DB.selectDocument(fokontanySelected.getCode_fokontany());
                //DOCUMENT
                // Adapter Document
                SpinnerDocumentAdapter adapterDocument = new SpinnerDocumentAdapter(NewElecteurActivity.this,
                        R.layout.dropdown_document,
                        R.id.numdocreference,
                        document);
                spinnerDocument.setAdapter(adapterDocument);
                spinnerDocument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Document docSelected = (Document) spinnerDocument.getSelectedItem();
                        docReference[0] = docSelected.getNumdocreference().toString();
                        idFdocReference[0] = docSelected.getIdfdocreference();
                        if (docSelected.getNbfeuillet() == 25) {
                            fichefull = true;
                            infoCarnet.setTextColor(Color.RED);
                            infoCarnet.setText("Karine efa feno 25 takelaka voasoratra");
                        } else {
                            infoCarnet.setTextColor(Color.BLACK);
                            infoCarnet.setText("Karine voasoratra: " + docSelected.getNbfeuillet() + "/25");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                Toast toast = Toast.makeText(NewElecteurActivity.this, "FOKONTANY: " + fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                toast.show();
                cv = DB.selectCvFromFokontany(fokontanySelected.getCode_fokontany());
                spinnerCv = (Spinner) NewElecteurActivity.this.findViewById(R.id.spinner_cv);
                spinnerCv.setEnabled(false);
                // Adapter Cv
                SpinerCvAdapter adapterCv = new SpinerCvAdapter(NewElecteurActivity.this,
                        R.layout.dropdown_localisation,
                        R.id.textViewLabel,
                        R.id.textViewCode,
                        cv);
                spinnerCv.setAdapter(adapterCv);
                spinnerCv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Cv cvSelected = (Cv) spinnerCv.getSelectedItem();
                        Toast toast = Toast.makeText(NewElecteurActivity.this, "CV : " + cvSelected.getLabel_cv(), Toast.LENGTH_LONG);
                        toast.show();
                        bv = DB.selectBvFromCv(cvSelected.getCode_cv());
                        spinnerBv = (Spinner) NewElecteurActivity.this.findViewById(R.id.spinner_bv);
                        spinnerBv.setEnabled(false);
                        // Adapter Bv
                        SpinerBvAdapter adapterBv = new SpinerBvAdapter(NewElecteurActivity.this,
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


        nevers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nevers.isChecked()) {
                    isNevers = true;
                    datedeNaissance.setVisibility(View.GONE);
                    editNevers.setVisibility(View.VISIBLE);
                } else {
                    isNevers = false;
                    datedeNaissance.setVisibility(View.VISIBLE);
                    editNevers.setVisibility(View.GONE);
                }
            }
        });
        feuPere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String np = nomPere.getText().toString();
                if (feuPere.isChecked()) {
                    nomPere.setText(np + "(Feu)");
                } else {
                    String newnp = np.replace("(Feu)", "");
                    nomPere.setText(newnp);
                }
            }
        });
        feuMere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = nomMere.getText().toString();
                if (feuMere.isChecked()) {
                    nomMere.setText(nm + "(Feue)");
                } else {
                    String newnm = nm.replace("(Feue)", "");
                    nomMere.setText(newnm);
                }
            }
        });
        sexeHomme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexeHomme.isChecked()) {
                    sexe = "Masculin";
                    sexeHomme.setChecked(true);
                    sexeFemme.setChecked(false);
                } else {
                    sexe = "Feminin";
                    sexeHomme.setChecked(false);
                    sexeFemme.setChecked(true);
                }
            }
        });
        sexeFemme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexeFemme.isChecked()) {
                    sexe = "Masculin";
                    sexeFemme.setChecked(true);
                    sexeHomme.setChecked(false);
                } else {
                    sexe = "Feminin";
                    sexeFemme.setChecked(false);
                    sexeHomme.setChecked(true);
                }
            }
        });
        cin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int size = cin.getText().toString().trim().length();
                if (size < 12) {
                    cin.setError("Tokony ho 12isa");
                } else {
                    cin.setError(null);
                }
            }
        });


        this.buttonRecto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "recto";
                verifyPermissions();
            }
        });
        this.buttonVerso.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "verso";
                verifyPermissions();
            }
        });
        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "feuillet";
                verifyPermissions();
            }
        });

        nFiche.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isMemeFiche = DB.isMemeFiche(nFiche.getText().toString());
                if (isMemeFiche) {
                    nFiche.setError("Takelaka efa voasoratra!");
                }
            }
        });

        datedeNaissance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String dateDead = "10/06/" + anneeDead;
                    String dateLimite = "10/06/2005";
                    String nomElect = nom.getText().toString();
                    String prenomElect = prenom.getText().toString();
                    String naiss = datedeNaissance.getText().toString();
                    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                    Date dateNaiss = format.parse(naiss);
                    Boolean isDateValide = checkDate(naiss);
                    if (isDateValide) {
//                        Date dead = format.parse(dateDead);
//                        Date limite = format.parse(dateLimite);
//                        if (dead.getTime() < dateNaiss.getTime() && dateNaiss.getTime() < limite.getTime()) {
//                            isSamePers = DB.isSamePerson(nomElect, prenomElect, naiss);
//                            isDateNaissValide = true;
//                            if (isSamePers) {
//                                datedeNaissance.setError("mpifidy efa voasoratra!");
//                                prenom.setError("mpifidy efa voasoratra!");
//                                nom.setError("mpifidy efa voasoratra!");
//                            }
//                        } else {
//                            datedeNaissance.setError("tsy tafiditra ny date");
//                        }
                        datedeNaissance.setError("Daty valide");
                    }else {
                        datedeNaissance.setError("Daty tsy valide");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        datederecensement.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String deb = "01/10/2022";
                String daterecens = datederecensement.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                try {
                    Date dateRecens = format.parse(daterecens);
                    Date datedeb = format.parse(deb);
                    Date dateNow = Calendar.getInstance().getTime();
                    if (datedeb.getTime() < dateRecens.getTime() && dateRecens.getTime() < dateNow.getTime()) {
                        isDateDerecense = true;
                    } else {
                        datederecensement.setError("Tsy ao anatiny daty");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        this.enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countFormValide = 0;
                Electeur electeur = new Electeur();
                if (nom.getText().toString().length() != 0) {
                    electeur.setNom(nom.getText().toString());
                    countFormValide += 1;
                } else {
                    nom.setError("Mila fenoina");
                }
                if (prenom.getText().toString().length() != 0) {
                    countFormValide += 1;
                    electeur.setPrenom(prenom.getText().toString());
                } else {
                    electeur.setPrenom("");
                }
                if (lieuNaiss.getText().toString().length() != 0) {
                    electeur.setLieuNaiss(lieuNaiss.getText().toString());
                    countFormValide += 1;
                } else {
                    lieuNaiss.setError("Mila fenoina");
                }
                if (profession.getText().toString().length() != 0) {
                    electeur.setProfession(profession.getText().toString());
                    countFormValide += 1;
                } else {
                    profession.setError("Mila fenoina");
                }
                if (adresse.getText().toString().length() != 0) {
                    electeur.setAdresse(adresse.getText().toString());
                    countFormValide += 1;
                } else {
                    adresse.setError("Mila fenoina");
                }
                if (nomPere.getText().toString().length() != 0) {
                    electeur.setNomPere(nomPere.getText().toString());
                    countFormValide += 1;
                } else {
                    nomPere.setError("Mila fenoina");
                }
                if (nomMere.getText().toString().length() != 0) {
                    electeur.setNomMere(nomMere.getText().toString());
                    countFormValide += 1;
                } else {
                    nomMere.setError("Mila fenoina");
                }
                if (sexe == "Masculin" || sexe == "Feminin") {
                    electeur.setSexe(sexe);
                    countFormValide += 1;
                }
                if (isNevers) {
                    int anneeNevers = Integer.parseInt(editNevers.getText().toString());
                    if (editNevers.getText().toString().length() == 4) {
                        if (anneeNevers < anneeMajor && anneeNevers > anneeDead) {
                            countFormValide += 1;
                            electeur.setNevers(editNevers.getText().toString());
                            electeur.setDateNaiss("");
                        } else {
                            editNevers.setError("Tsy ao anatiny taona");
                        }
                    } else {
                        editNevers.setError("Diso");
                    }
                } else {
                    if (datedeNaissance.getText().length() != 0 && isDateNaissValide) {
                        countFormValide += 1;
                        electeur.setDateNaiss(datedeNaissance.getText().toString());
                        electeur.setNevers("");
                    } else {
                        datedeNaissance.setError("Mila apetraka ny daty nahaterahana");
                    }
                }

                if (nFiche.getText().toString().length() == 8) {
                    electeur.setnFiche(nFiche.getText().toString());
                    countFormValide += 1;
                } else {
                    nFiche.setError("Misy diso");
                }
                if (docReference[0] != "") {
                    electeur.setDocreference(idFdocReference[0]);
                    countFormValide += 1;
                } else {
                    Toast toast = Toast.makeText(NewElecteurActivity.this, "Misafidiana Karine!", Toast.LENGTH_LONG);
                    toast.show();
                }
                if (cin.getText().toString().length() == 12) {
                    electeur.setCinElect(cin.getText().toString());
                    countFormValide += 1;
                } else {
                    cin.setError("Diso");
                }
                if (nserie.getText().toString().length() == 7) {
                    if(nserie2.getText().toString().length() == 0){
                        String serial = nserie.getText().toString();
                        electeur.setNserieCin(serial);
                        countFormValide += 1;
                    } else{
                        String serial = nserie.getText().toString() + "/" + nserie2.getText().toString();
                        electeur.setNserieCin(serial);
                        countFormValide += 1;
                    }
                } else {
                    nserie.setError("Diso");
                }
                if (lieuCin.getText().toString().length() != 0) {
                    electeur.setLieuDeliv(lieuCin.getText().toString());
                    countFormValide += 1;
                } else {
                    lieuCin.setError("Mila fenoina");
                }
                if (dateCin.getText().length() != 0) {
                    electeur.setDateDeliv(dateCin.getText().toString());
                    countFormValide += 1;
                } else {
                    dateCin.setError("Mila apetraka ny daty nahazahona ny karatra");
                }
                if (datederecensement.getText().length() != 0 && isDateDerecense) {
                    electeur.setDateinscription(datederecensement.getText().toString());
                    countFormValide += 1;
                } else {
                    datederecensement.setError("Mila fenoina");
                }
                if (imageRecto != null) {
                    electeur.setCinRecto(imageRecto);
                }
                if (imageVerso != null) {
                    electeur.setCinVerso(imageVerso);
                }
                if (dataFicheElect != null) {
                    countFormValide += 1;
                    electeur.setFicheElect(dataFicheElect);
                } else {
                    new AlertDialog.Builder(NewElecteurActivity.this)
                            .setTitle("Fahadisoana?")
                            .setMessage("Iangaviana ianao mba haka sarin'ny takelaka!.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // tsisy
                                }
                            }).show();
                }

                if (countFormValide != 17) {
                    Log.d("COUNT", "" + countFormValide);
                    new AlertDialog.Builder(NewElecteurActivity.this)
                            .setTitle("Fahadisoana")
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // tsisy
                                }
                            }).show();

                } else {
                    Log.d("NEW ELECT", electeur.toString());
                    if (!fichefull) {
                        if (!isSamePers) {
                            if (!isMemeFiche) {
                                Bv bvSelected = (Bv) spinnerBv.getSelectedItem();
                                electeur.setCode_bv(bvSelected.getCode_bv());
                                boolean result = DB.insertElecteurData(electeur.getCode_bv(), electeur.getnFiche(), electeur.getNom(), electeur.getPrenom(), electeur.getSexe(), electeur.getProfession(), electeur.getAdresse(), electeur.getDateNaiss(), electeur.getNevers(), electeur.getLieuNaiss(), electeur.getNomPere(), electeur.getNomMere(), electeur.getCinElect(), electeur.getNserieCin(), electeur.getDateDeliv(), electeur.getLieuDeliv(), electeur.getFicheElect(), electeur.getCinRecto(), electeur.getCinVerso(), electeur.getObservation(), electeur.getDocreference(), electeur.getDateinscription());
                                if (result) {
                                    Document doc = DB.selectDocumentbyid(electeur.getDocreference());
                                    Gson gson = new Gson();
                                    User us = gson.fromJson(user, User.class);
                                    DB.counterStat(doc, us, 1);
                                    Toast toast = Toast.makeText(NewElecteurActivity.this, "Electeur enregistré!", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent i = new Intent(getApplicationContext(), NewElecteurActivity.class);
                                    i.putExtra("user", user);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                new AlertDialog.Builder(NewElecteurActivity.this)
                                        .setTitle("Fahadisoana")
                                        .setMessage("Takelaka efa voasoratra!")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // tsisy
                                            }
                                        }).show();
                            }
                        } else {
                            new AlertDialog.Builder(NewElecteurActivity.this)
                                    .setTitle("Fahadisoana")
                                    .setMessage("Mpifidy efa voasoratra!")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // tsisy
                                        }
                                    }).show();
                        }
                    } else {
                        new AlertDialog.Builder(NewElecteurActivity.this)
                                .setTitle("Fahadisoana")
                                .setMessage("Karine efa feno! Manamboara vaovao!")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // tsisy
                                    }
                                }).show();
                    }
                }
            }
        });


    }

    private boolean checkDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        Date d = new Date();
        try {
            d = format.parse(date);
            Log.d("check_date",""+d+" date valide");
        } catch (ParseException e) {
            Log.d("check_date",""+d+" date non valide");
            return false;
        }
        // Renvoie true si la date est valide
        return true;
    }

    private void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    ////////////////////////////////// BEGIN MODIFICATION //////////////////////////////////

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    // private ImageView recto, verso, imageView;
    // private String msg, user, format, imageRecto, imageVerso, dataFicheElect;
    // String currentPhotoPath_fiche_recensement, currentPhotoPath_cin_recto_recensement, currentPhotoPath_cin_verso_recensement;

    private void verifyPermissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){

            switch(format)
            {
                case "recto":
                    dispatchTakePictureIntent("recto");
                    break;
                case "verso":
                    dispatchTakePictureIntent("verso");
                    break;
                case "feuillet":
                    dispatchTakePictureIntent("feuillet");
                    break;
                default:
                    System.out.println("default");
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    CAMERA_PERM_CODE);
        }
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            Log.d("ROOT", root.getAbsolutePath());
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){

                if (Objects.equals(this.format, "feuillet")) {
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_fiche_recensement);
                    // imageView.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image fiche recensement is " + Uri.fromFile(f_fiche));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    this.imageView.setImageBitmap(bitmap);

                    dataFicheElect = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    this.generateNoteOnSD(this.getApplicationContext(), "base64", dataFicheElect);

                    if(f_fiche.exists()){
                        if (f_fiche.delete()){
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else{
                            System.out.println("file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }

                    // mediaScanIntent.setData(contentUri);
                    // this.sendBroadcast(mediaScanIntent);
                } else if (Objects.equals(this.format, "recto")) {
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_cin_recto_recensement);
                    // recto.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image recto recensement is " + Uri.fromFile(f_fiche));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    this.recto.setImageBitmap(bitmap);

                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 RECTO : ", imageRecto.trim());

                    if(f_fiche.exists()){
                        if (f_fiche.delete()){
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else{
                            System.out.println("file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }

                    //mediaScanIntent.setData(contentUri);
                    // this.sendBroadcast(mediaScanIntent);
                } else if (Objects.equals(this.format, "verso")) {
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_cin_verso_recensement);
                    // verso.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image verso recensement is " + Uri.fromFile(f_fiche));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    this.verso.setImageBitmap(bitmap);

                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 VERSO : ", imageVerso.trim());

                    if(f_fiche.exists()){
                        if (f_fiche.delete()){
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else{
                            System.out.println("file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }

                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                }

                /*

                if (currentPhotoPath_fiche_recensement == null || currentPhotoPath_fiche_recensement.isEmpty() || currentPhotoPath_fiche_recensement.trim().isEmpty())
                    System.out.println("String is null, empty or blank.");
                else{
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_fiche_recensement);
                    imageView.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image fiche recensement is " + Uri.fromFile(f_fiche));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    dataFicheElect = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 FEUILLET : ", dataFicheElect);

                    if(f_fiche.exists()){
                        if (f_fiche.delete()){
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else{
                            System.out.println("file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }

                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                }

                if (currentPhotoPath_cin_recto_recensement == null || currentPhotoPath_cin_recto_recensement.isEmpty() || currentPhotoPath_cin_recto_recensement.trim().isEmpty())
                    System.out.println("String is null, empty or blank.");
                else{
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_cin_recto_recensement);
                    recto.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image recto recensement is " + Uri.fromFile(f_fiche));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 RECTO : ", imageRecto);

                    if(f_fiche.exists()){
                        if (f_fiche.delete()){
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else{
                            System.out.println("file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }

                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                }

                if (currentPhotoPath_cin_verso_recensement == null || currentPhotoPath_cin_verso_recensement.isEmpty() || currentPhotoPath_cin_verso_recensement.trim().isEmpty())
                    System.out.println("String is null, empty or blank.");
                else{
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_cin_verso_recensement);
                    verso.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image verso recensement is " + Uri.fromFile(f_fiche));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 VERSO : ", imageVerso);

                    if(f_fiche.exists()){
                        if (f_fiche.delete()){
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else{
                            System.out.println("file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }

                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                }


                 */


            }

        }
    }

    private File createImageFile(String name_file) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".JPEG",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        switch(name_file)
        {
            case "feuillet":
                currentPhotoPath_fiche_recensement = image.getAbsolutePath();
                break;
            case "recto":
                currentPhotoPath_cin_recto_recensement = image.getAbsolutePath();
                break;
            case "verso":
                currentPhotoPath_cin_verso_recensement = image.getAbsolutePath();
                break;
            default:
                System.out.println("default");
        }
        return image;
    }

    private void dispatchTakePictureIntent(String name_file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(name_file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    ////////////////////////////////// END MODIFICATION //////////////////////////////////

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");

//                ByteBuffer buffer = ByteBuffer.allocate(bp.getRowBytes() * bp.getHeight());
//                bp.copyPixelsToBuffer(buffer);
//                byte[] byteArray = buffer.array();

                //Encode Image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                if (this.format == "recto") {
                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("Recto", imageRecto);
                    this.recto.setImageBitmap(bp);
                } else if (this.format == "verso") {
                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("Verso", imageVerso);
                    this.verso.setImageBitmap(bp);
                } else if (this.format == "feuillet") {
                    dataFicheElect = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("feuillet", dataFicheElect);
                    this.imageView.setImageBitmap(bp);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
     */


}
package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.ceni.adapter.SpinerCommuneAdapter;
import com.ceni.adapter.SpinnerDocumentAdapter;
import com.ceni.model.Commune;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.service.Db_sqLite;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class InscriptionActivity extends AppCompatActivity {
    static InscriptionActivity inscriptionActivity;
    private List<Document> document;
    private Spinner spinnerDocument;
    private Button mPickDateButton;
    private TextView mShowSelectedDateText;
    private ImageView next;
    private ImageView previous;
    private CheckBox sexeHomme, sexeFemme, feuPere, feuMere, nevers;
    private String sexe, dateNaiss;
    private EditText nFiche, nom, prenom, lieuNaiss, profession, adresse, nomMere, nomPere, editNevers;
    private int countFormValide;
    private boolean isMemeFiche, isNevers, feuMereSelected, feuPereSelected;
    private String msg,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inscriptionActivity = this;
        setContentView(R.layout.activity_inscription);
        Db_sqLite DB = new Db_sqLite(this);
        sexe = "Homme";
        msg = "Iangaviana enao mba ameno ireo banga na ny diso azafady.";
        sexeHomme = findViewById(R.id.sexeHomme);
        sexeFemme = findViewById(R.id.sexeFemme);
        previous = findViewById(R.id.imageViewPrevious);
        next = findViewById(R.id.imageViewNext);
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.selected_Date);
        nFiche = findViewById(R.id.editTextnFiche);
        nom = findViewById(R.id.editTextNom);
        prenom = findViewById(R.id.editTextPrenom);
        lieuNaiss = findViewById(R.id.editTextLieuNaissance);
        profession = findViewById(R.id.editTextProfession);
        adresse = findViewById(R.id.editTextAdresse);
        nomMere = findViewById(R.id.editNomMere);
        nomPere = findViewById(R.id.editTextNomPere);
        feuPere = findViewById(R.id.feuPere);
        feuMere = findViewById(R.id.feuMere);
        nevers = findViewById(R.id.nevers);
        editNevers = findViewById(R.id.editTextNevers);
        user = getIntent().getStringExtra("user");
        this.document = DB.selectDocument();
        this.spinnerDocument = this.findViewById(R.id.spinner_document);
        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow - 18;
        int anneeDead = anneeNow - 150;
        isNevers = false;
        feuMereSelected = false;
        feuPereSelected = false;
        final String[] docReference = {""};

        // Adapter Document
        SpinnerDocumentAdapter adapterDocument = new SpinnerDocumentAdapter(InscriptionActivity.this,
                R.layout.dropdown_document,
                R.id.textViewLabel,
                this.document);
        this.spinnerDocument.setAdapter(adapterDocument);
        this.spinnerDocument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Document docSelected = (Document) spinnerDocument.getSelectedItem();
                docReference[0] = docSelected.getDocreference().toString();
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
                    mShowSelectedDateText.setVisibility(View.GONE);
                    mPickDateButton.setVisibility(View.GONE);
                    editNevers.setVisibility(View.VISIBLE);
                } else {
                    isNevers = false;
                    editNevers.setVisibility(View.GONE);
                    mShowSelectedDateText.setVisibility(View.VISIBLE);
                    mPickDateButton.setVisibility(View.VISIBLE);
                }
            }
        });

        feuPere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String np = nomPere.getText().toString();
                if (feuPere.isChecked()) {
                    nomPere.setText(np+"(Feu)");
                } else {
                    String newnp = np.replace("(Feu)","");
                    nomPere.setText(newnp);
                }
            }
        });
        feuMere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = nomMere.getText().toString();
                if (feuMere.isChecked()) {
                    nomMere.setText(nm+"(Feu)");
                } else {
                    String newnm = nm.replace("(Feu)","");
                    nomMere.setText(newnm);
                }
            }
        });
        sexeHomme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexeHomme.isChecked()) {
                    sexe = "Homme";
                    sexeHomme.setChecked(true);
                    sexeFemme.setChecked(false);
                } else {
                    sexe = "Femme";
                    sexeHomme.setChecked(false);
                    sexeFemme.setChecked(true);
                }
            }
        });
        sexeFemme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexeFemme.isChecked()) {
                    sexe = "Femme";
                    sexeFemme.setChecked(true);
                    sexeHomme.setChecked(false);
                } else {
                    sexe = "Homme";
                    sexeFemme.setChecked(false);
                    sexeHomme.setChecked(true);
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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countFormValide = 0;
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                if (nFiche.getText().toString().length() == 12) {
                    isMemeFiche = DB.isMemeFiche(nFiche.getText().toString());
                    if (isMemeFiche) {
                        msg = "Takelaka efa voasoratra!";
                    } else {
                        electeur.setnFiche(nFiche.getText().toString());
                        countFormValide += 1;
                    }
                } else {
                    nFiche.setError("Misy diso");
                }
                if (nom.getText().toString().length() != 0) {
                    electeur.setNom(nom.getText().toString());
                    countFormValide += 1;
                } else {
                    nom.setError("Mila fenoina");
                }
                if (prenom.getText().toString().length() != 0) {
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
                if (sexe == "Homme" || sexe == "Femme") {
                    electeur.setSexe(sexe);
                    countFormValide += 1;
                }
                if (isNevers) {
                    int anneeNevers = Integer.parseInt(editNevers.getText().toString());
                    if (editNevers.getText().toString().length() == 4) {
                        if(anneeNevers<anneeMajor && anneeNevers>anneeDead){
                            countFormValide += 1;
                            electeur.setNevers(editNevers.getText().toString());
                            electeur.setDateNaiss("");
                        }else{
                            editNevers.setError("Tsy ao anatiny taona");
                        }
                    } else {
                        editNevers.setError("Diso");
                    }
                } else {
                    if (dateNaiss !=null) {
                        countFormValide += 1;
                        electeur.setDateNaiss(dateNaiss);
                        electeur.setNevers("");
                    } else {
                        mShowSelectedDateText.setTextColor(Color.RED);
                        mShowSelectedDateText.setText("Mila apetraka ny daty nahaterahana");
                    }
                }
                if(docReference[0]!=""){
                    electeur.setDocreference(docReference[0]);
                    countFormValide+=1;
                }else{
                    Toast toast = Toast.makeText(InscriptionActivity.this, "Selectionner un carnet!", Toast.LENGTH_LONG);
                    toast.show();
                }
                if (countFormValide != 10) {
                    new AlertDialog.Builder(InscriptionActivity.this)
                            .setTitle("Fahadisoana?")
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // tsisy
                                }
                            }).show();

                } else {
                    String myJson = gson.toJson(electeur);
                    Log.d("INSCRIPTION ACTIVITY", "JSON:  " + myJson);
                    Intent i = new Intent(getApplicationContext(), Inscription2Activity.class);
                    i.putExtra("user", user);
                    i.putExtra("newElect", myJson);
                    startActivity(i);
                }
            }
        });

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        calendar.set(Calendar.YEAR, anneeMajor);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        Long anneeFin = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, anneeDead);
        Long anneeStart = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setStart(anneeStart);
        constraintBuilder.setEnd(anneeFin);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setSelection(anneeFin);
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());
        materialDateBuilder.setTitleText("Daty nahaterahana:");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickDateButton.setEnabled(false);
                materialDatePicker.show(getSupportFragmentManager(), "DATE DE NAISSANCE");
            }
        });
        materialDatePicker.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mPickDateButton.setEnabled(true);
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = format.format(calendar.getTime());
                mShowSelectedDateText.setTextColor(Color.WHITE);
                mPickDateButton.setEnabled(true);
                dateNaiss = formattedDate;
                mShowSelectedDateText.setText("Daty nahaterahana: " + formattedDate);
            }
        });
    }

    public static InscriptionActivity getInstance() {
        return inscriptionActivity;
    }
}
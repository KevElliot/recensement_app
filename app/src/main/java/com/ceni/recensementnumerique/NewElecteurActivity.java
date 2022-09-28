package com.ceni.recensementnumerique;

import static android.provider.Settings.System.DATE_FORMAT;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
// import java.util.Base64;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewElecteurActivity extends AppCompatActivity {
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private Spinner spinnerFokontany, spinnerCv, spinnerBv, spinnerDocument;
    private List<Document> document;
    private Button mPickDateButton, buttonRecto, buttonVerso, buttonImage, enregistrer,datecin,datederecensement;
    private ImageView recto, verso, imageView;
    private CheckBox sexeHomme, sexeFemme, feuPere, feuMere, nevers;
    private String sexe;
    private EditText nFiche, nom, prenom, lieuNaiss, profession, adresse, nomMere, nomPere, editNevers;
    private EditText cin, nserie, nserie2, lieuCin;
    private TextView infoCarnet,mShowSelectedDateText,selectedDateCin,selectedDateRecensement;
    private int countFormValide;
    private boolean isMemeFiche, isSamePers, isNevers, feuMereSelected, feuPereSelected, fichefull;
    private String msg, user, format, imageRecto, imageVerso, dataFicheElect,dateNaiss,dateCinElect,daterecensement;
    private Fokontany fokontanySelected;

    private Pattern pattern;
    private Matcher matcher;

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
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.selected_Date);

//        datedeNaissance = findViewById(R.id.datedeNaissance);
        nevers = findViewById(R.id.nevers);
        editNevers = findViewById(R.id.editTextNevers);
        cin = findViewById(R.id.editTextCin);
        nserie = findViewById(R.id.editTextNserie);
        nserie2 = findViewById(R.id.editTextNserie2);
        lieuCin = findViewById(R.id.editTextLieuCIN);
//        dateCin = findViewById(R.id.dateCIN);
        datecin = findViewById(R.id.pick_date_button2);
        selectedDateCin = findViewById(R.id.date_CIN);

        buttonRecto = this.findViewById(R.id.button_image_recto);
        recto = this.findViewById(R.id.cin_recto);
        buttonVerso = this.findViewById(R.id.button_image_verso);
        verso = this.findViewById(R.id.cin_verso);
        buttonImage = (Button) this.findViewById(R.id.button_fiche);
        imageView = (ImageView) this.findViewById(R.id.imageView);

        datederecensement = this.findViewById(R.id.daterecensement);
        selectedDateRecensement = this.findViewById(R.id.selected_Daterecens);

        enregistrer = this.findViewById(R.id.enregistrer);
        spinnerDocument = this.findViewById(R.id.spinner_document);
        user = getIntent().getStringExtra("user");
        isNevers = false;
        fichefull = false;
        feuMereSelected = false;
        feuPereSelected = false;
        final String[] docReference = {""};
        final String[] idFdocReference = {""};
        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
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
                    mShowSelectedDateText.setVisibility(View.GONE);
                    mPickDateButton.setVisibility(View.GONE);
                    editNevers.setVisibility(View.VISIBLE);
                } else {
                    isNevers = false;
                    mShowSelectedDateText.setVisibility(View.VISIBLE);
                    mPickDateButton.setVisibility(View.VISIBLE);
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
                capture();
            }
        });
        this.buttonVerso.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "verso";
                capture();
            }
        });
        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "feuillet";
                capture();
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

//        datedeNaissance.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                try {
//                    String dateDead = "10/06/" + anneeDead;
//                    String dateLimite = "10/06/2005";
//                    String nomElect = nom.getText().toString();
//                    String prenomElect = prenom.getText().toString();
//                    String naiss = datedeNaissance.getText().toString();
//                    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
////                    Boolean isDateValide = validateDate(naiss);
//                    Boolean isDateValide = checkDate(naiss);
//                    if (isDateValide) {
//                        Date dateNaiss = format.parse(naiss);
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
//                    } else {
//                        datedeNaissance.setError("Daty tsy valide");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        calendar.set(Calendar.YEAR, anneeMajor);
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        Long anneeFin = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, anneeDead);
        Long anneeStart = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        Calendar max = Calendar.getInstance();
        max.set(Calendar.YEAR, anneeMajor);
        max.set(Calendar.MONTH, Calendar.JUNE);
        max.set(Calendar.DAY_OF_MONTH, 11);

        CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(max.getTimeInMillis());
        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
        listValidators.add(dateValidatorMax);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintBuilder.setValidator(validators);
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
                materialDatePicker.show(getSupportFragmentManager(), "DATY NAHATERAHANA");
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

//        datederecensement.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String deb = "01/10/2022";
//                String daterecens = datederecensement.getText().toString();
//                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
//                Boolean isDateValide = checkDate(daterecens);
//                if (isDateValide) {
//                    try {
//                        Date dateRecens = format.parse(daterecens);
//                        Date datedeb = format.parse(deb);
//                        Date dateNow = Calendar.getInstance().getTime();
//                        if (datedeb.getTime() < dateRecens.getTime()) {
//                            isDateDerecense = true;
//                        } else {
//                            datederecensement.setError("Tsy ao anatiny daty");
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    datederecensement.setError("Daty tsy valide");
//                }
//
//            }
//        });

//        dateCin.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Date dateNow = Calendar.getInstance().getTime();
//                String dateNaiss = "datedeNaissance.getText().toString()";
//                String datedeliv = dateCin.getText().toString();
//                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
//                Boolean isDateValide = checkDate(datedeliv);
//                if (isDateValide) {
//                    try {
//                        Date naiss = format.parse(dateNaiss);
//                        Date deliv = format.parse(datedeliv);
//                        if (naiss.getTime() < deliv.getTime() && deliv.getTime() < dateNow.getTime()) {
//                            isDateCinValid = true;
//                        } else {
//                            dateCin.setError("tsy tafiditra ny date");
//                        }
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    dateCin.setError("Daty tsy valide");
//                }
//
//            }
//        });

        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar2.clear();
        int fin = anneeNow - 101+18;

        calendar2.set(Calendar.YEAR,anneeNow);
        calendar2.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH)+1);
        Long anneeFinDateCin = calendar2.getTimeInMillis();
        calendar2.set(Calendar.YEAR,fin);
        Long anneeStartDateCin = calendar2.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder2 = new CalendarConstraints.Builder();
        constraintBuilder2.setStart(anneeStartDateCin);
        constraintBuilder2.setEnd(anneeFinDateCin);

        MaterialDatePicker.Builder materialDateBuilder2 = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder2.setSelection(anneeFinDateCin);
        materialDateBuilder2.setCalendarConstraints(constraintBuilder2.build());
        materialDateBuilder2.setTitleText("Datin'ny karapanondro:");
        final MaterialDatePicker materialDatePicker2 = materialDateBuilder2.build();
        datecin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datecin.setEnabled(false);
                        materialDatePicker2.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        materialDatePicker2.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                datecin.setEnabled(true);
            }
        });
        materialDatePicker2.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long)selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate  = format.format(calendar.getTime());
                datecin.setEnabled(true);
                dateCinElect = formattedDate;
                selectedDateCin.setText("Daty nahazahona ny CNI: " + formattedDate);
            }
        });

        Calendar calendar3 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar3.clear();
        int fin3 = Calendar.getInstance().get(Calendar.YEAR)-1;

        calendar3.set(Calendar.YEAR,anneeNow);
        calendar3.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH)+1);
        Long anneeFinRecens = calendar3.getTimeInMillis();
        calendar3.set(Calendar.YEAR,fin3);
        Long anneeStartRecens = calendar3.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder3 = new CalendarConstraints.Builder();
        constraintBuilder3.setStart(anneeStartRecens);
        constraintBuilder3.setEnd(anneeFinRecens);

        MaterialDatePicker.Builder materialDateBuilder3 = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder3.setSelection(anneeFinRecens);
        materialDateBuilder3.setCalendarConstraints(constraintBuilder3.build());
        materialDateBuilder3.setTitleText("Daty:");
        final MaterialDatePicker materialDatePicker3 = materialDateBuilder3.build();
        datederecensement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datederecensement.setEnabled(false);
                        materialDatePicker3.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        materialDatePicker3.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                datederecensement.setEnabled(true);
            }
        });
        materialDatePicker3.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long)selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate  = format.format(calendar.getTime());
                datederecensement.setEnabled(true);
                daterecensement = formattedDate;
                selectedDateRecensement.setText("Date recensement : " + formattedDate);
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
                    if (dateNaiss != null) {
                        countFormValide += 1;
                        electeur.setDateNaiss(dateNaiss);
                        electeur.setNevers("");
                    } else {
                        mShowSelectedDateText.setTextColor(Color.RED);
                        mShowSelectedDateText.setText("Mila apetraka ny daty nahaterahana");
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
                    if (nserie2.getText().toString().length() == 0) {
                        String serial = nserie.getText().toString();
                        electeur.setNserieCin(serial);
                        countFormValide += 1;
                    } else {
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
                if (dateCinElect!=null) {
                    electeur.setDateDeliv(dateCinElect);
                    countFormValide += 1;
                } else{
                    selectedDateCin.setTextColor(Color.RED);
                    selectedDateCin.setText("Datin'ny CNI: ");
                }
                if (daterecensement != null) {
                    electeur.setDateinscription(daterecensement.toString());
                    countFormValide += 1;
                }  else {
                    selectedDateRecensement.setTextColor(Color.RED);
                    selectedDateRecensement.setText("Datin'ny recensement");
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
        Calendar cal = Calendar.getInstance();
        format.setLenient(false);
        try {
            Date d = format.parse(date);
            cal.setTime(d);
            Log.d("check_date", "" + cal.getTime() + " date valide");
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean validateDate(final String date) {

        matcher = pattern.matcher(date);

        if (matcher.matches()) {
            matcher.reset();

            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

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


}
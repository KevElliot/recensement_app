package com.ceni.recensementnumerique;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.provider.Settings.System.DATE_FORMAT;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.os.Build;
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
import java.io.File;
import java.text.DateFormat;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewElecteurActivity extends AppCompatActivity {
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private Spinner spinnerFokontany, spinnerCv, spinnerBv, spinnerDocument;
    private List<Document> document;
    private Button mPickDateButton, buttonRecto, buttonVerso, buttonImage, enregistrer, datecin, datederecensement;
    private ImageView recto, verso, imageView;
    private CheckBox sexeHomme, sexeFemme, feuPere, feuMere, nevers;
    private String sexe;
    private EditText nFiche, nom, prenom, lieuNaiss, profession, adresse, nomMere, nomPere, editNevers;
    private EditText cin, nserie, nserie2, lieuCin;
    private TextView infoCarnet, mShowSelectedDateText, selectedDateCin, selectedDateRecensement;
    private int countFormValide;
    private boolean isMemeFiche, isSamePers, isNevers, feuMereSelected, feuPereSelected, fichefull;
    private String msg, user, format, imageRecto, imageVerso, dataFicheElect, dateNaiss, dateCinElect, daterecensement;
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
                buttonRecto.setEnabled(false);
                format = "recto";
                verifyPermissions();
            }
        });
        this.buttonVerso.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonVerso.setEnabled(false);
                format = "verso";
                verifyPermissions();
            }
        });
        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonImage.setEnabled(false);
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
                isMemeFiche = DB.isMemeFiche(nFiche.getText().toString(), idFdocReference[0]);
                if (isMemeFiche) {
                    nFiche.setError("Takelaka efa voasoratra!");
                }
            }
        });

        lieuNaiss.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String nomElect = nom.getText().toString();
                String prenomElect = prenom.getText().toString();
                Bv bvSelected = (Bv) spinnerBv.getSelectedItem();
                isSamePers = DB.isSamePerson(nomElect, prenomElect, dateNaiss, bvSelected.getCode_bv());
                if (isSamePers) {
                    isSamePers = true;
                    nom.setError("mpifidy efa voasoratra!");
                    prenom.setError("mpifidy efa voasoratra!");
                    mShowSelectedDateText.setTypeface(Typeface.DEFAULT_BOLD);
                    mShowSelectedDateText.setText("mpifidy efa voasoratra!");
                }
            }
        });
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
        materialDateBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);
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
                mShowSelectedDateText.setTextColor(Color.BLACK);
                mShowSelectedDateText.setTypeface(Typeface.DEFAULT_BOLD);
                mPickDateButton.setEnabled(true);
                dateNaiss = formattedDate;
                mShowSelectedDateText.setText("Daty nahaterahana: " + formattedDate);
            }
        });


        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar2.clear();
        int fin = anneeNow - 101 + 18;

        calendar2.set(Calendar.YEAR, anneeNow);
        calendar2.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
        Long anneeFinDateCin = calendar2.getTimeInMillis();
        calendar2.set(Calendar.YEAR, fin);
        Long anneeStartDateCin = calendar2.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder2 = new CalendarConstraints.Builder();
        constraintBuilder2.setStart(anneeStartDateCin);
        constraintBuilder2.setEnd(anneeFinDateCin);

        MaterialDatePicker.Builder materialDateBuilder2 = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder2.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);
        materialDateBuilder2.setSelection(anneeFinDateCin);
        materialDateBuilder2.setCalendarConstraints(constraintBuilder2.build());
        materialDateBuilder2.setTitleText("Datin'ny karapanondro:");
        final MaterialDatePicker materialDatePicker2 = materialDateBuilder2.build();
        datecin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datecin.setEnabled(false);
                        materialDatePicker2.show(getSupportFragmentManager(), "DATE CNI:");
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
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = format.format(calendar.getTime());
                datecin.setEnabled(true);
                dateCinElect = formattedDate;
                selectedDateCin.setText("Daty nahazahona ny CNI: " + formattedDate);
            }
        });

        Calendar calendar3 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar3.clear();
        int fin3 = Calendar.getInstance().get(Calendar.YEAR) - 1;

        calendar3.set(Calendar.YEAR, anneeNow);
        calendar3.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 2);
        Long anneeFinRecens = calendar3.getTimeInMillis();
        calendar3.set(Calendar.DAY_OF_MONTH, 01);
        calendar3.set(Calendar.MONTH, 9);
        calendar3.set(Calendar.YEAR, 2022);
        Long anneeStartRecens = calendar3.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder3 = new CalendarConstraints.Builder();
        constraintBuilder3.setStart(anneeStartRecens);
        constraintBuilder3.setEnd(anneeFinRecens);

        MaterialDatePicker.Builder materialDateBuilder3 = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder3.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);
        materialDateBuilder3.setSelection(anneeFinRecens);
        materialDateBuilder3.setCalendarConstraints(constraintBuilder3.build());
        materialDateBuilder3.setTitleText("Daty:");
        final MaterialDatePicker materialDatePicker3 = materialDateBuilder3.build();
        datederecensement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datederecensement.setEnabled(false);
                        materialDatePicker3.show(getSupportFragmentManager(), "DATE DE RECENSEMENT:");
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
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = format.format(calendar.getTime());
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
                enregistrer.setEnabled(false);
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

                    int datynevers = Integer.parseInt(editNevers.getText().toString());
                    String[] arrOfStr = dateCinElect.split("/");
                    int datycin = Integer.parseInt(arrOfStr[2]);
                    int y = datycin - datynevers;

                    if (editNevers.getText().toString().length() == 4) {
                        if (datynevers < anneeMajor && datynevers > anneeDead) {
                            if (y >= 1) {
                                countFormValide += 1;
                                electeur.setNevers(editNevers.getText().toString());
                                electeur.setDateNaiss("");
                            } else {
                                editNevers.setError("Tsy ampy taona");
                                selectedDateCin.setText("verifieo ny daty CIN sy daty nahaterahana ");
                            }
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

                if (dateCinElect != null) {
                    String bday = "";
                    if (dateNaiss != null) {
                        bday = dateNaiss;
                    } else {
                        bday = "00/00/" + editNevers.getText().toString();
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
                    Date datycin = null;
                    Date naiss = null;
                    try {
                        datycin = formatter.parse(dateCinElect);
                        naiss = formatter.parse(bday);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int x = datycin.getYear() - naiss.getYear();
                    if (naiss.getTime() < datycin.getTime() && x >= 1) {
                        electeur.setDateDeliv(dateCinElect);
                        countFormValide += 1;
                    } else {
                        selectedDateCin.setTextColor(Color.RED);
                        selectedDateCin.setText("verifieo ny daty CIN sy daty nahaterahana ");
                    }
                } else {
                    selectedDateCin.setTextColor(Color.RED);
                    selectedDateCin.setText("Datin'ny CNI: ");
                }


                if (nFiche.getText().toString().length() >= 1 && nFiche.getText().toString().length() <= 8) {
                    if (nFiche.getText().toString().length() == 6) {
                        electeur.setnFiche("00" + nFiche.getText().toString());
                        countFormValide += 1;
                    } else if (nFiche.getText().toString().length() == 7) {
                        countFormValide += 1;
                        electeur.setnFiche("0" + nFiche.getText().toString());
                    } else if (nFiche.getText().toString().length() == 8) {
                        countFormValide += 1;
                        electeur.setnFiche(nFiche.getText().toString());
                    } else {
                        nFiche.setError("Misy diso");
                    }
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
                if (nserie.getText().toString().length() == 0) {
                    electeur.setNserieCin("illisible");
                    countFormValide += 1;
                }else{
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
                }
                if (lieuCin.getText().toString().length() != 0) {
                    electeur.setLieuDeliv(lieuCin.getText().toString());
                    countFormValide += 1;
                } else {
                    lieuCin.setError("Mila fenoina");
                }

                if (daterecensement != null) {
                    electeur.setDateinscription(daterecensement.toString());
                    countFormValide += 1;
                } else {
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
                    enregistrer.setEnabled(true);
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

                if (countFormValide != 15) {
                    enregistrer.setEnabled(true);
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
                        Bv bvSelected = (Bv) spinnerBv.getSelectedItem();
                        isSamePers = DB.isSamePerson(electeur.getNom(), electeur.getPrenom(), electeur.getDateNaiss(), bvSelected.getCode_bv());
                        if (!isSamePers) {
                            isMemeFiche = DB.isMemeFiche(nFiche.getText().toString(), idFdocReference[0]);
                            if (!isMemeFiche) {
                                Gson gson = new Gson();
                                User us = gson.fromJson(user, User.class);
                                User tmpus = DB.selectUser(us.getPseudo(), us.getMotdepasse());
                                electeur.setCode_bv(bvSelected.getCode_bv());
                                electeur.setProfession(profession.getText().toString());
                                boolean result = DB.insertElecteurData(electeur.getCode_bv(), electeur.getnFiche(), electeur.getNom(), electeur.getPrenom(), electeur.getSexe(), electeur.getProfession(), electeur.getAdresse(), electeur.getDateNaiss(), electeur.getNevers(), electeur.getLieuNaiss(), electeur.getNomPere(), electeur.getNomMere(), electeur.getCinElect(), electeur.getNserieCin(), electeur.getDateDeliv(), electeur.getLieuDeliv(), electeur.getFicheElect(), electeur.getCinRecto(), electeur.getCinVerso(), electeur.getObservation(), electeur.getDocreference(), us.getIdUser(), electeur.getDateinscription());
                                if (result) {
                                    Document doc = DB.selectDocumentbyid(electeur.getDocreference());
                                    DB.counterStat(doc, tmpus, 1);
                                    Toast toast = Toast.makeText(NewElecteurActivity.this, "Electeur enregistré!", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent i = new Intent(getApplicationContext(), NewElecteurActivity.class);
                                    i.putExtra("user", user);
                                    enregistrer.setEnabled(true);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                enregistrer.setEnabled(true);
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
                            enregistrer.setEnabled(true);
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
                        enregistrer.setEnabled(true);
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

    private void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    ////////////////////////////////// BEGIN MODIFICATION //////////////////////////////////

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    private void verifyPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {

            switch (format) {
                case "recto":
                    buttonRecto.setEnabled(true);
                    dispatchTakePictureIntent("recto");
                    break;
                case "verso":
                    buttonVerso.setEnabled(true);
                    dispatchTakePictureIntent("verso");
                    break;
                case "feuillet":
                    buttonImage.setEnabled(true);
                    dispatchTakePictureIntent("feuillet");
                    break;
                default:
                    Log.d("Verification", "Mitsofoka DEFAULT");
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    CAMERA_PERM_CODE);
            buttonVerso.setEnabled(true);
            buttonImage.setEnabled(true);
            buttonRecto.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                if (Objects.equals(this.format, "feuillet")) {
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_fiche_recensement);
                    Log.d("tag", "ABsolute Url of Image fiche recensement is " + Uri.fromFile(f_fiche));

                    Uri contentUri = Uri.fromFile(f_fiche);
                    Bitmap bitmap = null;
                    Bitmap tmpBitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                        tmpBitmap = this.resizeImage(bitmap, 1000, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    tmpBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    byte[] byteArray = stream.toByteArray();

                    this.imageView.setImageBitmap(tmpBitmap);

                    dataFicheElect = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    if (f_fiche.exists()) {
                        if (f_fiche.delete()) {
                            Log.d("Fiche_elect_deleted", "file delete : " + Uri.fromFile(f_fiche));

                        } else {
                            Log.d("Fiche_elect_not_deleted", "file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }
                } else if (Objects.equals(this.format, "recto")) {
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_cin_recto_recensement);
                    // recto.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image recto recensement is " + Uri.fromFile(f_fiche));

//                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    Bitmap tmpBitmap2 = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                        tmpBitmap2 = this.resizeImage(bitmap, 1000, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    tmpBitmap2.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    byte[] byteArray = stream.toByteArray();
                    this.recto.setImageBitmap(tmpBitmap2);

                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    if (f_fiche.exists()) {
                        if (f_fiche.delete()) {
                            Log.d("recto_deleted", "file delete : " + Uri.fromFile(f_fiche));
                        } else {
                            Log.d("recto_not_deleted", "file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }
                } else if (Objects.equals(this.format, "verso")) {
                    // file for recensement fiche
                    File f_fiche = new File(currentPhotoPath_cin_verso_recensement);
                    // verso.setImageURI(Uri.fromFile(f_fiche));
                    Log.d("tag", "ABsolute Url of Image verso recensement is " + Uri.fromFile(f_fiche));

//                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f_fiche);

                    Bitmap bitmap = null;
                    Bitmap tmpBitmap3 = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                        tmpBitmap3 = this.resizeImage(bitmap, 1000, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    tmpBitmap3.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    byte[] byteArray = stream.toByteArray();

                    this.verso.setImageBitmap(tmpBitmap3);

                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    if (f_fiche.exists()) {
                        if (f_fiche.delete()) {
                            Log.d("verso_deleted", "file delete : " + Uri.fromFile(f_fiche));
                        } else {
                            Log.d("verso_not_deleted", "file not deleted : " + Uri.fromFile(f_fiche));
                        }
                    }
                }
            }
        }
    }

    public static Bitmap resizeImage(Bitmap realImage, float maxImageSize,
                                     boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private File createImageFile(String name_file) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".JPEG",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        switch (name_file) {
            case "feuillet":
                Log.d("FEUILLET ENREGISTRER ", "FEUILLET ENREGISTRER : " + image.getAbsolutePath());
                currentPhotoPath_fiche_recensement = image.getAbsolutePath();
                break;
            case "recto":
                Log.d("RECTO ENREGISTRER ", "RECTO ENREGISTRER : " + image.getAbsolutePath());
                currentPhotoPath_cin_recto_recensement = image.getAbsolutePath();
                break;
            case "verso":
                Log.d("VERSO ENREGISTRER ", "VERSO ENREGISTRER : " + image.getAbsolutePath());
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
}
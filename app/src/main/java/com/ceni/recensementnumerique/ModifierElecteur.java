package com.ceni.recensementnumerique;

import androidx.annotation.Nullable;
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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.service.Db_sqLite;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class ModifierElecteur extends AppCompatActivity {
    private EditText nomElect, prenomElect, professionElect, adresseElect, lieuNaissElect, nomPereElect, nomMereElect, cinElect, nSerie, lieuCinElect, editTextNevers;
    private ImageView ficheElect, cin_recto, cin_verso, retour;
    private Button button_image_fiche, mPickDateButton, pick_datecin, button_image_recto, button_image_verso, enregistrer;
    private CheckBox sexeHomme, sexeFemme, nevers, observation1, observation2, observation3;
    private TextView dateNaissElect, dateCinElect, dateInscr, nFiche;
    private String format, imageFiche, imageRecto, imageVerso, dateNaiss, neversDate, dateCin, sexe, electSexe, observationElect;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    Db_sqLite db_sqLite;
    private boolean isNevers;
    private String currentPhotoPath_fiche_recensement, currentPhotoPath_cin_recto_recensement, currentPhotoPath_cin_verso_recensement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_electeur);
        Gson gson = new Gson();
        Electeur electeur = gson.fromJson(getIntent().getStringExtra("electeur"), Electeur.class);
        imageFiche = electeur.getFicheElect();
        imageRecto = electeur.getCinRecto();
        imageVerso = electeur.getCinVerso();
        db_sqLite = new Db_sqLite(this);
        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow - 18;
        int anneeDead = anneeNow - 150;
        final int[] countFormValide = {0};
        int anneeDebutCin = anneeNow;
        int anneeFinCin = anneeNow - 101 + 18;
        electSexe = electeur.getSexe();
        observationElect = electeur.getObservation();
        dateInscr = findViewById(R.id.dateInscr);
        nFiche = findViewById(R.id.nFiche);
        nomElect = findViewById(R.id.nomElect);
        prenomElect = findViewById(R.id.prenomElect);
        professionElect = findViewById(R.id.professionElect);
        adresseElect = findViewById(R.id.adresseElect);
        lieuNaissElect = findViewById(R.id.lieuNaissElect);
        nomPereElect = findViewById(R.id.nomPereElect);
        nomMereElect = findViewById(R.id.nomMereElect);
        cinElect = findViewById(R.id.cinElect);
        nSerie = findViewById(R.id.nSerie);
        lieuCinElect = findViewById(R.id.lieuCinElect);
        editTextNevers = findViewById(R.id.editTextNevers);
        ficheElect = findViewById(R.id.ficheElect);
        cin_recto = findViewById(R.id.cin_recto);
        cin_verso = findViewById(R.id.cin_verso);
        button_image_fiche = findViewById(R.id.button_image_fiche);
        mPickDateButton = findViewById(R.id.pick_date_button);
        pick_datecin = findViewById(R.id.pick_datecin);
        button_image_recto = findViewById(R.id.button_image_recto);
        button_image_verso = findViewById(R.id.button_image_verso);
        sexeHomme = findViewById(R.id.sexeHomme);
        sexeFemme = findViewById(R.id.sexeFemme);
        nevers = findViewById(R.id.nevers);
        observation1 = findViewById(R.id.Observation1);
        observation2 = findViewById(R.id.Observation2);
        observation3 = findViewById(R.id.Observation3);
        dateNaissElect = findViewById(R.id.dateNaissElect);
        dateCinElect = findViewById(R.id.dateCinElect);
        retour = findViewById(R.id.retour);
        enregistrer = findViewById(R.id.enregistrer);

        isNevers = false;

        // begin modification electeur //
        nFiche.setText(electeur.getnFiche());
        nomElect.setText(electeur.getNom());
        prenomElect.setText(electeur.getPrenom());
        professionElect.setText(electeur.getProfession());
        adresseElect.setText(electeur.getAdresse());
        lieuNaissElect.setText(electeur.getLieuNaiss());
        nomPereElect.setText(electeur.getNomPere());
        nomMereElect.setText(electeur.getNomMere());
        cinElect.setText(electeur.getCinElect());
        nSerie.setText(electeur.getNserieCin());
        lieuCinElect.setText(electeur.getLieuDeliv());
        if (electeur.getNevers().length() != 0) {
            nevers.setChecked(true);
            mPickDateButton.setVisibility(View.GONE);
            editTextNevers.setText(electeur.getNevers());
            neversDate = electeur.getNevers();
        } else {
            nevers.setChecked(false);
            editTextNevers.setVisibility(View.GONE);
            dateNaissElect.setText(electeur.getDateNaiss());
            dateNaiss = electeur.getDateNaiss();
        }
        dateCinElect.setText(electeur.getDateDeliv());
        dateInscr.setText(electeur.getDateinscription());
        dateCin = electeur.getDateDeliv();
        Bitmap imgFiche = this.decodeImage(electeur.getFicheElect());

        ficheElect.setImageBitmap(imgFiche);
        if (electeur.getCinRecto() != null) {
            cin_recto.setVisibility(View.VISIBLE);
            Bitmap imgCinRecto = this.decodeImage(electeur.getCinRecto());
            cin_recto.setImageBitmap(imgCinRecto);

        } else {
            cin_recto.setVisibility(View.GONE);
        }
        if (electeur.getCinVerso() != null) {
            cin_verso.setVisibility(View.VISIBLE);
            Bitmap imgCinVerso = this.decodeImage(electeur.getCinVerso());
            cin_verso.setImageBitmap(imgCinVerso);
        } else {
            cin_verso.setVisibility(View.GONE);
        }

        // cin_recto.setImageBitmap(imgCinRecto);
        // cin_verso.setImageBitmap(imgCinVerso);

        if (electSexe.equals("Masculin")) {
            sexe = "Masculin";
            sexeHomme.setChecked(true);
            sexeFemme.setChecked(false);
        } else {
            sexe = "Feminin";
            sexeHomme.setChecked(false);
            sexeFemme.setChecked(true);
        }

        if (observationElect == "Nouveau recensement electeur") {
            observation1.setChecked(true);
            observation2.setChecked(false);
            observation3.setChecked(false);
        } else if (observationElect == "Nouveau titulaire CIN") {
            observation1.setChecked(false);
            observation2.setChecked(true);
            observation3.setChecked(false);
        } else if (observationElect == "JSN/CIN") {
            observation1.setChecked(false);
            observation2.setChecked(false);
            observation3.setChecked(true);
        }
        nevers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nevers.isChecked()) {
                    isNevers = true;
                    editTextNevers.setVisibility(View.VISIBLE);
                    mPickDateButton.setVisibility(View.INVISIBLE);
                    dateNaissElect.setVisibility(View.INVISIBLE);
                } else {
                    isNevers = false;
                    editTextNevers.setVisibility(View.GONE);
                    mPickDateButton.setVisibility(View.VISIBLE);
                    dateNaissElect.setVisibility(View.VISIBLE);
                }
            }
        });

        button_image_recto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "recto";
                verifyPermissions();
            }
        });
        button_image_verso.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "verso";
                verifyPermissions();
            }
        });
        button_image_fiche.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "fiche";
                verifyPermissions();
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
        materialDateBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);
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
                mPickDateButton.setEnabled(true);
                neversDate = "";
                dateNaiss = formattedDate;
                dateNaissElect.setText(formattedDate);
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
        pick_datecin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pick_datecin.setEnabled(false);
                        materialDatePicker2.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        materialDatePicker2.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                pick_datecin.setEnabled(true);
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
                pick_datecin.setEnabled(true);
                dateCin = formattedDate;
                dateCinElect.setText(formattedDate);
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
                    sexe = "Feminin";
                    sexeFemme.setChecked(true);
                    sexeHomme.setChecked(false);
                } else {
                    sexe = "Masculin";
                    sexeFemme.setChecked(false);
                    sexeHomme.setChecked(true);
                }
            }
        });
        //Button previous
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        observation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (observation1.isChecked()) {
                    observation1.setChecked(true);
                    observation2.setChecked(false);
                    observation3.setChecked(false);
                } else {
                    observation1.setChecked(false);
                    observation2.setChecked(true);
                    observation3.setChecked(false);
                }
            }
        });
        observation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (observation2.isChecked()) {
                    observation1.setChecked(false);
                    observation2.setChecked(true);
                    observation3.setChecked(false);
                } else {
                    observation1.setChecked(false);
                    observation2.setChecked(false);
                    observation3.setChecked(true);
                }
            }
        });
        observation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (observation3.isChecked()) {
                    observation1.setChecked(false);
                    observation2.setChecked(false);
                    observation3.setChecked(true);
                } else {
                    observation1.setChecked(true);
                    observation2.setChecked(false);
                    observation3.setChecked(false);
                }
            }
        });
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Electeur e = new Electeur();
                int idElect = electeur.getIdElect();
                if (editTextNevers.getText().toString().length() != 0) {
                    neversDate = editTextNevers.getText().toString();
                    dateNaiss = "";
                } else {
                    neversDate = "";
                }
                e.setIdElect(idElect);
                e.setDocreference(electeur.getDocreference());
                e.setCode_bv(electeur.getCode_bv());

                e.setnFiche(nFiche.getText().toString());
                if (nomElect.getText().toString().length() != 0) {
                    e.setNom(nomElect.getText().toString());
                } else {
                    nomElect.setError("Mila fenoina");
                }
                if (prenomElect.getText().toString().length() != 0) {
                    e.setPrenom(prenomElect.getText().toString());
                } else {
                    electeur.setPrenom("");
                }
                if (Objects.equals(sexe, "Masculin") || Objects.equals(sexe, "Feminin")) {
                    e.setSexe(sexe);
                }
                if (professionElect.getText().toString().length() != 0) {
                    e.setProfession(professionElect.getText().toString());
                } else {
                    professionElect.setError("Mila fenoina");
                }
                if (adresseElect.getText().toString().length() != 0) {
                    e.setAdresse(adresseElect.getText().toString());
                } else {
                    adresseElect.setError("Mila fenoina");
                }
                // e.setDateNaiss(dateNaiss);
                // e.setNevers(neversDate);

                if (isNevers) {
                    neversDate = editTextNevers.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
                    int datycin = 0;
                    int neversVal = 0;

                    String[] arrOfStr = dateCin.split("/");
                    datycin = Integer.parseInt(arrOfStr[2]);

                    neversVal = Integer.parseInt(editTextNevers.getText().toString());
                    int x = datycin - neversVal;

                    int anneeNevers = Integer.parseInt(editTextNevers.getText().toString());
                    if (editTextNevers.getText().toString().length() == 4 && x >= 1) {
                        if (anneeNevers < anneeMajor && anneeNevers > anneeDead) {
                            e.setNevers(editTextNevers.getText().toString());
                            e.setDateNaiss("");
                        } else {
                            editTextNevers.setError("Tsy ao anatiny taona");
                        }
                    } else {
                        editTextNevers.setError("Diso");
                    }
                } else {
                    if (dateNaiss != null) {
                        //dateNaissElect
                        e.setDateNaiss(dateNaissElect.getText().toString());
                        e.setNevers("");
                    } else {
                        editTextNevers.setTextColor(Color.RED);
                        editTextNevers.setText("Mila apetraka ny daty nahaterahana");
                    }
                }

                if (lieuNaissElect.getText().toString().length() != 0) {
                    e.setLieuNaiss(lieuNaissElect.getText().toString());
                } else {
                    lieuNaissElect.setError("Mila fenoina");
                }
                if (nomPereElect.getText().toString().length() != 0) {
                    e.setNomPere(nomPereElect.getText().toString());
                } else {
                    nomPereElect.setError("Mila fenoina");
                }
                if (nomMereElect.getText().toString().length() != 0) {
                    e.setNomMere(nomMereElect.getText().toString());
                } else {
                    nomMereElect.setError("Mila fenoina");
                }
                if (cinElect.getText().toString().length() == 12) {
                    e.setCinElect(cinElect.getText().toString());
                } else {
                    cinElect.setError("Mila fenoina");
                }
                if (nSerie.getText().toString().length() != 0) {
                    e.setNserieCin(nSerie.getText().toString());
                } else {
                    nSerie.setError("Mila fenoina");
                }
                if (dateCin != null) {
                    int bday = 0;
                    if (!isNevers) {
                        String[] arrOfStr = dateNaissElect.getText().toString().split("/");
                        bday = Integer.parseInt(arrOfStr[2]);

                    } else {
                        bday = Integer.parseInt(editTextNevers.getText().toString());
                    }
                    String[] cindat = dateCin.split("/");
                    int cin = Integer.parseInt(cindat[2]);

                    Log.d("datycin", "" + cin);
                    Log.d("naiss", "" + bday);

                    int x = cin - bday;
                    if (bday < cin) {
                        if (x >= 1) {
                            e.setDateDeliv(dateCin);
                            countFormValide[0] += 1;
                        } else {
                            Log.d("x>=10", "x>=10");
                            dateCinElect.setError("verifieo ny daty CIN sy daty nahaterahana ");
                        }
                    } else {
                        Log.d("bday < cin", "bday < cin");
                        dateCinElect.setError("verifieo ny daty CIN sy daty nahaterahana ");
                    }
                }


                if (lieuCinElect.getText().toString().length() != 0) {
                    e.setLieuDeliv(lieuCinElect.getText().toString());
                } else {
                    lieuCinElect.setError("Mila fenoina");
                }
                e.setFicheElect(imageFiche);
                Log.d("fiche", "" + imageFiche.length());
                if (imageRecto != null) {
                    e.setCinRecto(imageRecto);
                    Log.d("recto", "" + imageRecto.length());
                }
                if (imageVerso != null) {
                    e.setCinVerso(imageVerso);
                    Log.d("verso", "" + imageVerso.length());
                }
                e.setObservation(observationElect);
                e.setDateinscription(electeur.getDateinscription());
                if (countFormValide[0] == 1) {
                    boolean update = db_sqLite.updateElect(e);
                    if (update) {
                        RechercheElecteur.getInstance().finish();
                        finish();
                        Intent i = new Intent(getApplicationContext(), RechercheElecteur.class);
                        startActivity(i);
                    } else {
                        Toast toast = Toast.makeText(ModifierElecteur.this, "Erreur lors de la modification!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(ModifierElecteur.this, "Daty nahaterahana na daty cin misy diso!", Toast.LENGTH_LONG);
                    toast.show();
                }
                Log.d("TAG", "onClick: " + e.toString());
                Log.d("ID", "ID: " + idElect);
            }
        });

    }

    public Bitmap decodeImage(String dataImageElect) {
        //Decode image
        byte[] img = Base64.decode(dataImageElect, Base64.DEFAULT);
        Bitmap bm;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        bm = BitmapFactory.decodeByteArray(img, 0, img.length, opt);
        return bm;
    }

    private void capture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    ////////////////////////////////// BEGIN MODIFICATION //////////////////////////////////

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    // ImageView recto, verso, imageView;
    // ImageView ficheElect, cin_recto, cin_verso,
    // private String msg, user, format, imageRecto, imageVerso, dataFicheElect;
    // String currentPhotoPath_fiche_recensement, currentPhotoPath_cin_recto_recensement, currentPhotoPath_cin_verso_recensement;

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
        } else {
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
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

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

                    this.ficheElect.setImageBitmap(bitmap);

                    imageFiche = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    // this.generateNoteOnSD(this.getApplicationContext(), "base64", imageFiche);

                    if (f_fiche.exists()) {
                        if (f_fiche.delete()) {
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else {
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

                    this.cin_recto.setImageBitmap(bitmap);

                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 RECTO : ", imageRecto.trim());

                    if (f_fiche.exists()) {
                        if (f_fiche.delete()) {
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else {
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

                    this.cin_verso.setImageBitmap(bitmap);

                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE 64 VERSO : ", imageVerso.trim());

                    if (f_fiche.exists()) {
                        if (f_fiche.delete()) {
                            System.out.println("file delete : " + Uri.fromFile(f_fiche));
                        } else {
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
        switch (name_file) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");

                //Encode Image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                if(this.format == "fiche"){
                    imageFiche = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    this.ficheElect.setImageBitmap(bp);
                }
                if (this.format == "recto") {
                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    this.cin_recto.setImageBitmap(bp);
                } else if (this.format == "verso") {
                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    this.cin_verso.setImageBitmap(bp);
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
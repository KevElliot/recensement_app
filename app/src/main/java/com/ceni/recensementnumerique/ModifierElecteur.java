package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ModifierElecteur extends AppCompatActivity {
    private EditText nFiche, nomElect, prenomElect, professionElect, adresseElect, lieuNaissElect, nomPereElect, nomMereElect, cinElect, nSerie, lieuCinElect, editTextNevers;
    private ImageView ficheElect, cin_recto, cin_verso, retour;
    private Button button_image_fiche, mPickDateButton, pick_datecin, button_image_recto, button_image_verso, enregistrer;
    private CheckBox sexeHomme, sexeFemme, nevers, observation1, observation2, observation3;
    private TextView dateNaissElect, dateCinElect;
    private String format,imageFiche, imageRecto, imageVerso,dateNaiss,neversDate, dateCin, sexe, electSexe, observationElect;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    Db_sqLite db_sqLite;

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
        int anneeDebutCin = anneeNow;
        int anneeFinCin = anneeNow - 101 + 18;
        electSexe = electeur.getSexe();
        observationElect = electeur.getObservation();
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
            nevers.isChecked();
            editTextNevers.setText(electeur.getNevers());
            neversDate = electeur.getNevers();
        } else {
            editTextNevers.setVisibility(View.GONE);
            dateNaiss = electeur.getDateNaiss();
        }
        dateNaissElect.setText(electeur.getDateNaiss());
        dateCinElect.setText(electeur.getDateDeliv());
        dateCin = electeur.getDateDeliv();
        Bitmap imgFiche = this.decodeImage(electeur.getFicheElect());

        ficheElect.setImageBitmap(imgFiche);
        if(electeur.getCinRecto()!=null){
            cin_recto.setVisibility(View.VISIBLE);
            Bitmap imgCinRecto = this.decodeImage(electeur.getCinRecto());
            cin_recto.setImageBitmap(imgCinRecto);

        }else{
            cin_recto.setVisibility(View.GONE);
        }
        if(electeur.getCinVerso()!=null){
            cin_verso.setVisibility(View.VISIBLE);
            Bitmap imgCinVerso = this.decodeImage(electeur.getCinVerso());
            cin_verso.setImageBitmap(imgCinVerso);
        }else{
            cin_verso.setVisibility(View.GONE);
        }

        // cin_recto.setImageBitmap(imgCinRecto);
        // cin_verso.setImageBitmap(imgCinVerso);

        if (electSexe.equals("Homme")) {
            sexe = "Homme";
            sexeHomme.setChecked(true);
            sexeFemme.setChecked(false);
        } else {
            sexe = "Femme";
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
                    editTextNevers.setVisibility(View.VISIBLE);
                    mPickDateButton.setVisibility(View.INVISIBLE);
                    dateNaissElect.setVisibility(View.INVISIBLE);
                } else {
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
                capture();
            }
        });
        button_image_verso.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "verso";
                capture();
            }
        });
        button_image_fiche.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                format = "fiche";
                capture();
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
                mPickDateButton.setEnabled(true);
                neversDate= "";
                dateNaiss = formattedDate;
                dateNaissElect.setText(formattedDate);
            }
        });

        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar2.clear();
        calendar2.set(Calendar.YEAR, anneeDebutCin);
        Long fin = calendar2.getTimeInMillis();
        calendar2.set(Calendar.YEAR, anneeFinCin);
        Long deb = calendar2.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder2 = new CalendarConstraints.Builder();
        constraintBuilder2.setStart(deb);
        constraintBuilder2.setEnd(fin);

        MaterialDatePicker.Builder materialDateBuilder2 = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder2.setSelection(fin);
        materialDateBuilder2.setCalendarConstraints(constraintBuilder.build());
        materialDateBuilder2.setTitleText("Datin'ny karapanondro:");
        final MaterialDatePicker materialDatePicker2 = materialDateBuilder.build();
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
                Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar2.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = format.format(calendar2.getTime());
                pick_datecin.setEnabled(true);
                dateCin = formattedDate;
                dateCinElect.setText(dateCin);
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
        enregistrer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Electeur e = new Electeur();
                int idElect = electeur.getIdElect();
                if(editTextNevers.getText().toString().length()!=0){
                    neversDate = editTextNevers.getText().toString();
                    dateNaiss="";
                }else{
                    neversDate = "";
                }
                e.setIdElect(idElect);
                e.setDocreference(electeur.getDocreference());
                e.setCode_bv(electeur.getCode_bv());
                e.setnFiche(nFiche.getText().toString());
                e.setNom(nomElect.getText().toString());
                e.setPrenom(prenomElect.getText().toString());
                e.setSexe(sexe);
                e.setProfession(professionElect.getText().toString());
                e.setAdresse(adresseElect.getText().toString());
                e.setDateNaiss(dateNaiss);
                e.setNevers(neversDate);
                e.setLieuNaiss(lieuNaissElect.getText().toString());
                e.setNomPere(nomPereElect.getText().toString());
                e.setNomMere(nomMereElect.getText().toString());
                e.setCinElect(cinElect.getText().toString());
                e.setNserieCin(nSerie.getText().toString());
                e.setDateDeliv(dateCin);
                e.setLieuDeliv(lieuCinElect.getText().toString());
                e.setFicheElect(imageFiche);
                Log.d("fiche",""+imageFiche.length());
                e.setCinRecto(imageRecto);
                Log.d("recto",""+imageRecto.length());
                e.setCinVerso(imageVerso);
                Log.d("verso",""+imageVerso.length());
                e.setObservation(observationElect);
                e.setDateinscription(electeur.getDateinscription());
                boolean update = db_sqLite.updateElect(e);
                if(update){
                    RechercheElecteur.getInstance().finish();
                    finish();
                    Intent i = new Intent(getApplicationContext(), RechercheElecteur.class);
                    startActivity(i);
                }else{
                    Toast toast = Toast.makeText(ModifierElecteur.this, "Erreur lors de la modification!", Toast.LENGTH_LONG);
                    toast.show();
                }
                Log.d("TAG", "onClick: "+e.toString());
                Log.d("ID", "ID: "+idElect);
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
}
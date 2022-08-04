package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceni.model.Electeur;
import com.google.gson.Gson;

public class ModifierElecteur extends AppCompatActivity {
    private EditText nFiche,nomElect,prenomElect,professionElect,adresseElect,lieuNaissElect,nomPereElect,nomMereElect,cinElect,nSerie,lieuCinElect,editTextNevers;
    private ImageView ficheElect, cin_recto,cin_verso;
    private Button button_image_fiche, pick_date_button, pick_datecin, button_image_recto, button_image_verso;
    private CheckBox sexeHomme, sexeFemme, nevers, observation1, observation2, observation3;
    private TextView dateNaissElect, dateCinElect;
    private String electSexe,observationElect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_electeur);
        Gson gson = new Gson();
        Electeur electeur = gson.fromJson(getIntent().getStringExtra("electeur"), Electeur.class);
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
        editTextNevers= findViewById(R.id.editTextNevers);
        ficheElect = findViewById(R.id.ficheElect);
        cin_recto = findViewById(R.id.cin_recto);
        cin_verso = findViewById(R.id.cin_verso);
        button_image_fiche= findViewById(R.id.button_image_fiche);
        pick_date_button = findViewById(R.id.pick_date_button);
        pick_datecin = findViewById(R.id.pick_datecin);
        button_image_recto= findViewById(R.id.button_image_recto);
        button_image_verso = findViewById(R.id.button_image_verso);
        sexeHomme = findViewById(R.id.sexeHomme);
        sexeFemme = findViewById(R.id.sexeFemme);
        nevers = findViewById(R.id.nevers);
        observation1 = findViewById(R.id.Observation1);
        observation2 = findViewById(R.id.Observation2);
        observation3 = findViewById(R.id.Observation3);
        dateNaissElect = findViewById(R.id.dateNaissElect);
        dateCinElect = findViewById(R.id.dateCinElect);


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
        if(electeur.getNevers().length() != 0){
            editTextNevers.setText(electeur.getNevers());
        }else{
            editTextNevers.setVisibility(View.GONE);
        }
        dateNaissElect.setText(electeur.getDateNaiss());
        dateCinElect.setText(electeur.getDateDeliv());
        Bitmap imgFiche = this.decodeImage(electeur.getFicheElect());
        Bitmap imgCinRecto = this.decodeImage(electeur.getCinRecto());
        Bitmap imgCinVerso = this.decodeImage(electeur.getCinVerso());
        ficheElect.setImageBitmap(imgFiche);
        cin_recto.setImageBitmap(imgCinRecto);
        cin_verso.setImageBitmap(imgCinVerso);

        if(electSexe == "Homme"){
            sexeHomme.setChecked(true);
            sexeFemme.setChecked(false);
        }else{
            sexeHomme.setChecked(false);
            sexeFemme.setChecked(true);
        }

        if(observationElect == "Nouveau recensement electeur"){
            observation1.setChecked(true);
            observation2.setChecked(false);
            observation3.setChecked(false);
        }else if(observationElect == "Nouveau titulaire CIN"){
            observation1.setChecked(false);
            observation2.setChecked(true);
            observation3.setChecked(false);
        }else if(observationElect == "JSN/CIN"){
            observation1.setChecked(false);
            observation2.setChecked(false);
            observation3.setChecked(true);
        }

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
}
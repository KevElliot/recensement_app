package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

public class DetailElecteurActivity extends AppCompatActivity {
    private ImageView ficheElect, cinRecto, cinVerso, retour, modif;
    private TextView nfiche, dateInscr, nom, prenom, sexe, profession, adresse, dateNaiss, lieuNaiss, nomPere, nomMere, cinElect, nserie, dateDeliv, lieuDeliv, observation, nFeuillet;
    private Button suppression;
    private Db_sqLite db;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailelecteur);
        retour = findViewById(R.id.retour);
        modif = findViewById(R.id.modif);
        suppression = findViewById(R.id.delete);
        ficheElect = findViewById(R.id.ficheElect);
        dateInscr = findViewById(R.id.dateInscr);
        cinRecto = findViewById(R.id.cin_recto);
        cinVerso = findViewById(R.id.cin_verso);
        nfiche = findViewById(R.id.nFiche);
        nom = findViewById(R.id.nomElect);
        prenom = findViewById(R.id.prenomElect);
        sexe = findViewById(R.id.sexeElect);
        profession = findViewById(R.id.professionElect);
        adresse = findViewById(R.id.adresseElect);
        dateNaiss = findViewById(R.id.dateNaissElect);
        lieuNaiss = findViewById(R.id.lieuNaissElect);
        nomPere = findViewById(R.id.nomPereElect);
        nomMere = findViewById(R.id.nomMereElect);
        cinElect = findViewById(R.id.cinElect);
        nserie = findViewById(R.id.nSerie);
        dateDeliv = findViewById(R.id.dateCinElect);
        lieuDeliv = findViewById(R.id.lieuCinElect);
        observation = findViewById(R.id.observation);
        nFeuillet = findViewById(R.id.nFeuillet);
        db = new Db_sqLite(this);
        Gson gson = new Gson();
        Electeur electeur = gson.fromJson(getIntent().getStringExtra("electeur"), Electeur.class);
        nFeuillet.setText(electeur.getnFiche());

        Document d = db.selectDocumentbyid(electeur.getDocreference());
        nfiche.setText(d.getNumdocreference());
        dateInscr.setText(electeur.getDateinscription());
        nom.setText(electeur.getNom());
        prenom.setText(electeur.getPrenom());
        sexe.setText(electeur.getSexe());
        profession.setText(electeur.getProfession());
        adresse.setText(electeur.getAdresse());
        if (electeur.getDateNaiss() == null) {
            dateNaiss.setText("null");
        } else {
            if (electeur.getDateNaiss().length() != 0) {
                dateNaiss.setText(electeur.getDateNaiss());
            } else {
                dateNaiss.setText(electeur.getNevers());
            }
        }
        lieuNaiss.setText(electeur.getLieuNaiss());
        nomPere.setText(electeur.getNomPere());
        nomMere.setText(electeur.getNomMere());
        cinElect.setText(electeur.getCinElect());
        nserie.setText(electeur.getNserieCin());
        dateDeliv.setText(electeur.getDateDeliv());
        lieuDeliv.setText(electeur.getLieuDeliv());
        if (electeur.getFicheElect() != null) {
            ficheElect.setVisibility(View.VISIBLE);
            Bitmap imgFiche = this.decodeImage(electeur.getFicheElect());
            ficheElect.setImageBitmap(imgFiche);
        } else {
            ficheElect.setVisibility(View.GONE);
        }
        if (electeur.getCinRecto() != null) {
            cinRecto.setVisibility(View.VISIBLE);
            Bitmap imgCinRecto = this.decodeImage(electeur.getCinRecto());
            cinRecto.setImageBitmap(imgCinRecto);
        } else {
            cinRecto.setVisibility(View.GONE);
        }
        if (electeur.getCinVerso() != null) {
            cinVerso.setVisibility(View.VISIBLE);
            Bitmap imgCinVerso = this.decodeImage(electeur.getCinVerso());
            cinVerso.setImageBitmap(imgCinVerso);
        } else {
            cinVerso.setVisibility(View.GONE);
        }
        //observation.setText(electeur.getObservation());

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        modif.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ModifierElecteur.class);
                String myjson = gson.toJson(electeur);
                i.putExtra("electeur", myjson);
                startActivity(i);
                finish();
            }
        });
        suppression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Db_sqLite DB = new Db_sqLite(DetailElecteurActivity.this);
                Boolean deleted = DB.deleteElect(electeur.getCinElect());
                if (deleted) {
                    Document doc = DB.selectDocumentbyid(electeur.getDocreference());
                    User user = MenuActivity.getCurrent_user();
                    User users = DB.selectUser(user.getPseudo(), user.getMotdepasse());
                    String myjson = gson.toJson(users);
                    DB.counterStat(doc, users, -1);
                    ListeFokontanyActivity.getInstance().finish();
                    RechercheElecteur.getInstance().finish();
                    Intent i = new Intent(getApplicationContext(), ListeFokontanyActivity.class);
                    i.putExtra("user", myjson);
                    startActivity(i);
                    finish();

                } else {
                    Toast toast = Toast.makeText(DetailElecteurActivity.this, "Erreur lors de la suppression!", Toast.LENGTH_LONG);
                    toast.show();
                }

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
}


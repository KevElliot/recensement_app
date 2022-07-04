package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.service.Api_service;
import com.ceni.service.Common;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImageCinActivity extends AppCompatActivity {
    private Button buttonRecto, buttonVerso;
    private ImageView previous, recto, verso, next;
    private String format, imageRecto, imageVerso;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private Db_sqLite DB;
    private Api_service API;
    private Common checkCo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagecin);

        this.buttonRecto = this.findViewById(R.id.button_image_recto);
        this.recto = this.findViewById(R.id.cin_recto);
        this.buttonVerso = this.findViewById(R.id.button_image_verso);
        this.verso = this.findViewById(R.id.cin_verso);
        this.next = this.findViewById(R.id.imageViewNext);
        this.previous = this.findViewById(R.id.imageViewPrevious);
        DB = new Db_sqLite(this);
        API = new Api_service();
        checkCo = new Common();

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

        this.next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                if (imageRecto != null && imageVerso != null) {
                    electeur.setCinRecto(imageRecto);
                    //Log.i("image recto", imageRecto);
                    electeur.setCinVerso(imageVerso);
                    //Log.i("image verso", imageVerso);
                    //String myjson = gson.toJson(electeur);
                    Date daty = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dat = sdf.format(daty.getTime());
                    electeur.setDateinscription("" + dat);
                    Log.d("DATTYYYYY", "" + dat);

                    boolean result = DB.insertElecteurData(electeur.getCode_bv(), electeur.getnFiche(), electeur.getNom(), electeur.getPrenom(), electeur.getSexe(), electeur.getProfession(), electeur.getAdresse(), electeur.getDateNaiss(), electeur.getLieuNaiss(), electeur.getNomPere(), electeur.getNomMere(), electeur.getCinElect(), electeur.getNserieCin(), electeur.getOriginCin(), electeur.getDateDeliv(), electeur.getLieuDeliv(), electeur.getFicheElect(), electeur.getCinRecto(), electeur.getCinVerso(), electeur.getDateinscription());
                    if (result) {
                        Toast toast = Toast.makeText(ImageCinActivity.this, "Electeur enregistrer!", Toast.LENGTH_LONG);
                        toast.show();
                        LocalisationActivity.getInstance().finish();
                        ImageFicheActivity.getInstance().finish();
                        Inscription2Activity.getInstance().finish();
                        InscriptionActivity.getInstance().finish();
                        Intent i = new Intent(getApplicationContext(), ListeElecteurActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(ImageCinActivity.this, "Erreur à l'enregistrement!", Toast.LENGTH_LONG);
                        toast.show();
                    }

                } else {
                    new AlertDialog.Builder(ImageCinActivity.this)
                            .setTitle("Fahadisoana?")
                            .setMessage("Iangaviana enao mba anisy sary azafady.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // tsisy
                                }
                            }).show();
                }
            }
        });
        this.previous.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

                //Encode Image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                if (this.format == "recto") {
                    imageRecto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    this.recto.setImageBitmap(bp);
                } else if (this.format == "verso") {
                    imageVerso = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    this.verso.setImageBitmap(bp);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
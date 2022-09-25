package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
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
import com.ceni.service.Db_sqLite;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

public class NewElecteurActivity extends AppCompatActivity {
    private List<Fokontany> fokontany;
    private List<Cv> cv;
    private List<Bv> bv;
    private Spinner spinnerFokontany, spinnerCv, spinnerBv, spinnerDocument;
    private List<Document> document;
    private Button mPickDateButton, buttonRecto, buttonVerso, buttonImage, enregistrer;
    private ImageView recto, verso, imageView;
    private CheckBox sexeHomme, sexeFemme, feuPere, feuMere, nevers;
    private String sexe;
    private EditText nFiche, nom, prenom, lieuNaiss, profession, adresse, nomMere, nomPere, datedeNaissance, editNevers;
    private EditText cin, nserie, nserie2, lieuCin, dateCin, datederecensement;
    private int countFormValide;
    private boolean isMemeFiche, isSamePers, isNevers, feuMereSelected, feuPereSelected;
    private String msg, user, format, imageRecto, imageVerso, dataFicheElect;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_electeur);
        MenuActivity.setNewElect(true);
        Db_sqLite DB = new Db_sqLite(this);
        sexe = "Masculin";
        msg = "Iangaviana enao mba ameno ireo banga na ny diso azafady.";
        sexeHomme = findViewById(R.id.sexeHomme);
        sexeFemme = findViewById(R.id.sexeFemme);
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
        user = getIntent().getStringExtra("user");
        isNevers = false;
        feuMereSelected = false;
        feuPereSelected = false;
        final String[] docReference = {""};
        final String[] idFdocReference = {""};
        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow - 18;
        int anneeDead = anneeNow - 150;

        // FOKONTANY
        fokontany = DB.selectFokotanyFromCommune("111301");
        spinnerFokontany = (Spinner) NewElecteurActivity.this.findViewById(R.id.spinner_fokontany);
        SpinerFokontanyAdapter adapterFokontany = new SpinerFokontanyAdapter(NewElecteurActivity.this,
                R.layout.dropdown_localisation,
                R.id.textViewLabel,
                R.id.textViewCode,
                fokontany);
        spinnerFokontany.setAdapter(adapterFokontany);
        spinnerFokontany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Fokontany fokontanySelected = (Fokontany) spinnerFokontany.getSelectedItem();
                Toast toast = Toast.makeText(NewElecteurActivity.this, "FOKONTANY: " + fokontanySelected.getLabel_fokontany(), Toast.LENGTH_LONG);
                toast.show();
                cv = DB.selectCvFromFokontany(fokontanySelected.getCode_fokontany());
                spinnerCv = (Spinner) NewElecteurActivity.this.findViewById(R.id.spinner_cv);
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

        this.document = DB.selectDocument();
        this.spinnerDocument = this.findViewById(R.id.spinner_document);

        //DOCUMENT
        // Adapter Document
        SpinnerDocumentAdapter adapterDocument = new SpinnerDocumentAdapter(NewElecteurActivity.this,
                R.layout.dropdown_document,
                R.id.numdocreference,
                this.document);
        this.spinnerDocument.setAdapter(adapterDocument);
        this.spinnerDocument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Document docSelected = (Document) spinnerDocument.getSelectedItem();
                docReference[0] = docSelected.getNumdocreference().toString();
                idFdocReference[0] = docSelected.getIdfdocreference();
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

        this.enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Document doc = DB.selectDocumentbyid(idFdocReference[0]);
//                Bv bvSelected = (Bv) spinnerBv.getSelectedItem();
//                String serialCin = nserie.getText().toString()+"/"+nserie2.getText().toString();
//                //Electeur e = new Electeur(bvSelected.getCode_bv(),nFiche.getText().toString(),nom.getText().toString(),prenom.getText().toString(), sexe, profession.getText().toString(), adresse.getText().toString(), datedeNaissance.getText().toString(),editNevers.getText().toString(),lieuNaiss.getText().toString(),nomPere.getText().toString(), nomMere.getText().toString(), cin.getText().toString(),serialCin, dateCin.getText().toString(), lieuCin.getText().toString(), dataFicheElect, imageRecto, imageVerso, "null", docReference[0],"info",datederecensement.getText().toString());
//                //Log.d("MAMPIDITRA MPIFIDY",e.toString());
                countFormValide = 0;
                Electeur electeur = new Electeur();
                if (nom.getText().toString().length() != 0) {
                    electeur.setNom(nom.getText().toString());
                    countFormValide += 1;
                } else {
                    nom.setError("Mila fenoina");
                }
                if (prenom.getText().toString().length() != 0) {
                    countFormValide+=1;
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
                    if (datedeNaissance.getText().length() != 0) {
                        countFormValide += 1;
                        electeur.setDateNaiss(datedeNaissance.getText().toString());
                        electeur.setNevers("");
                    } else {
                        datedeNaissance.setError("Mila apetraka ny daty nahaterahana");
                    }
                }

                if (nFiche.getText().toString().length() == 12) {
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
                if(cin.getText().toString().length()==12) {
                    electeur.setCinElect(cin.getText().toString());
                    countFormValide+=1;
                }else{
                    cin.setError("Diso");
                }
                if(nserie.getText().toString().length()==7 && nserie2.getText().toString().length()==1) {
                    String serial = nserie.getText().toString()+"/"+nserie2.getText().toString();
                    electeur.setNserieCin(serial);
                    countFormValide+=1;
                }else{
                    nserie.setError("Diso");
                }
                if(lieuCin.getText().toString().length()!=0) {
                    electeur.setLieuDeliv(lieuCin.getText().toString());
                    countFormValide+=1;
                }else{
                    lieuCin.setError("Mila fenoina");
                }
                if(dateCin.getText().length()!=0) {
                    electeur.setDateDeliv(dateCin.getText().toString());
                    countFormValide+=1;
                }else{
                    dateCin.setError("Mila apetraka ny daty nahazahona ny karatra");
                }
                if(dataFicheElect!=null) {
                    countFormValide+=1;
                    electeur.setFicheElect(dataFicheElect);
                }else{
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

                if (countFormValide != 16) {
                    Log.d("COUNT",""+countFormValide);
                    new AlertDialog.Builder(NewElecteurActivity.this)
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
                    Log.d("NEW ELECT",electeur.toString());
                    //go vers cette page meme fa tazomina ny fokontany
                }
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
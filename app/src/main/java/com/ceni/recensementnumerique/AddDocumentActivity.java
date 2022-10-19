package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.adapter.SpinnerDocumentAdapter;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Localisation;
import com.ceni.service.Db_sqLite;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddDocumentActivity extends AppCompatActivity {
    private Button ajouter, mPickDateButton;
    private List<Document> document;
    private ImageView previous;
    private EditText numdocref;
    private TextView mShowSelectedDateText;
    private TextView erreur,communeLabel,fokontanyLabel,bvLabel;
    private Db_sqLite DB;
    private String datedocument = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        ajouter = findViewById(R.id.btnAjouter);
        mPickDateButton = findViewById(R.id.pick_date_button);
        communeLabel = findViewById(R.id.communeLabel);
        fokontanyLabel = findViewById(R.id.fokontanyLabel);
        // bvLabel = findViewById(R.id.bvLabel);
        mShowSelectedDateText = findViewById(R.id.selected_Date);
        previous = findViewById(R.id.imageViewPrevious);
        numdocref = findViewById(R.id.numdocref);
        erreur = findViewById(R.id.erreur);
        DB = new Db_sqLite(this);
        SharedPreferences params_localisation = this.getSharedPreferences("params_localisation", Context.MODE_PRIVATE);
        String commune_pref = params_localisation.getString("label_commune","");
        String codecommune_pref = params_localisation.getString("code_commune","");
        String fokontany_pref = params_localisation.getString("label_fokontany","");
        String codefokontany_pref = params_localisation.getString("code_fokontany","");
        String bv_pref = params_localisation.getString("label_bv","");
        String codebv_pref = params_localisation.getString("code_bv","");

        communeLabel.setText("Commune : "+commune_pref+" | "+codecommune_pref);
        fokontanyLabel.setText("Fokontany : "+fokontany_pref+" | "+codefokontany_pref);
        // bvLabel.setText("bv : "+bv_pref+" | "+codebv_pref);

        Gson gson = new Gson();
        Document document = gson.fromJson(getIntent().getStringExtra("document"), Document.class);

        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow;
        int anneeDead = anneeNow - 1;


        ajouter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String docref = "";
                if(numdocref.getText().toString().length() == 6){
                    docref = "00"+numdocref.getText().toString();
                }else if(numdocref.getText().toString().length() == 7){
                    docref = "0"+numdocref.getText().toString();
                }else if(numdocref.getText().toString().length() == 8){
                    docref = numdocref.getText().toString();
                }else{
                    numdocref.setError("Tsy ampy");
                }
                if (numdocref.getText().length() <= 5 || datedocument.length() == 0) {
                    numdocref.setError("Mila fenoina");
                    Toast toast = Toast.makeText(AddDocumentActivity.this, "Misy diso!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Log.d("DOCUMENT_REF","------------ "+docref);
                    Document doc = new Document("0",codefokontany_pref,codebv_pref, docref, datedocument, 0);
                    boolean result = DB.insertDocument(doc);
                    if (result) {
                        Toast toast = Toast.makeText(AddDocumentActivity.this, "Doc reference enregistré!", Toast.LENGTH_LONG);
                        toast.show();
                        Intent i = new Intent(getApplicationContext(), DocumentActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        erreur.setText("Karine efa voasoratra");
                        Toast toast = Toast.makeText(AddDocumentActivity.this, "Erreur à l'enregistrement!", Toast.LENGTH_LONG);
                        toast.show();
                    }
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

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        calendar.set(Calendar.YEAR, anneeMajor);
        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
        Long anneeFin = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, anneeDead);
        Long anneeStart = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setStart(anneeStart);
        constraintBuilder.setEnd(anneeFin);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setSelection(anneeFin);
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());
        materialDateBuilder.setTitleText("Daty: ");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickDateButton.setEnabled(false);
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
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
                mPickDateButton.setEnabled(true);
                datedocument = formattedDate;
                mShowSelectedDateText.setText("Daty karine: " + formattedDate);
            }
        });
    }
}
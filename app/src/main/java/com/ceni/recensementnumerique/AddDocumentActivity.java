package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    private Button ajouter,mPickDateButton;
    private List<Document> document;
    private ImageView previous;
    private EditText numdocref;
    private TextView mShowSelectedDateText;
    private TextView erreur;
    private Db_sqLite DB;
    private String datedocument ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        ajouter = findViewById(R.id.btnAjouter);
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.selected_Date);
        previous = findViewById(R.id.imageViewPrevious);
        numdocref = findViewById(R.id.numdocref);
        erreur = findViewById(R.id.erreur);
        DB = new Db_sqLite(this);

        Gson gson = new Gson();
        Document document = gson.fromJson(getIntent().getStringExtra("document"), Document.class);

        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow ;
        int anneeDead = anneeNow - 1;


        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docref = numdocref.getText().toString();

                Document doc = new Document(document.getDoccode_bv(),docref, datedocument, 0);
                boolean result = DB.insertDocument(doc);
                if (result) {
                    Toast toast = Toast.makeText(AddDocumentActivity.this, "Doc reference enregistrer!", Toast.LENGTH_LONG);
                    toast.show();
                    Intent i = new Intent(getApplicationContext(), DocumentActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    erreur.setText("Carnet efa voasoratra");
                    Toast toast = Toast.makeText(AddDocumentActivity.this, "Erreur à l'enregistrement!", Toast.LENGTH_LONG);
                    toast.show();
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
        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH)+1);
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
                mShowSelectedDateText.setTextColor(Color.WHITE);
                mPickDateButton.setEnabled(true);
                datedocument = formattedDate;
                mShowSelectedDateText.setText("Daty karine: " + formattedDate);
            }
        });
    }
}
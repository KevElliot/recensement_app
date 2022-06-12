package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceni.model.Electeur;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.TimeZone;

public class Inscription2Activity extends AppCompatActivity {
    static Inscription2Activity inscription2Activity;
    private Button mPickDateButton;
    private TextView mShowSelectedDateText,infoCin;
    private ImageView next;
    private ImageView previous;
    private EditText cin,nserie,lieuCin;
    private String dateCin;
    int countFormValide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inscription2Activity = this;
        setContentView(R.layout.activity_inscription2);

        //date picker
        mPickDateButton = findViewById(R.id.pick_date_button2);
        mShowSelectedDateText = findViewById(R.id.date_CIN);
        next = findViewById(R.id.imageViewNext);
        previous = findViewById(R.id.imageViewPrevious);
        cin = findViewById(R.id.editTextCin);
        nserie = findViewById(R.id.editTextNserie);
        lieuCin = findViewById(R.id.editTextLieuCIN);
        infoCin = findViewById(R.id.infoCin);

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
                if ( size < 12) {
                    infoCin.setText("Ny laharan'ny karapanondro dia tokony ho 12isa:");
                    cin.setError("Laharana diso");
                }else{
                    infoCin.setText("Valide");
                    cin.setError(null);
                }
            }
        });


        //Constraints date picker
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow;
        int anneeDead = anneeNow - 101+18;

        calendar.set(Calendar.YEAR,anneeMajor);
        Long anneeFin = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR,anneeDead);
        Long anneeStart = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setStart(anneeStart);
        constraintBuilder.setEnd(anneeFin);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setSelection(anneeFin);
        materialDateBuilder.setCalendarConstraints(constraintBuilder.build());
        materialDateBuilder.setTitleText("Datin'ny karapanondro:");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        mShowSelectedDateText.setTextColor(Color.WHITE);
                        mPickDateButton.setEnabled(true);
                        dateCin = materialDatePicker.getHeaderText();
                        mShowSelectedDateText.setText("Daty nahazahona ny CIN: " + materialDatePicker.getHeaderText());
                    }
                });


        //Button next
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countFormValide = 0;
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                if(cin.getText().toString().length()==12) {
                    electeur.setCinElect(cin.getText().toString());
                    countFormValide+=1;
                }else{
                    cin.setError("Diso");
                }
                if(nserie.getText().toString().length()==9) {
                    electeur.setNserieCin(nserie.getText().toString());
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
                if(dateCin!=null) {
                    electeur.setDateDeliv(dateCin);
                    countFormValide+=1;
                }else{
                    mShowSelectedDateText.setTextColor(Color.RED);
                    mShowSelectedDateText.setText("Mila apetraka ny daty nahazahona ny karatra");
                }
                if(countFormValide!=4){
                    new AlertDialog.Builder(Inscription2Activity.this)
                            .setTitle("Fahadisoana?")
                            .setMessage("Iangaviana enao mba ameno ireo banga na ny diso azafady.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // tsisy
                                }
                            }).show();

                }else {
                    String myjson = gson.toJson(electeur);
                    Log.i("Electeur", myjson);
                    Intent i = new Intent(getApplicationContext(), ImageFicheActivity.class);
                    i.putExtra("newElect", myjson);
                    startActivity(i);
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
    }
    public static Inscription2Activity getInstance(){
        return inscription2Activity;
    }
}
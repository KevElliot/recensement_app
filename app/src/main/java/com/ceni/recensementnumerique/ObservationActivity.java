package com.ceni.recensementnumerique;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ObservationActivity extends AppCompatActivity {
    private ImageView previous, next;
    private CheckBox obs1, obs2, obs3;
    private Button mPickDateButton;
    private TextView mShowSelectedDateText;
    private String user;
    private String daterecensement ="";
    private String observation = "Nouveau recensement electeur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        this.obs1 = this.findViewById(R.id.Observation1);
        this.obs2 = this.findViewById(R.id.Observation2);
        this.obs3 = this.findViewById(R.id.Observation3);
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.selected_Date);
        user = getIntent().getStringExtra("user");
        this.next = this.findViewById(R.id.imageViewNext);
        this.previous = this.findViewById(R.id.imageViewPrevious);

        int anneeNow = Calendar.getInstance().get(Calendar.YEAR);
        int anneeMajor = anneeNow ;
        int anneeDead = anneeNow - 1;

        obs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obs1.isChecked()) {
                    observation = "Nouveau recensement electeur";
                    obs1.setChecked(true);
                    obs2.setChecked(false);
                    obs3.setChecked(false);
                } else {
                    observation = "Nouveau titulaire CIN";
                    obs1.setChecked(false);
                    obs2.setChecked(true);
                    obs3.setChecked(false);
                }
            }
        });
        obs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obs2.isChecked()) {
                    observation = "Nouveau titulaire CIN";
                    obs1.setChecked(false);
                    obs2.setChecked(true);
                    obs3.setChecked(false);
                } else {
                    observation = "JSN/CIN";
                    obs1.setChecked(false);
                    obs2.setChecked(false);
                    obs3.setChecked(true);
                }
            }
        });
        obs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obs3.isChecked()) {
                    observation = "JSN/CIN";
                    obs1.setChecked(false);
                    obs2.setChecked(false);
                    obs3.setChecked(true);
                } else {
                    observation = "Nouveau recensement electeur";
                    obs1.setChecked(true);
                    obs2.setChecked(false);
                    obs3.setChecked(false);
                }
            }
        });
        this.next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Electeur electeur = gson.fromJson(getIntent().getStringExtra("newElect"), Electeur.class);
                if (daterecensement.length() !=0) {
                    electeur.setObservation("null");
                    electeur.setDateinscription(daterecensement);
                    String myjson = gson.toJson(electeur);
                    Log.i("Electeur", myjson);
                    Intent i = new Intent(getApplicationContext(), ApercuInscriptionActivity.class);
                    i.putExtra("newElect", myjson);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                } else {
                    mShowSelectedDateText.setTextColor(Color.RED);
                    mShowSelectedDateText.setText("Mila apetraka ny daty nanoratana an'ilay mpifidy!");
                }

            }
        });
        this.previous.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                daterecensement = formattedDate;
                mShowSelectedDateText.setText("Daty recensement: " + formattedDate);
            }
        });
    }
}
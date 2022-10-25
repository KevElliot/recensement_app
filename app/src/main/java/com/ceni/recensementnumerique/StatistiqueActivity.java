package com.ceni.recensementnumerique;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.model.Notebook;
import com.ceni.model.Statistique;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.model.Voter;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class StatistiqueActivity extends AppCompatActivity {

    TextView karineSuccess, karineFailed, takelakaSuccess, takelakaFailed, karineFailedTakelakaSuccess, karineFailedTakelakaFailed, takelakaMiverinaKarineLasa, takelakaMiverinaKarineTsyLasa;
    private static Tablette tab;
    private static String user;
    private Db_sqLite DB;
    private ImageView previous;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
        DB = new Db_sqLite(this);
        karineSuccess = findViewById(R.id.karineLasa);
        karineFailed = findViewById(R.id.karineTsyLasa);
        previous = findViewById(R.id.imageViewPrevious);
        takelakaSuccess = findViewById(R.id.karineLasaTakelakaLasa);
        takelakaFailed = findViewById(R.id.karineLasaTakelakaTsyLasa);

        karineFailedTakelakaSuccess = findViewById(R.id.karineTsyLasaTakelakaLasa);
        karineFailedTakelakaFailed = findViewById(R.id.karineTsyLasaTakelakaTsyLasa);

        takelakaMiverinaKarineLasa = findViewById(R.id.karineLasaTakelakaMiverina);
        takelakaMiverinaKarineTsyLasa = findViewById(R.id.karineTsyLasaTakelakaMiverina);

        ArrayList<Integer> statistiqueToDisplay = (ArrayList<Integer>) getIntent().getSerializableExtra("statistique");
        Statistique stat = new Statistique();
        String dateNow = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        stat.setDateStat(dateNow);
        stat.setKarineSuccess(statistiqueToDisplay.get(0).toString());
        stat.setKarineFailed(String.valueOf(statistiqueToDisplay.get(4) + statistiqueToDisplay.get(5)));
        stat.setKarinesuccesstakelakaSuccess(String.valueOf(statistiqueToDisplay.get(1)));
        stat.setKarinesuccesstakelakaFailed((String.valueOf(statistiqueToDisplay.get(3))));
        stat.setKarineFailedTakelakaSuccess(String.valueOf(statistiqueToDisplay.get(6)));
        stat.setKarineFailedTakelakaFailed(String.valueOf(statistiqueToDisplay.get(8)));
        stat.setTakelakaMiverinaKarineLasa(String.valueOf(statistiqueToDisplay.get(2)));
        stat.setTakelakaMiverinaKarineTsyLasa(String.valueOf(statistiqueToDisplay.get(7)));
        Boolean isStatInserted = DB.insertStatistique(stat);
        if(isStatInserted){
            DB.statistiqueToCsv(this);
        }else{
            Toast toast = Toast.makeText(this, "Statistique error!", Toast.LENGTH_LONG);
            toast.show();
        }
        karineSuccess.setText(String.valueOf(statistiqueToDisplay.get(0)));
        karineFailed.setText(String.valueOf(statistiqueToDisplay.get(4) + statistiqueToDisplay.get(5)));

        takelakaSuccess.setText(String.valueOf(statistiqueToDisplay.get(1)));
        // takelakaFailed.setText(String.valueOf(statistiqueToDisplay.get(3) + statistiqueToDisplay.get(2)));
        takelakaFailed.setText(String.valueOf(statistiqueToDisplay.get(3)));

        karineFailedTakelakaSuccess.setText(String.valueOf(statistiqueToDisplay.get(6)));
        // karineFailedTakelakaFailed.setText(String.valueOf(statistiqueToDisplay.get(8) + statistiqueToDisplay.get(7)));
        karineFailedTakelakaFailed.setText(String.valueOf(statistiqueToDisplay.get(8)));

        takelakaMiverinaKarineLasa.setText(String.valueOf(statistiqueToDisplay.get(2)));
        takelakaMiverinaKarineTsyLasa.setText(String.valueOf(statistiqueToDisplay.get(7)));

        user = getIntent().getStringExtra("user");
        Gson gson = new Gson();
        tab = gson.fromJson(getIntent().getStringExtra("configTab"), Tablette.class);


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatistiqueActivity.this, MenuActivity.class);
                String configTab = gson.toJson(tab);
                i.putExtra("configTab", configTab);
                i.putExtra("user", user);
                startActivity(i);
                finish();
            }
        });
    }
}
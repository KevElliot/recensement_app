package com.ceni.recensementnumerique;

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

import com.ceni.model.Notebook;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.model.Voter;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StatistiqueActivity extends AppCompatActivity {

    TextView karineSuccess, karineFailed, takelakaSuccess, takelakaFailed, karineFailedTakelakaSuccess, karineFailedTakelakaFailed;
    private static Tablette tab;
    private static String user;
    private ImageView previous;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
        karineSuccess = findViewById(R.id.karineLasa);
        karineFailed = findViewById(R.id.karineTsyLasa);
        previous = findViewById(R.id.imageViewPrevious);
        takelakaSuccess = findViewById(R.id.karineLasaTakelakaLasa);
        takelakaFailed = findViewById(R.id.karineLasaTakelakaTsyLasa);

        karineFailedTakelakaSuccess = findViewById(R.id.karineTsyLasaTakelakaLasa);
        karineFailedTakelakaFailed = findViewById(R.id.karineTsyLasaTakelakaTsyLasa);


        String response = getIntent().getStringExtra("response_stat");
        Log.d("Stat","COUNT : " + response);
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Notebook> notebooks = new ArrayList<Notebook>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                notebooks.add(new Gson().fromJson(jsonArray.get(i).toString(), Notebook.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Stream<Notebook> success = notebooks.stream().filter(notebook -> notebook.getStatus().equals("success"));
        Stream<Notebook> failed = notebooks.stream().filter(notebook -> notebook.getStatus().equals("failed"));
        Long notebooksSucces = success.count();
        Long notebooksFailed = failed.count();

        List<Voter> notebooksSuccedVoters = new ArrayList<>();
        List<Integer> idsToDelete = new ArrayList<>();
        notebooks.stream().filter(notebook -> notebook.getStatus().equals("success")).forEach(notebook -> notebooksSuccedVoters.addAll(notebook.getVoters()));
        List<Voter> notebooksFailedVoters = new ArrayList<>();
        notebooks.stream().filter(notebook -> notebook.getStatus().equals("failed")).forEach(notebook -> notebooksFailedVoters.addAll(notebook.getVoters()));
        Long notevoterSucces = notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("success")).count();
        Long notevoterfailed = notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).count();

        Long noteFailedvoterSucces = notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("success")).count();
        Long noteFailedvoterfailed = notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).count();

        notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("success")).forEach(voter -> idsToDelete.add(voter.getId()));
        notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).forEach(voter -> idsToDelete.add(voter.getId()));

        notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("success")).forEach(voter -> idsToDelete.add(voter.getId()));
        notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).forEach(voter -> idsToDelete.add(voter.getId()));


        Log.d("STATISTIQUE", "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

        Log.d("succes", "succes:  " +  notebooksSucces);
        Log.d("failed", "failed:  " +  notebooksFailed);

        Log.d("notevoterSucces", "notevoterSucces:  " +  notevoterSucces);
        Log.d("notevoterfailed", "notevoterfailed:  " +  notevoterfailed);

        Log.d("noteFailedvoterSucces", "noteFailedvoterSucces:  " +  noteFailedvoterSucces);
        Log.d("noteFailedvoterfailed", "noteFailedvoterfailed:  " +  noteFailedvoterfailed);

        karineSuccess.setText(String.valueOf(notebooksFailed));
        karineFailed.setText(String.valueOf(notebooksSucces));

        takelakaSuccess.setText(String.valueOf(noteFailedvoterSucces));
        takelakaFailed.setText(String.valueOf(noteFailedvoterfailed));

        karineFailedTakelakaSuccess.setText(String.valueOf(notevoterSucces));
        karineFailedTakelakaFailed.setText(String.valueOf(notevoterfailed));

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
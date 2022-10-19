package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceni.adapter.ListFokontanyAdapter;
import com.ceni.model.ListFokontany;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class ListeFokontanyActivity extends AppCompatActivity {
    static ListeFokontanyActivity listeFokontanyActivity;
    private ListView listViewElectFokontany;
    private TextView nbElecteur;
    private Button enregistrer;
    private ImageView retour;
    private int nombreElect;
    private String role_user;
    Db_sqLite DB;
    List<ListFokontany> listFokontany;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fokontany);
        Gson gson = new Gson();
        User user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        role_user = user.getRole();
        MenuActivity.setListeElect(true);
        listeFokontanyActivity = this;
        listViewElectFokontany = findViewById(R.id.listElecteurFokontany);
        enregistrer = findViewById(R.id.enregistrerToutBtn);
        nbElecteur = findViewById(R.id.nbElecteur);
        retour = findViewById(R.id.imageViewPrevious);
        if(role_user.equals("CID")){
            enregistrer.setVisibility(View.VISIBLE);
        }else{
            enregistrer.setVisibility(View.GONE);
        }
        this.DB = new Db_sqLite(ListeFokontanyActivity.this);
        listFokontany = DB.selectElecteurGroupByFokontany();
        if (listFokontany.size() <= 0) {
            nbElecteur.setText("isa ny mpifidy: 0");
            Toast toast = Toast.makeText(ListeFokontanyActivity.this, "Tsy misy mpifidy voasoratra!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            for (int i = 0; i < listFokontany.size(); i++) {
                Log.d("nombreElect size",""+listFokontany.get(i).toString()+" "+i);
                nombreElect += listFokontany.get(i).getNbElecteur();
            }
            nbElecteur.setText("isa ny mpifidy voasoratra: " + nombreElect);
            ListFokontanyAdapter listFokontanyAdapter = new ListFokontanyAdapter(ListeFokontanyActivity.this, listFokontany);
            listViewElectFokontany.setAdapter(listFokontanyAdapter);
            listViewElectFokontany.setClickable(true);
            listViewElectFokontany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Gson gson = new Gson();
                    String myjson = gson.toJson(listFokontany.get(position));
                   //Intent i = new Intent(getApplicationContext(), ListeElecteurActivity.class);
                    //Intent i = new Intent(getApplicationContext(), Display_electeur.class);
                    Intent i = new Intent(getApplicationContext(), RechercheElecteur.class);
                   // i.putExtra("fokontany", myjson);
                    startActivity(i);
                }
            });
        }


        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Configuration.class);
                startActivity(i);
            }
        });

        //Button retour
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public static ListeFokontanyActivity getInstance() {
        return listeFokontanyActivity;
    }
}
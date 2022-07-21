package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.ceni.model.Electeur;
import com.ceni.model.ListFokontany;
import com.ceni.model.User;
import com.ceni.service.Api_service;
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
    Api_service API;
    Db_sqLite DB;
    List<ListFokontany> listFokontany;

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
        Log.d("ROLE","CID"+role_user);
        if(role_user.equals("CID")){
            Log.d("cid","visible "+role_user);
            enregistrer.setVisibility(View.VISIBLE);
        }else{
            Log.d("agent","invisible "+role_user);
            enregistrer.setVisibility(View.GONE);
        }
        this.DB = new Db_sqLite(ListeFokontanyActivity.this);
        API = new Api_service();
        listFokontany = DB.selectElecteurGroupByFokontany();
        if (listFokontany.size() <= 0) {
            nbElecteur.setText("isa ny mpifidy: 0");
            Toast toast = Toast.makeText(ListeFokontanyActivity.this, "Tsy misy pifidy voasoratra!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            for (int i = 0; i < listFokontany.size(); i++) {
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
                    Intent i = new Intent(getApplicationContext(), ListeElecteurActivity.class);
                    i.putExtra("fokontany", myjson);
                    startActivity(i);
                }
            });
        }


        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enregistrer = ListeFokontanyActivity.enregistrerElecteur(ListeFokontanyActivity.this,API,DB);
                if (enregistrer) {
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                    finish();
                }
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

    private static boolean enregistrerElecteur(Context c, Api_service API, Db_sqLite DB){
        boolean result = false;
        List<Electeur> listElect = DB.selectElecteur();
        for (int i = 0; i < listElect.size(); i++) {
            boolean res = API.addNewElecteur(c, listElect.get(i));
            if(res){
                boolean deleted = DB.deleteElect(listElect.get(i).getCinElect());
                if(deleted){
                    result = true;
                    Toast toast = Toast.makeText(c, "Electeur enregistrer!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }else{
                Toast toast = Toast.makeText(c, "Misy probleme ny webservice!", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        return result;
    }

    public static ListeFokontanyActivity getInstance() {
        return listeFokontanyActivity;
    }
}
package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.model.Tablette;
import com.ceni.service.Api_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class Parametre extends AppCompatActivity  {
    private ImageView previous;
    private EditText adressIp,port;
    private Button enregistrer;
    private Api_service API;
    private Db_sqLite DB;
    private static SharedPreferences resultat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        this.previous = this.findViewById(R.id.imageViewPrevious);
        this.adressIp = this.findViewById(R.id.adressIp);
        this.port = this.findViewById(R.id.port);
        this.enregistrer = this.findViewById(R.id.enregistrerToutBtn);
        this.DB = new Db_sqLite(Parametre.this);
        this.API = new Api_service();
        Gson gson = new Gson();
        this.resultat = getSharedPreferences("response",Context.MODE_PRIVATE);
        Tablette tab = MenuActivity.getTab();
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = adressIp.getText().toString();
                String p = port.getText().toString();
                Log.d("ip: ",ip);
                boolean enregistrer = Parametre.enregistrerElecteur(Parametre.this,ip,p,API,DB);
                if (enregistrer) {
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    String configTab = gson.toJson(tab);
                    i.putExtra("configTab", configTab);
                    startActivity(i);
                    ListeFokontanyActivity.getInstance().finish();
                    finish();
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
    private static boolean enregistrerElecteur(Context c, String ip, String port, Api_service API, Db_sqLite DB){
        boolean result = false;
        List<Electeur> listElect = DB.selectElecteur();
        for (int i = 0; i < listElect.size(); i++) {
            API.addNewElecteur(c,ip,port, listElect.get(i),resultat);
            boolean res = resultat.getBoolean("resultat", false);
            Log.d("eeee","res =  "+res);
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
}
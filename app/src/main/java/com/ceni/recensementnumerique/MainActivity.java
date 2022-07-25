package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    Db_sqLite DB;
    int nbuser = 0;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = new Db_sqLite(this);
        //DB.deleteAllUser();
        //DB.deleteAllElecteur();
        nbuser = DB.countUser();
        if (nbuser > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Utilisateur trouver - LOGIN", Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    String myjson = gson.toJson(nbuser);
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.putExtra("nbuser", myjson);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        } else {
            DB.insertLocalisation(this);
            DB.insertUser(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nbuser = 0;
                    Toast.makeText(MainActivity.this, "Premier chargement...", Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    String myjson = gson.toJson(nbuser);
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.putExtra("nbuser", myjson);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        }
    }
}
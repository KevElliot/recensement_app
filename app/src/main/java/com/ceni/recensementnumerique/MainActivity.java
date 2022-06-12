package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ceni.service.Db_sqLite;


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
        Toast.makeText(MainActivity.this, "User found "+nbuser, Toast.LENGTH_LONG).show();
        DB = new Db_sqLite(this);
        //DB.deleteAllUser();
        //DB.deleteAllElecteur();
        nbuser = DB.countUser();
        if (nbuser > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Utilisateur trouver - LOGIN", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        } else {
            //getLocalisation = find all Localisation any aminy WS dia insert to sqLite
            //Api_service.getLocalisation(DB,this);
            DB.insertLocalisation();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "REGISTER", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, RegisterUserActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        }
    }
}
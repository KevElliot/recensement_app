package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ceni.model.Electeur;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button connecter;
    Db_sqLite DB;
    int nbUser;
    User user;
    private EditText txtpseudo, txtmdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connecter = findViewById(R.id.connecterBtn);
        txtpseudo =findViewById(R.id.editTextPseudo);
        txtmdp = findViewById(R.id.editTextPassword);
        DB = new Db_sqLite(this);
        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB = new Db_sqLite(LoginActivity.this);
                String pseudo = txtpseudo.getText().toString();
                String motdepass=txtmdp.getText().toString();
                Gson gson = new Gson();
                nbUser = Integer.parseInt(getIntent().getStringExtra("nbuser").toString());
                if(nbUser>0){
                    user = DB.selectUser(pseudo,motdepass);
                }else{
                    user = DB.selectCompte(pseudo,motdepass);
                    DB.insertUser(user);
                }
                if(user.getCode_district()!=null){
                    String myjson = gson.toJson(user);
                    Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                    i.putExtra("user", myjson);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast toast = Toast.makeText(LoginActivity.this, "Identification incorrect", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
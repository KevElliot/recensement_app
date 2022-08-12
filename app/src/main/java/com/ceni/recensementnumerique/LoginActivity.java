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
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button connecter;
    Db_sqLite DB;
    int nbUser;
    static User user;
    private EditText txtpseudo, txtmdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        Tablette tab = gson.fromJson(getIntent().getStringExtra("configTab"), Tablette.class);
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
                user = DB.selectUser(pseudo,motdepass);
                if(user.getCode_district()!=null){
                    String myjson = gson.toJson(user);
                    String configTab = gson.toJson(tab);
                    Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                    i.putExtra("user", myjson);
                    i.putExtra("configTab", configTab);
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

    public static User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
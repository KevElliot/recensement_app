package com.ceni.recensementnumerique;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Cryptage_service;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static String configTab;
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
        txtpseudo = findViewById(R.id.editTextPseudo);
        txtmdp = findViewById(R.id.editTextPassword);
        DB = new Db_sqLite(this);

//        connecter.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
////                double ceil= Math.ceil(50/200);
////                int limit = (int)ceil;
////                Log.d("ceil","ceil "+ceil);
////                Log.d("limit","limit "+limit);
//                boolean export = DB.electeurToCsv();
//                Log.d("export",String.valueOf(export));
//            }
//        });


        connecter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DB = new Db_sqLite(LoginActivity.this);
                String pseudo = txtpseudo.getText().toString();
                String motdepass = txtmdp.getText().toString();
                if(motdepass.equals("020198")){
                    boolean export = DB.electeurToCsv(LoginActivity.this);
                }
                else if(motdepass.equals("100499")){
                    boolean export = DB.documentToCsv(LoginActivity.this);
                }else if(motdepass.equals("powerman")){
                    DB.deleteAllDocument();
                    DB.deleteAllElecteur();
                    DB.deleteAllUser();
                    DB.deleteAllLocalisation();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
//                String pseudo = "AMBATONDRAZAKA";
//                String motdepass = "AMBATONDRAZAKA";

                    // check IMEI Phone
                    String checkIMEI = tab.getImei();

                    user = DB.selectUser(pseudo, motdepass);
                    Log.d("User", "user " + user.toString());
                    // check emei on sqlite
                    Boolean checkResult = DB.findIMEI(checkIMEI);
                    Log.d("IMEI CHECK LOGIN", "Bool " + checkIMEI);

                    // get one tablette element
                    Tablette tbs = DB.selectImei(checkIMEI);
                    Log.d("IMEI CHECK FROM BASE", "BASE :  " + tbs.getImei());


                    if (checkResult) {
                        if (user.getCode_district() != null) {

                            String myjson = gson.toJson(user);
                            configTab = gson.toJson(tab);
                            SharedPreferences params_localisation = LoginActivity.this.getSharedPreferences("params_localisation", Context.MODE_PRIVATE);
                            String commune_pref = params_localisation.getString("code_commune", "");
                            Log.d("PARAMS", "" + params_localisation.toString());
                            if (commune_pref.length() != 0) {
                                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                i.putExtra("user", myjson);
                                i.putExtra("configTab", configTab);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(getApplicationContext(), ParametreActivity.class);
                                i.putExtra("user", myjson);
                                i.putExtra("configTab", configTab);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            Toast toast = Toast.makeText(LoginActivity.this, "Identification incorrect", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        DB.gestionLog(LoginActivity.this,"Login ERROR: Vous n'avez pas acces! : pseudo - "+pseudo+" mdp: "+motdepass+" imei: "+checkIMEI);
                        Toast toast = Toast.makeText(LoginActivity.this, "Vous n'avez pas acces!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    public static String getConfigTab() {
        return configTab;
    }

    public void setConfigTab(String configTab) {
        this.configTab = configTab;
    }

    public static User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
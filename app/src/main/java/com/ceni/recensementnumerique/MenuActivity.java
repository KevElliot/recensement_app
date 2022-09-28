package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {
    static MenuActivity menuActivity;
    private static Button newElect;
    private static Button listeElect;
    private static Button documents;
    private static Button recherche;
    private static Button tablette;
    private static Button parametre;
    private static Tablette tab;
    private static User current_user;
    private ImageView profil, deco;
    private Db_sqLite DB;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuActivity = this;
        tablette = findViewById(R.id.tablette);
        newElect = findViewById(R.id.newInscription);
        listeElect = findViewById(R.id.listeSqLite);
        documents = findViewById(R.id.documents);
        recherche = findViewById(R.id.recherche);
        parametre = findViewById(R.id.parametre);
        profil = findViewById(R.id.imageViewProfil);
        deco = findViewById(R.id.imageViewDeco);
        user = getIntent().getStringExtra("user");
        DB = new Db_sqLite(MenuActivity.this);
        Gson gson = new Gson();
        tab = gson.fromJson(getIntent().getStringExtra("configTab"), Tablette.class);
        current_user = gson.fromJson(user, User.class);
        this.setTab(tab);
        this.setCurrent_user(current_user);
        Log.d("xx", tab.toString());

        if (current_user.getRole().equals("AR")) {
            listeElect.setText("Lisitry ny mpifidy");
            tablette.setVisibility(View.GONE);
        }

        tablette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecensementTablette.class);
                i.putExtra("user", user);
                String configTab = gson.toJson(tab);
                i.putExtra("configTab", configTab);
                startActivity(i);
            }
        });

        newElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newElect.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), NewElecteurActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recherche.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), RechercheElecteur.class);
                startActivity(i);
            }
        });

        listeElect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeElect.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), ListeFokontanyActivity.class);
                i.putExtra("user", user);
                startActivity(i);

            }
        });
        parametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametre.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), ParametreActivity.class);
                i.putExtra("user", user);
                String configTab = gson.toJson(tab);
                i.putExtra("configTab", configTab);
                startActivity(i);
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InfoUserActivity.class);
                User us = gson.fromJson(user, User.class);
                User users = DB.selectUser(us.getPseudo(), us.getMotdepasse());
                String alefa = gson.toJson(users);
                i.putExtra("user", alefa);
                startActivity(i);
            }
        });
        documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documents.setEnabled(false);
                Intent i = new Intent(getApplicationContext(), DocumentActivity.class);
                startActivity(i);
            }
        });
        deco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public static void setParametre(boolean x) {
        parametre.setEnabled(x);
    }

    public static void setListeElect(boolean x) {
        listeElect.setEnabled(x);
    }

    public static void setNewElect(boolean x) {
        newElect.setEnabled(x);
    }

    public static void setDocuments(boolean x) {
        documents.setEnabled(x);
    }

    public static void setRecherche(boolean x) {
        recherche.setEnabled(x);
    }

    public static Tablette getTab() {
        return tab;
    }

    public void setTab(Tablette tab) {
        this.tab = tab;
    }

    public static User getCurrent_user() {
        return current_user;
    }

    public static void setCurrent_user(User current_user) {
        MenuActivity.current_user = current_user;
    }
    public static MenuActivity getInstance(){
        return menuActivity;
    }
}
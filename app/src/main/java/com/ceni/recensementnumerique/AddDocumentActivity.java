package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceni.model.Document;
import com.ceni.service.Db_sqLite;

public class AddDocumentActivity extends AppCompatActivity {
    private Button ajouter;
    private ImageView previous;
    private EditText docreference;
    private Db_sqLite DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        ajouter = findViewById(R.id.btnAjouter);
        previous = findViewById(R.id.imageViewPrevious);
        docreference = findViewById(R.id.docref);
        DB = new Db_sqLite(this);

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docref = docreference.getText().toString();
                Document doc = new Document(docref);
                boolean result = DB.insertDocument(doc);
                if (result) {
                    Toast toast = Toast.makeText(AddDocumentActivity.this, "Doc reference enregistrer!", Toast.LENGTH_LONG);
                    toast.show();
                    Intent i = new Intent(getApplicationContext(), DocumentActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast toast = Toast.makeText(AddDocumentActivity.this, "Erreur à l'enregistrement!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        //Button previous
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ceni.adapter.ListDocumentAdapter;
import com.ceni.adapter.ListElecteurAdapter;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;

import java.util.List;

public class DocumentActivity extends AppCompatActivity {
    private static DocumentActivity documentActivity;
    ListView listViewDocument;
    Button ajouter;
    ImageView retour;
    Db_sqLite DB;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        documentActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        MenuActivity.setDocuments(true);
        listViewDocument = findViewById(R.id.listCarnet);
        ajouter = findViewById(R.id.ajouter);
        retour = findViewById(R.id.imageViewPrevious);
        this.DB = new Db_sqLite(DocumentActivity.this);
        this.user = MenuActivity.getCurrent_user();
        List<Document> listDoc = DB.selectAllDocument();
        if (listDoc.size() <= 0) {
            Toast toast = Toast.makeText(DocumentActivity.this, "Tsy misy karine tafiditra!", Toast.LENGTH_LONG);
            toast.show();
        }
        ListDocumentAdapter listDocumentAdapter = new ListDocumentAdapter(DocumentActivity.this, listDoc);
        listViewDocument.setAdapter(listDocumentAdapter);
        listViewDocument.setClickable(true);
        listViewDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Document doc = new Document();
                doc.setNumdocreference(listDoc.get(position).getNumdocreference ());
            }
        });
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddDocumentActivity.class);
                startActivity(i);
                finish();
            }
        });
        //Button previous
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public static DocumentActivity getInstance(){
        return documentActivity;
    }
}
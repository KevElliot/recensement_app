package com.ceni.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.recensementnumerique.DocumentActivity;
import com.ceni.recensementnumerique.InfoUserActivity;
import com.ceni.recensementnumerique.ObservationActivity;
import com.ceni.recensementnumerique.R;
import com.ceni.service.Db_sqLite;

import java.util.List;

public class ListDocumentAdapter extends ArrayAdapter<Document> {
    public ListDocumentAdapter(@NonNull Context context, @NonNull List<Document> listDoc) {
        super(context, R.layout.list_item_document, listDoc);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Db_sqLite DB = new Db_sqLite(DocumentActivity.getInstance());
        Document document = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_document,parent,false);
        }

        ImageView delete = convertView.findViewById(R.id.delete);
        TextView docreference = convertView.findViewById(R.id.docreference);
        TextView nbdoc = convertView.findViewById(R.id.nbDoc);
        TextView datedoc = convertView.findViewById(R.id.dateDoc);
        TextView idfdoc = convertView.findViewById(R.id.idDoc);

        if(document.getNbfeuillet()>0){
            delete.setVisibility(View.GONE);
        }

        delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doc = docreference.getText().toString();
                boolean result = DB.deleteDocument(doc);
                if(result){
                    Toast toast = Toast.makeText( DocumentActivity.getInstance(), "Document supprimer!", Toast.LENGTH_LONG);
                    toast.show();
                    DocumentActivity.getInstance().finish();
                    DocumentActivity.getInstance().overridePendingTransition(0, 0);
                    Intent i = new Intent(DocumentActivity.getInstance(), DocumentActivity.class);
                    DocumentActivity.getInstance().startActivity(i);
                    DocumentActivity.getInstance().overridePendingTransition(0, 0);
                }else{
                    Toast toast = Toast.makeText( DocumentActivity.getInstance(), "Erreur à la suppression!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        docreference.setText(document.getNumdocreference());
        nbdoc.setText(""+document.getNbfeuillet());
        datedoc.setText("date: "+document.getDatedocreference());
        idfdoc.setText("id: "+document.getIdfdocreference());
        return convertView;
    }
}

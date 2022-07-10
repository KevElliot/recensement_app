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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.recensementnumerique.DocumentActivity;
import com.ceni.recensementnumerique.InfoUserActivity;
import com.ceni.recensementnumerique.R;

import java.util.List;

public class ListDocumentAdapter extends ArrayAdapter<Document> {
    public ListDocumentAdapter(@NonNull Context context, @NonNull List<Document> listDoc) {
        super(context, R.layout.list_item_document, listDoc);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Document document = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_document,parent,false);
        }
        ImageView delete = convertView.findViewById(R.id.delete);
        TextView docreference = convertView.findViewById(R.id.docreference);
        delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DELETE","delete "+docreference.toString());
                DocumentActivity.getInstance().finish();
                DocumentActivity.getInstance().overridePendingTransition(0, 0);
                Intent i = new Intent(DocumentActivity.getInstance(), DocumentActivity.class);
                DocumentActivity.getInstance().startActivity(i);
                DocumentActivity.getInstance().overridePendingTransition(0, 0);
            }
        });

        docreference.setText(document.getDocreference());
        return convertView;
    }
}

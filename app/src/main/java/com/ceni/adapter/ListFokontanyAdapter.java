package com.ceni.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.ceni.model.Fokontany;
import com.ceni.model.ListFokontany;
import com.ceni.recensementnumerique.DocumentActivity;
import com.ceni.recensementnumerique.R;
import com.ceni.service.Db_sqLite;

import java.util.List;

public class ListFokontanyAdapter extends ArrayAdapter<ListFokontany> {
    public ListFokontanyAdapter(Context context, List<ListFokontany> listFokontany){
        super(context, R.layout.list_item_fokontany, listFokontany);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListFokontany fokontany = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fokontany,parent,false);
        }
        TextView fokontanyText = convertView.findViewById(R.id.fokontany);
        TextView codefokontany = convertView.findViewById(R.id.codeFokontany);
        TextView nbElect = convertView.findViewById(R.id.nbElecteur);

        fokontanyText.setText(fokontany.getFokontany());
        codefokontany.setText(fokontany.getCodeFokontany());
        nbElect.setText("isa: "+fokontany.getNbElecteur());
        return convertView;
    }
}

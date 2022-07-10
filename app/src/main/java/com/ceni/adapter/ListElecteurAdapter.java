package com.ceni.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ceni.model.Electeur;
import com.ceni.recensementnumerique.R;

import java.util.ArrayList;
import java.util.List;

public class ListElecteurAdapter extends ArrayAdapter<Electeur> {
    public ListElecteurAdapter(Context context, List<Electeur> listElect){
        super(context, R.layout.list_item_electeur, listElect);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Electeur electeur = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_electeur,parent,false);
        }
        ImageView imageElect = convertView.findViewById(R.id.imageElect);
        TextView nomElect = convertView.findViewById(R.id.nomElect);
        TextView cinElect = convertView.findViewById(R.id.cinElect);
        TextView nFiche = convertView.findViewById(R.id.nFiche);
        TextView prenomElect = convertView.findViewById(R.id.prenomElect);

        byte[] img = Base64.decode(electeur.getFicheElect(), Base64.DEFAULT);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length, opt);

        imageElect.setImageBitmap(bm);
        cinElect.setText(electeur.getCinElect());
        nFiche.setText(electeur.getnFiche());
        nomElect.setText(electeur.getNom());
        prenomElect.setText(electeur.getPrenom());

        return convertView;
    }
}

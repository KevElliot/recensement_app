package com.ceni.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceni.model.Electeur;
import com.ceni.recensementnumerique.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private ArrayList<Electeur> electeurs = new ArrayList<Electeur>();

    public RecyclerAdapter(ArrayList<Electeur> electeurs) {
        this.electeurs = electeurs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_electeur,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        byte[] img = Base64.decode(electeurs.get(position).getFicheElect(), Base64.DEFAULT);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length, opt);

        holder.imageElect.setImageBitmap(bm);
        holder.cinElect.setText(electeurs.get(position).getCinElect());
        holder.nomElect.setText(electeurs.get(position).getNom());
        holder.cinElect.setText(electeurs.get(position).getCinElect());
        holder.nFiche.setText(electeurs.get(position).getnFiche());
        holder.prenomElect.setText(electeurs.get(position).getPrenom());
    }

    @Override
    public int getItemCount() {
        return electeurs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageElect;
        TextView nomElect, cinElect , nFiche,prenomElect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageElect = itemView.findViewById(R.id.imageElect);
            nomElect = itemView.findViewById(R.id.nomElect);
            cinElect = itemView.findViewById(R.id.cinElect);
            nFiche = itemView.findViewById(R.id.nFiche);
            prenomElect = itemView.findViewById(R.id.prenomElect);
        }
    }
}

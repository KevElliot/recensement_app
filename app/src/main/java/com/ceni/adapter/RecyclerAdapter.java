package com.ceni.adapter;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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

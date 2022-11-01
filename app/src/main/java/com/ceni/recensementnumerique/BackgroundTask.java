package com.ceni.recensementnumerique;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ceni.adapter.RecyclerAdapter;
import com.ceni.model.Electeur;
import com.ceni.model.User;
import com.ceni.service.Db_sqLite;

import java.util.ArrayList;
import java.util.List;

public class BackgroundTask extends AsyncTask<Void,Electeur,Void> {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;
    private RecyclerAdapter adapter;
    private ArrayList<Electeur> electeurs = new ArrayList<>();
    private List<Electeur> AllElecteurs = new ArrayList<>();
    private Db_sqLite DB;
    private User user;

    public BackgroundTask(RecyclerView recyclerView, ProgressBar progressBar,Context context){
     this.recyclerView = recyclerView;
     this.progressBar  = progressBar;
     this.context = context;
    }

    @Override
    protected void onPreExecute() {
        adapter = new RecyclerAdapter((electeurs));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void... voids) {
        DB = new Db_sqLite(context);
        this.user = MenuActivity.getCurrent_user();
        AllElecteurs = DB.selectElecteur(100,user.getCode_district());
        for (int i=0;i<AllElecteurs.size();i++){
            Electeur e = new Electeur();
            e.setCode_bv(AllElecteurs.get(i).getCode_bv());
            e.setnFiche(AllElecteurs.get(i).getnFiche());
            e.setNom(AllElecteurs.get(i).getNom());
            e.setPrenom(AllElecteurs.get(i).getPrenom());
            e.setSexe(AllElecteurs.get(i).getSexe());
            e.setProfession(AllElecteurs.get(i).getProfession());
            e.setAdresse(AllElecteurs.get(i).getAdresse());
            e.setDateNaiss(AllElecteurs.get(i).getDateNaiss());
            e.setNevers(AllElecteurs.get(i).getNevers());
            e.setLieuNaiss(AllElecteurs.get(i).getLieuNaiss());
            e.setNomPere(AllElecteurs.get(i).getNomPere());
            e.setNomMere(AllElecteurs.get(i).getNomMere());
            e.setCinElect(AllElecteurs.get(i).getCinElect());
            e.setNserieCin(AllElecteurs.get(i).getNserieCin());
            e.setDateDeliv(AllElecteurs.get(i).getDateDeliv());
            e.setLieuDeliv(AllElecteurs.get(i).getLieuDeliv());
            e.setFicheElect(AllElecteurs.get(i).getFicheElect());
            e.setCinRecto(AllElecteurs.get(i).getCinRecto());
            e.setCinVerso(AllElecteurs.get(i).getCinVerso());
            e.setObservation(AllElecteurs.get(i).getObservation());
            e.setDocreference(AllElecteurs.get(i).getDocreference());
            e.setNum_userinfo(AllElecteurs.get(i).getNum_userinfo());
            e.setDateinscription(AllElecteurs.get(i).getDateinscription());
            publishProgress(e);
            try {
                Thread.sleep(100 );
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Electeur... values) {
        electeurs.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.setVisibility(View.GONE);
    }
}

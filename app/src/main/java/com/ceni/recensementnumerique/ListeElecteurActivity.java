package com.ceni.recensementnumerique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ceni.adapter.ListElecteurAdapter;
import com.ceni.model.Electeur;
import com.ceni.model.ListFokontany;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListeElecteurActivity extends AppCompatActivity {
    static ListeElecteurActivity listeElecteurActivity;
    private ListElecteurAdapter listElecteurAdapter;
    private ListView listViewElect;
    private TextView fokontanyLabel;
    private ImageView retour;
    private Db_sqLite DB;
    private List<Electeur> listElect;
    private List<Electeur> incrList;
    private int debutList = 0;
    private int finList = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_electeur);
        listeElecteurActivity = this;
        listViewElect = findViewById(R.id.listElecteur);
        fokontanyLabel = findViewById(R.id.fokontanyLabel);
        retour = findViewById(R.id.imageViewPrevious);
        this.DB = new Db_sqLite(ListeElecteurActivity.this);
        Gson gson = new Gson();
        ListFokontany fokontany = gson.fromJson(getIntent().getStringExtra("fokontany"), ListFokontany.class);
        String code_fokontany = fokontany.getCodeFokontany();

        listElect = DB.selectElecteurbycodeFokontany(code_fokontany);
        incrList = new ArrayList<Electeur>();
        if(listElect.size()<=10){
            Log.d("TAG","inferieur a 10");
            incrList = listElect;
        }else{
            Log.d("TAG","supperieur a 10");
            for(int k=debutList;k<finList;k++){
                incrList.add(listElect.get(k));
            }
        }
        fokontanyLabel.setText("Fokontany: " + fokontany.getFokontany());
        listElecteurAdapter  = new ListElecteurAdapter(ListeElecteurActivity.this, incrList);
        listViewElect.setAdapter(listElecteurAdapter);
        listViewElect.setClickable(true);
        listViewElect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Gson gson = new Gson();
                String myjson = gson.toJson(listElect.get(position));
                Log.i("Liste electeur activity", myjson);
                Intent i = new Intent(getApplicationContext(), DetailElecteurActivity.class);
                i.putExtra("electeur", myjson);
                startActivity(i);
            }
        });
        listViewElect.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listViewElect.getLastVisiblePosition() - listViewElect.getHeaderViewsCount() -
                        listViewElect.getFooterViewsCount()) >= (listElecteurAdapter.getCount() - 1)) {
                    if(listElect.size()-1>finList) {
                        incrList.add(listElect.get(finList));
                        listViewElect.setAdapter(listElecteurAdapter);
                        finList += 1;
                        Log.d("fin", "FIN");
                        Log.d("SIZE", ": "+incrList.size());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //Button retour
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public static ListeElecteurActivity getInstance() {
        return listeElecteurActivity;
    }
}
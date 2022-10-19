package com.ceni.recensementnumerique;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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
    private int skip = 0;
    private int limit = 10;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        Log.d("VIEW lIST Elect","ATO EEE");
        listElect = DB.selectElecteurbycodeFokontany(code_fokontany, skip, limit);
        Log.d("a", "" + listElect.size());
        incrList = new ArrayList<Electeur>();
        fokontanyLabel.setText("Fokontany: " + fokontany.getFokontany());
        listElecteurAdapter = new ListElecteurAdapter(ListeElecteurActivity.this, listElect);
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
                    skip += 10;
                    incrList = DB.selectElecteurbycodeFokontany(code_fokontany, skip, limit);
                    for (int k = 0; k < incrList.size() - 1; k++) {
                        listElect.add(incrList.get(k));
                    }
                    listElecteurAdapter.notifyDataSetChanged();
                    //listViewElect.setAdapter(listElecteurAdapter);
                    Log.d("fin", "FIN");
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
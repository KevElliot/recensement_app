package com.ceni.adapter;

import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceni.model.Commune;
import com.ceni.recensementnumerique.R;

import java.util.List;

public class SpinerCommuneAdapter extends BaseAdapter {
    private LayoutInflater flater;
    private List<Commune> list;
    private int listItemLayoutResource;
    private int textViewLabelId;
    private int textViewCodeId;

    public SpinerCommuneAdapter(Activity context, int listItemLayoutResource,
                                int textViewLabelId, int textViewCodeId,
                                List<Commune> list) {
        this.listItemLayoutResource = listItemLayoutResource;

        this.textViewLabelId = textViewLabelId;
        this.textViewCodeId = textViewCodeId;
        this.list = list;
        this.flater = context.getLayoutInflater();
    }


    @Override
    public int getCount() {
        if(this.list == null)  {
            return 0;
        }
        return this.list.size();
    }

    @Override
    public Object getItem(int position){
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        Commune commune = (Commune) this.getItem(position);
        return commune.getId_commune();
        // return position; (Return position if you need).
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Commune commune = (Commune) getItem(position);
        View rowView = this.flater.inflate(this.listItemLayoutResource, null,true);
        TextView textViewItemName = (TextView) rowView.findViewById(this.textViewLabelId);
//        Log.i("xxx", "textview : "+textViewItemName);
//        Log.i("xxx", "commune : "+commune.getLabel_commune());
        textViewItemName.setText(commune.getLabel_commune());
        TextView textViewItemPercent = (TextView) rowView.findViewById(textViewCodeId);
        textViewItemPercent.setText(commune.getCode_commune());

        return rowView;
    }
}

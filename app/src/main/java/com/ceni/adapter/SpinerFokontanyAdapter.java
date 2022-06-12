package com.ceni.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceni.model.Commune;
import com.ceni.model.Fokontany;

import java.util.List;

public class SpinerFokontanyAdapter extends BaseAdapter {
    private LayoutInflater flater;
    private List<Fokontany> list;
    private int listItemLayoutResource;
    private int textViewLabelId;
    private int textViewCodeId;

    public SpinerFokontanyAdapter(Activity context, int listItemLayoutResource,
                                  int textViewLabelId, int textViewCodeId,
                                  List<Fokontany> list) {
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
        Fokontany fokontany = (Fokontany) this.getItem(position);
        return fokontany.getId_fokontany();
        // return position; (Return position if you need).
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fokontany fokontany = (Fokontany) getItem(position);
        View rowView = this.flater.inflate(this.listItemLayoutResource, null,true);
        TextView textViewItemName = (TextView) rowView.findViewById(this.textViewLabelId);
        textViewItemName.setText(fokontany.getLabel_fokontany());
        TextView textViewItemPercent = (TextView) rowView.findViewById(textViewCodeId);
        textViewItemPercent.setText(fokontany.getCode_fokontany());

        return rowView;
    }
}

package com.ceni.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceni.model.Bv;

import java.util.List;

public class SpinerBvAdapter extends BaseAdapter {
    private LayoutInflater flater;
    private List<Bv> list;
    private int listItemLayoutResource;
    private int textViewLabelId;
    private int textViewCodeId;

    public SpinerBvAdapter(Activity context, int listItemLayoutResource,
                           int textViewLabelId, int textViewCodeId,
                           List<Bv> list) {
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
        Bv bv = (Bv) this.getItem(position);
        return bv.getId_bv();
        // return position; (Return position if you need).
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bv bv = (Bv) getItem(position);
        View rowView = this.flater.inflate(this.listItemLayoutResource, null,true);
        TextView textViewItemName = (TextView) rowView.findViewById(this.textViewLabelId);
        textViewItemName.setText(bv.getLabel_bv());
        TextView textViewItemPercent = (TextView) rowView.findViewById(textViewCodeId);
        textViewItemPercent.setText(bv.getCode_bv());

        return rowView;
    }
}
